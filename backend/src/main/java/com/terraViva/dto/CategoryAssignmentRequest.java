package com.terraViva.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAssignmentRequest {
    private Long categoryId;
    private List<Long> studentIds;
}
