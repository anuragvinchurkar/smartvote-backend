package com.smartvote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectionResultsResponse {

    private Long electionId;
    private String electionTitle;
    private String status;
    private long totalVotes;
    private List<CandidateResult> results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CandidateResult {
        private Long candidateId;
        private String candidateName;
        private String party;
        private long voteCount;
        private double votePercentage;
    }
}