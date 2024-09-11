package com.enigmacamp.springbootwmbreview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private Integer totalPages;
    private Long count; //ini jumlah elemen keseluruhan
    private Integer page;
    private Integer size;
}
