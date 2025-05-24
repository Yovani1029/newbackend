package com.billetera.backend.infrastructure.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Internet;

public interface InternetRepository  extends JpaRepository<Internet, Long> {
    
}
