package com.billetera.backend.infrastructure;
import com.billetera.backend.domain.Agua;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AguaRepository extends JpaRepository<Agua, Long>{
    
}

