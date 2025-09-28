package com.thirdeye3.messenger.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stock {
    private Long uniqueId;
    private String uniqueCode;
    private String marketCode;
}
