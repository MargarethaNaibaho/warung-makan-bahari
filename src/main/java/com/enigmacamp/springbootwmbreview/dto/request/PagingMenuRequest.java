package com.enigmacamp.springbootwmbreview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingMenuRequest {
    private Integer page;
    private Integer size;
    private String name;
    private Long minPrice;
    private Long maxPrice;
}
