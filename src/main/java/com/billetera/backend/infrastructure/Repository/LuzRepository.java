package com.billetera.backend.infrastructure.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Luz;

public interface LuzRepository extends JpaRepository<Luz, Long> {
}
