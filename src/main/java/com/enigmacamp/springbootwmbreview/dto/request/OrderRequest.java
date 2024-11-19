package com.enigmacamp.springbootwmbreview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customerId;

    //yg di bawah ini aku ganti dulu karna aku pake tableName di orderserviceimpl
    private String tableId;

    //ini kucomment karna mau pake tableId di react native mobile
//    private String tableName;
    private List<OrderDetailRequest> orderDetails; //penamaannya ini supaya ga bingugn pas di postman
}
