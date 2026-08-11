package com.smartvote.repository;

import com.smartvote.entity.Election;
import com.smartvote.enums.ElectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {

    long countByStatus(ElectionStatus status);
}