package com.smartvote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponse {

    private Long id;
    private String name;
    private String party;
    private String bio;
    private Long electionId;
    private String electionTitle;
    private int voteCount;
    private LocalDateTime createdAt;
}