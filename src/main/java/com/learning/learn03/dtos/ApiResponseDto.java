package com.learning.learn03.dtos;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private String massage;
    private boolean success;
}
