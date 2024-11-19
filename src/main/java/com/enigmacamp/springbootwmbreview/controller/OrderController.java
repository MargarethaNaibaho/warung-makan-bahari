package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.request.OrderRequest;
import com.enigmacamp.springbootwmbreview.dto.response.CommonResponse;
import com.enigmacamp.springbootwmbreview.dto.response.OrderDetailResponse;
import com.enigmacamp.springbootwmbreview.dto.response.OrderResponse;
import com.enigmacamp.springbootwmbreview.entity.Order;
import com.enigmacamp.springbootwmbreview.service.OrderDetailService;
import com.enigmacamp.springbootwmbreview.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @PostMapping("/api/v1/transaction")
    //ResponseEntity    ini bukan entity yg aku buat sendiri. itu buatan si java
    //ResponseEntity untuk dapat httpstatus. ini juga bisa kita pake untuk error handling
    public ResponseEntity<CommonResponse<OrderResponse>> createNewTransaction(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.createNewTransaction(orderRequest);
        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
                .message("Successfully created new transaction")
                .statusCode(HttpStatus.CREATED.value())
                .data(orderResponse)
                .build();

        //ini .status , .body itu punya builder si response entity. bukan aku yg buat
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    //cara ini boleh juga tapi kurang disarankan karna errornya nanti ga bisa dicustom
//    public CommonResponse<OrderResponse> createNewTransaction(@RequestBody OrderRequest orderRequest){
//        OrderResponse orderResponse = orderService.createNewTransaction(orderRequest);
//        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
//                .message("Successfully created new transaction")
//                .statusCode(HttpStatus.CREATED.value())
//                .data(orderResponse)
//                .build();
//
//        return response;
//    }

    @GetMapping("/api/v1/transaction")
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getAllTransactions(){
        List<OrderResponse> orderResponses = orderService.getAll();
        CommonResponse<List<OrderResponse>> commonResponse = CommonResponse.<List<OrderResponse>>builder()
                .message("Successfully created new transasction")
                .statusCode(HttpStatus.CREATED.value())
                .data(orderResponses)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
    }

    @GetMapping("/api/v1/transaction/customer/{id}")
    public ResponseEntity<?> getAllTransactionsByCustomerId(@PathVariable String id){
        List<OrderResponse> orderResponses = orderService.getAllByCustomerId(id);
        CommonResponse<List<OrderResponse>> commonResponse = CommonResponse.<List<OrderResponse>>builder()
                .message("Successfully created new transasction")
                .statusCode(HttpStatus.CREATED.value())
                .data(orderResponses)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
    }

    //ini tanpa pake template Common Response
//    @GetMapping("/api/v1/transaction/{id}")
//    public OrderResponse getTransactionById(@PathVariable String id){
//        return orderService.getById(id);
//    }

    //pake tanda tanya ? yg artinya yg ditampung lebih bebas
    @GetMapping("/api/v1/transaction/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        OrderResponse orderResponse = orderService.getById(id);

        //kalo pake ini, kita ga bisa dapat response.getData().isian si orderresponse
//        CommonResponse<?> response = CommonResponse.builder()
//                .message("Successfully get transaction by id")
//                .data(orderResponse)
//                .build();

        //kalo pake ini, kita bisa dapat response.getData().iisian si Order Response
        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
                .message("Successfully get transaction by id")
                .statusCode(HttpStatus.OK.value())
                .data(orderResponse)
                .build();

        //apa beda pake tanda ? dengan OrderResponse sebagai isian CommonResponse?
        //kalo pake tanda tanya, nanti ga bisa ambil data data common response dari si order response
        //response.getData().

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

//    @GetMapping("/api/v1/transaction/detail/{id}")
//    public OrderDetailResponse getOrderDetailById(@PathVariable String id){
//        return  orderDetailService.getById(id);
//    }

    @GetMapping("/api/v1/transaction/detail/{id}")
    public ResponseEntity<CommonResponse<OrderDetailResponse>> getOrderDetailById(@PathVariable String id){
        OrderDetailResponse orderDetailResponse = orderDetailService.getById(id);
        CommonResponse<OrderDetailResponse> commonResponse = CommonResponse.<OrderDetailResponse>builder()
                .message("Successfully get transaction detail data by id")
                .data(orderDetailResponse)
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
    }
}
