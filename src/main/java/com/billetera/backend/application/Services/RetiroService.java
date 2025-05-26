package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;
import com.billetera.backend.infrastructure.dto.RetiroResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetiroService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final JavaMailSender mailSender;

    private final ConcurrentHashMap<String, CodigoVerificacion> codigosVerificacion = new ConcurrentHashMap<>();
    private static final int LONGITUD_CODIGO = 6;
    private static final int TIEMPO_EXPIRACION_MINUTOS = 10;

    @Transactional
    public void solicitarCodigoRetiro(String telefono) {
        Usuario usuario = usuarioRepository.findByTelefono(telefono)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new RuntimeException("El usuario no tiene un correo electrónico registrado");
        }

        String codigo = generarCodigo();
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(TIEMPO_EXPIRACION_MINUTOS);

        codigosVerificacion.put(telefono, new CodigoVerificacion(codigo, expiracion));

        enviarEmailCodigo(usuario.getEmail(), codigo, expiracion);
    }

    @Transactional
    public RetiroResponse realizarRetiro(String telefono, BigDecimal monto, String codigoIngresado) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }

        Usuario usuario = usuarioRepository.findByTelefono(telefono)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        validarCodigo(telefono, codigoIngresado);

        Cuenta cuenta = usuario.getCuenta();

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar el retiro");
        }

        BigDecimal saldoAnterior = cuenta.getSaldo();
        cuenta.setSaldo(saldoAnterior.subtract(monto));
        cuentaRepository.save(cuenta);

        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setMonto(monto);
        transaccion.setTipoTransaccion("RETIRO");
        transaccion.setDescripcion("Retiro con verificación por correo");
        transaccion.setEstado("COMPLETADO");
        transaccionRepository.save(transaccion);

        return new RetiroResponse(
                true,
                "Retiro realizado con éxito",
                telefono,
                monto,
                cuenta.getSaldo());
    }

    private void validarCodigo(String telefono, String codigoIngresado) {
        CodigoVerificacion codigoVerificacion = codigosVerificacion.get(telefono);

        if (codigoVerificacion == null) {
            throw new RuntimeException("No hay código de verificación pendiente para este usuario");
        }

        if (LocalDateTime.now().isAfter(codigoVerificacion.expiracion())) {
            codigosVerificacion.remove(telefono);
            throw new RuntimeException("El código de verificación ha expirado");
        }

        if (!codigoVerificacion.codigo().equals(codigoIngresado)) {
            throw new RuntimeException("Código de verificación incorrecto");
        }

        codigosVerificacion.remove(telefono);
    }

    private String generarCodigo() {
        Random random = new Random();
        return String.format("%0" + LONGITUD_CODIGO + "d", random.nextInt((int) Math.pow(10, LONGITUD_CODIGO)));
    }

    private void enviarEmailCodigo(String emailDestino, String codigo, LocalDateTime expiracion) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestino);
            helper.setSubject("Código de verificación para retiro - BankNet");

            String fechaExpiracion = expiracion.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                            .withLocale(Locale.forLanguageTag("es-ES")));

            String contenidoHtml = String.format(
                    """
                            <html>
                              <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;">
                                <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                                  <h2 style="color: #2c3e50; text-align: center;">Verificación de Retiro</h2>
                                  <p style="font-size: 16px; color: #34495e;">Hemos recibido una solicitud de retiro. Por favor utiliza el siguiente código para autorizar la operación:</p>

                                  <div style="text-align: center; margin: 30px 0;">
                                    <div style="display: inline-block; padding: 15px 25px; background-color: #f8f9fa; border-radius: 5px; border: 1px dashed #ddd; font-size: 24px; letter-spacing: 2px; color: #2c3e50;">
                                      %s
                                    </div>
                                  </div>

                                  <p style="font-size: 14px; color: #7f8c8d; text-align: center;">
                                    Este código expirará el %s
                                  </p>

                                  <p style="font-size: 14px; color: #7f8c8d;">
                                    Si no has solicitado este retiro, por favor ignora este mensaje o contacta con nuestro soporte inmediatamente.
                                  </p>
                                </div>
                              </body>
                            </html>
                            """,
                    codigo,
                    fechaExpiracion);

            helper.setText(contenidoHtml, true);
            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            log.error("Error al enviar el código de verificación por correo: {}", e.getMessage());
            throw new RuntimeException("Error al enviar el código de verificación", e);
        }
    }

    private record CodigoVerificacion(String codigo, LocalDateTime expiracion) {
    }
}