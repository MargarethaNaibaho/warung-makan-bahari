package com.enigmacamp.springbootwmbreview.controller;

import com.enigmacamp.springbootwmbreview.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

//kita bisa pisahkan logic error. error pun ada logicnya cuy
//makanya kita pake anotasi @RestControllerAdvice
//macam macam logic. harusnya itu tiap macam harus dipisahkan dalam satu method spesifik
//nampaknya ini kek riweh, tapi ini sangat membantu dalam maintenance kodingan dan mudah dipahami
//fungsi ini digunakan untuk customize error handling. kalo ada status code yg macam macam, kita bisa customize juga gimana keluarannya
//ini anotasi dipake supaa nnti pas dipanggil isi @ExceptionHandler yaitu ResponseStatusException, dari sini yg diambil yg dah dicustom ini, bukan yg aslinya
@RestControllerAdvice
public class ErrorController {
    //ResponseStatusException ini kelas yg bukan kubuat
    //ResponseStatusException ini untuk hadnling status response nya
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> responseStatusException(ResponseStatusException e){
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity
                .status(e.getStatusCode()) //status code ini tergantung hasil dari client. status exception ini bisa kita pake untuk custom keluaran kode status. gimana penaganannya? di trace itu
                .body(response);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException e){
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) //status code ini tergantung hasil dari client. status exception ini bisa kita pake untuk custom keluaran kode status. gimana penaganannya? di trace itu
                .body(response);

    }

}
