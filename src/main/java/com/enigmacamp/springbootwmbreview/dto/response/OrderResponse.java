package com.enigmacamp.springbootwmbreview.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String orderId;
    private String customerId;
    private String tableName;
    private LocalDateTime transDate;
    private List<OrderDetailResponse> orderDetails;
}
