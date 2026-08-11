package com.smartvote.service;

import com.smartvote.dto.request.VoteRequest;
import com.smartvote.dto.response.VoteResponse;
import com.smartvote.entity.Candidate;
import com.smartvote.entity.Election;
import com.smartvote.entity.User;
import com.smartvote.entity.Vote;
import com.smartvote.enums.ElectionStatus;
import com.smartvote.repository.CandidateRepository;
import com.smartvote.repository.ElectionRepository;
import com.smartvote.repository.UserRepository;
import com.smartvote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public VoteResponse castVote(VoteRequest request) {
        // Step 1: Identify the logged-in voter from the JWT — never trust client input for this
        String voterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User voter = userRepository.findByEmail(voterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Voter not found"));

        // Step 2: Validate the election exists and is currently open for voting
        Election election = electionRepository.findById(request.getElectionId())
                .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + request.getElectionId()));

        if (election.getStatus() != ElectionStatus.ONGOING) {
            throw new IllegalStateException("Voting is not currently open for this election. Status: " + election.getStatus());
        }

        // Step 3: Validate the candidate exists AND actually belongs to this election
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + request.getCandidateId()));

        if (!candidate.getElection().getId().equals(election.getId())) {
            throw new IllegalArgumentException("This candidate does not belong to the specified election");
        }

        // Step 4: Ensure the voter hasn't already voted in this election
        if (voteRepository.existsByVoterIdAndElectionId(voter.getId(), election.getId())) {
            throw new IllegalStateException("You have already voted in this election");
        }

        // All checks passed — record the vote
        Vote vote = Vote.builder()
                .voter(voter)
                .candidate(candidate)
                .election(election)
                .build();

        Vote saved = voteRepository.save(vote);

        return VoteResponse.builder()
                .id(saved.getId())
                .voterName(voter.getFullName())
                .candidateName(candidate.getName())
                .electionTitle(election.getTitle())
                .votedAt(saved.getVotedAt())
                .build();
    }
}