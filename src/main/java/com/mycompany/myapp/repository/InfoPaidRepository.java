package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InfoPaid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InfoPaid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfoPaidRepository extends JpaRepository<InfoPaid, Long> {}
