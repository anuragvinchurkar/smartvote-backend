package com.smartvote.service;

import com.smartvote.dto.response.DashboardStatsResponse;
import com.smartvote.dto.response.ElectionResultsResponse;
import com.smartvote.entity.Candidate;
import com.smartvote.entity.Election;
import com.smartvote.enums.ElectionStatus;
import com.smartvote.enums.Role;
import com.smartvote.repository.CandidateRepository;
import com.smartvote.repository.ElectionRepository;
import com.smartvote.repository.UserRepository;
import com.smartvote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public DashboardStatsResponse getDashboardStats() {
        return DashboardStatsResponse.builder()
                .totalElections(electionRepository.count())
                .ongoingElections(electionRepository.countByStatus(ElectionStatus.ONGOING))
                .totalCandidates(candidateRepository.count())
                .totalVoters(userRepository.countByRole(Role.VOTER))
                .totalVotesCast(voteRepository.count())
                .build();
    }

    public ElectionResultsResponse getElectionResults(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));

        List<Candidate> candidates = candidateRepository.findByElectionId(electionId);
        long totalVotes = candidates.stream()
                .mapToLong(c -> c.getVotes().size())
                .sum();

        List<ElectionResultsResponse.CandidateResult> results = candidates.stream()
                .map(candidate -> {
                    long voteCount = candidate.getVotes().size();
                    double percentage = totalVotes == 0 ? 0.0 : (voteCount * 100.0) / totalVotes;
                    return ElectionResultsResponse.CandidateResult.builder()
                            .candidateId(candidate.getId())
                            .candidateName(candidate.getName())
                            .party(candidate.getParty())
                            .voteCount(voteCount)
                            .votePercentage(Math.round(percentage * 100.0) / 100.0)
                            .build();
                })
                .sorted(Comparator.comparingLong(ElectionResultsResponse.CandidateResult::getVoteCount).reversed())
                .toList();

        return ElectionResultsResponse.builder()
                .electionId(election.getId())
                .electionTitle(election.getTitle())
                .status(election.getStatus().name())
                .totalVotes(totalVotes)
                .results(results)
                .build();
    }
}