package com.smartvote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {

    private long totalElections;
    private long ongoingElections;
    private long totalCandidates;
    private long totalVoters;
    private long totalVotesCast;
}