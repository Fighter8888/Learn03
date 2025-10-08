package com.learning.learn03.repositories;

import com.learning.learn03.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Optional<Account> findByUserName(String userName);

    Optional<Account> findByAccountAuthId(UUID accountId);
}
