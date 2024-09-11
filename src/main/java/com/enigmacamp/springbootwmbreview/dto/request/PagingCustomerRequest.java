package com.enigmacamp.springbootwmbreview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//class ini bisa juga digunakan untuk terima param untuk paging entity lain juga
public class PagingCustomerRequest {
    private Integer page;
    private Integer size;
}
