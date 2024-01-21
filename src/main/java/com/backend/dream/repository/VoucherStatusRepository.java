package com.backend.dream.repository;

import com.backend.dream.entity.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherStatusRepository extends JpaRepository<VoucherStatus,Long> {
}
