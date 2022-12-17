package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Clans;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Clans entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClansRepository extends JpaRepository<Clans, Long> {}
