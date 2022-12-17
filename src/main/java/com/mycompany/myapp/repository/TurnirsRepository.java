package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Turnirs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Turnirs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurnirsRepository extends JpaRepository<Turnirs, Long> {}
