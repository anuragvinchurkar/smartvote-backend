package com.smartvote.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {

    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Party name is required")
    private String party;

    private String bio;
}