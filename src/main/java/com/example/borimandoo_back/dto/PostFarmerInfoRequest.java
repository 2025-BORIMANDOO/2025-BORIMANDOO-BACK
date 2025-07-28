package com.example.borimandoo_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFarmerInfoRequest {
    private String phoneNumber;
    private String farmLocation;
}
