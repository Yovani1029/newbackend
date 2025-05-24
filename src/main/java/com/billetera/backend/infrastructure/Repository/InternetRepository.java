package com.billetera.backend.infrastructure;
import com.billetera.backend.domain.Internet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternetRepository  extends JpaRepository<Internet, Long> {
    
}
