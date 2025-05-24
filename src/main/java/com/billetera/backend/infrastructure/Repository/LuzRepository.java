package com.billetera.backend.infrastructure;
import com.billetera.backend.domain.Luz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuzRepository extends JpaRepository<Luz, Long> {
}
