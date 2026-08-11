package com.smartvote.service;

import com.smartvote.dto.request.ElectionRequest;
import com.smartvote.dto.response.ElectionResponse;
import com.smartvote.entity.Election;
import com.smartvote.enums.ElectionStatus;
import com.smartvote.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElectionService {

    private final ElectionRepository electionRepository;

    public ElectionResponse createElection(ElectionRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Election election = Election.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ElectionStatus.UPCOMING)
                .build();

        Election saved = electionRepository.save(election);
        return mapToResponse(saved);
    }

    public List<ElectionResponse> getAllElections() {
        return electionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ElectionResponse getElectionById(Long id) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + id));
        return mapToResponse(election);
    }

    public ElectionResponse updateElection(Long id, ElectionRequest request) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election not found with id: " + id));

        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        election.setTitle(request.getTitle());
        election.setDescription(request.getDescription());
        election.setStartTime(request.getStartTime());
        election.setEndTime(request.getEndTime());

        Election updated = electionRepository.save(election);
        return mapToResponse(updated);
    }

    public void deleteElection(Long id) {
        if (!electionRepository.existsById(id)) {
            throw new IllegalArgumentException("Election not found with id: " + id);
        }
        electionRepository.deleteById(id);
    }

    private ElectionResponse mapToResponse(Election election) {
        return ElectionResponse.builder()
                .id(election.getId())
                .title(election.getTitle())
                .description(election.getDescription())
                .startTime(election.getStartTime())
                .endTime(election.getEndTime())
                .status(election.getStatus())
                .candidateCount(election.getCandidates().size())
                .createdAt(election.getCreatedAt())
                .build();
    }
}