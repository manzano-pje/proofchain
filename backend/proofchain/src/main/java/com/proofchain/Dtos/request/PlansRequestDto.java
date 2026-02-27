package com.proofchain.Dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlansRequestDto {

    private String name;
    private double price;
    private int durationDays;
    private boolean isReccurent;
    private boolean isActive;
}
