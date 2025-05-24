package com.billetera.backend.infrastructure.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Agua;


public interface AguaRepository extends JpaRepository<Agua, Long>{
    
}

