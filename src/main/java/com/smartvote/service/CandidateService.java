package com.smartvote.service;

import com.smartvote.dto.request.CandidateRequest;
import com.smartvote.dto.response.CandidateResponse;
import com.smartvote.entity.Candidate;
import com.smartvote.entity.Election;
import com.smartvote.repository.CandidateRepository;
import com.smartvote.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    public CandidateResponse addCandidate(Long electionId, CandidateRequest request) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + electionId));

        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .party(request.getParty())
                .bio(request.getBio())
                .election(election)
                .build();

        Candidate saved = candidateRepository.save(candidate);
        return mapToResponse(saved);
    }

    public List<CandidateResponse> getCandidatesByElection(Long electionId) {
        if (!electionRepository.existsById(electionId)) {
            throw new IllegalArgumentException("Election not found with id: " + electionId);
        }
        return candidateRepository.findByElectionId(electionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CandidateResponse getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));
        return mapToResponse(candidate);
    }

    public CandidateResponse updateCandidate(Long id, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));

        candidate.setName(request.getName());
        candidate.setParty(request.getParty());
        candidate.setBio(request.getBio());

        Candidate updated = candidateRepository.save(candidate);
        return mapToResponse(updated);
    }

    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new IllegalArgumentException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }

    private CandidateResponse mapToResponse(Candidate candidate) {
        return CandidateResponse.builder()
                .id(candidate.getId())
                .name(candidate.getName())
                .party(candidate.getParty())
                .bio(candidate.getBio())
                .electionId(candidate.getElection().getId())
                .electionTitle(candidate.getElection().getTitle())
                .voteCount(candidate.getVotes().size())
                .createdAt(candidate.getCreatedAt())
                .build();
    }
}