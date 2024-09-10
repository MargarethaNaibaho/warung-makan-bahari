package com.enigmacamp.springbootwmbreview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//ini dia terima tipe data generic dan kembalikan tipe data generic juga
public class CommonResponse <T>{
    private String message;
    private Integer statusCode;
    private T data;
}
