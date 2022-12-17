package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {}
