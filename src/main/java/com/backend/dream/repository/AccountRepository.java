package com.backend.dream.repository;

import com.backend.dream.dto.AccountDTO;
import com.backend.dream.entity.Account;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT a.id FROM Account a WHERE a.username = :username")
    Long findIdByUsername(@Param("username") String username);
    @Query("SELECT a.fullname FROM Account a WHERE a.username = :username")
    String findFullNameByUsername(@Param("username") String username);
    @Query("SELECT a.avatar FROM Account a WHERE a.username = :username")
    String getImageByUsername(@Param("username") String username);
    @Query("SELECT a.address FROM Account a WHERE a.username = :username")
    String getAddressByUsername(@Param("username") String username);
    Account findByUsernameAndEmail(String username, String email);
    @Query("SELECT DISTINCT ar.account FROM Authority ar WHERE ar.role.id IN (1 ,2)")
    List<Account> getStaff();

    @Query("SELECT a.role.id FROM Authority a WHERE a.account.username = :username")
    Long findRoleIdByUsername(@Param("username") String username);

    @Query("SELECT a FROM Account a WHERE LOWER(a.fullname) LIKE LOWER(concat('%', :name, '%')) OR LOWER(a.username) LIKE LOWER(concat('%', :name, '%'))")
    List<Account> searchAccount(@Param("name") String name);

    @Query("SELECT a FROM Account a JOIN a.authority au WHERE au.role.id = ?1")
    List<Account> getUsersByRole(@Param("roleID") Long roleID);

}
