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
public class VoteResponse {

    private Long id;
    private String voterName;
    private String candidateName;
    private String electionTitle;
    private LocalDateTime votedAt;
}