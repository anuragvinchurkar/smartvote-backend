package com.smartvote.repository;

import com.smartvote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByVoterIdAndElectionId(Long voterId, Long electionId);

    List<Vote> findByElectionId(Long electionId);

    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
}