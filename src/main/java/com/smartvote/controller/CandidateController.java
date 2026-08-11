package com.smartvote.controller;

import com.smartvote.dto.request.CandidateRequest;
import com.smartvote.dto.response.CandidateResponse;
import com.smartvote.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping("/api/elections/{electionId}/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponse> addCandidate(
            @PathVariable Long electionId,
            @Valid @RequestBody CandidateRequest request
    ) {
        CandidateResponse response = candidateService.addCandidate(electionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/elections/{electionId}/candidates")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByElection(@PathVariable Long electionId) {
        return ResponseEntity.ok(candidateService.getCandidatesByElection(electionId));
    }

    @GetMapping("/api/candidates/{id}")
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @PutMapping("/api/candidates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateRequest request
    ) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, request));
    }

    @DeleteMapping("/api/candidates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}