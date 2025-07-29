package com.example.borimandoo_back.dto;

import com.example.borimandoo_back.domain.Request;
import com.example.borimandoo_back.domain.Vet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetFarmerRequestResponse {
    private Long requestId;
    private LocalDateTime createdAt;
    private Request.RequestStatus requestStatus;
    private Vet vet;
}
