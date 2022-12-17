package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TelegramAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TelegramAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelegramAccountRepository extends JpaRepository<TelegramAccount, Long> {
    Optional<TelegramAccount> findByChatId(Long chatId);
}
