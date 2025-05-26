package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.TicketSoporte;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.domain.ports.TicketSoporteRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;
import com.billetera.backend.infrastructure.dto.TicketRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketSoporteService {

    private final TicketSoporteRepository ticketRepo;
    private final UsuarioRepository usuarioRepo;
    private final JavaMailSender mailSender;


    @Transactional
    public TicketSoporte crearTicket(TicketRequest request) {

        TicketSoporte ticket = new TicketSoporte();
        ticket.setAsunto(request.asunto());
        ticket.setMensaje(request.mensaje());
        ticket.setNombreContacto(request.nombreContacto());
        ticket.setCorreoContacto(request.correoContacto());

        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepo.findById(request.usuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            ticket.setUsuario(usuario);
        }

        TicketSoporte ticketGuardado = ticketRepo.save(ticket);

        enviarEmailSoporte(ticketGuardado);
        enviarEmailConfirmacion(ticketGuardado);

        return ticketGuardado;
    }

    private void enviarEmailSoporte(TicketSoporte ticket) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("banknet2025@gmail.com");
            helper.setSubject("[Ticket #" + ticket.getId() + "] " + ticket.getAsunto());

            String fechaFormateada = ticket.getFechaCreacion()
                    .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm 'hs'")
                            .withLocale(Locale.forLanguageTag("es-ES")));

            String contenidoHtml = String.format(
                    """
                            <html>
                              <body style="font-family: 'Segoe UI', sans-serif; background-color: #f8f9fa; padding: 20px;">
                                <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                  <h2 style="color: #004085; border-bottom: 1px solid #e2e3e5; padding-bottom: 10px;">📩 Nuevo Ticket de Soporte</h2>
                                  <p style="font-size: 16px;"><strong>Cliente:</strong> %s</p>
                                  <p style="font-size: 16px;"><strong>Email:</strong> %s</p>
                                  <p style="font-size: 16px;"><strong>Asunto:</strong> %s</p>
                                  <p style="font-size: 16px;"><strong>Mensaje:</strong><br>%s</p>
                                  <p style="font-size: 16px;"><strong>Fecha:</strong> %s</p>
                                </div>
                              </body>
                            </html>
                            """,
                    ticket.getNombreContacto(),
                    ticket.getCorreoContacto(),
                    ticket.getAsunto(),
                    ticket.getMensaje().replaceAll("\n", "<br>"),
                    fechaFormateada);

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            log.error("Error enviando email al equipo de soporte: {}", e.getMessage());
        }
    }

    private void enviarEmailConfirmacion(TicketSoporte ticket) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(ticket.getCorreoContacto());
            helper.setSubject("[Ticket #" + ticket.getId() + "] Confirmación de recepción");

            String fechaFormateada = ticket.getFechaCreacion()
                    .format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm 'hs'")
                            .withLocale(Locale.forLanguageTag("es-ES")));

            String contenidoHtml = String.format(
                    """
                            <html>
                              <body style="font-family: 'Segoe UI', sans-serif; background-color: #f8f9fa; padding: 20px;">
                                <div style="max-width: 600px; margin: auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                                  <h2 style="color: #004085; border-bottom: 1px solid #e2e3e5; padding-bottom: 10px;">✅ Confirmación de Ticket</h2>
                                  <p style="font-size: 16px;">Hola <strong>%s</strong>,</p>
                                  <p style="font-size: 16px;">
                                    Hemos recibido tu <strong>ticket de soporte</strong> con la siguiente información:
                                  </p>
                                  <ul style="font-size: 16px; padding-left: 20px;">
                                    <li><strong>Asunto:</strong> %s</li>
                                    <li><strong>ID de Ticket:</strong> %d</li>
                                    <li><strong>Fecha:</strong> %s</li>
                                  </ul>
                                  <p style="font-size: 16px;"><strong>Tu mensaje:</strong><br>%s</p>
                                  <br>
                                  <p style="font-size: 16px;">Nuestro equipo se pondrá en contacto contigo lo antes posible.</p>
                                  <p style="font-size: 16px;">Saludos,<br><strong>Equipo de Soporte – BankNet</strong></p>
                                </div>
                              </body>
                            </html>
                            """,
                    ticket.getNombreContacto(),
                    ticket.getAsunto(),
                    ticket.getId(),
                    fechaFormateada,
                    ticket.getMensaje().replaceAll("\n", "<br>"));

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            log.error("Error enviando email de confirmación al cliente: {}", e.getMessage());
        }
    }


    @Transactional(readOnly = true)
    public List<TicketSoporte> obtenerTicketsPorUsuario(Long usuarioId) {
        return ticketRepo.findByUsuarioId(usuarioId);
    }

}

