package com.enigmacamp.springbootwmbreview.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidationUtil {
    private final Validator validator;

    //dia bisa terima tipe data apapun
    public void validate(Object object){
        Set<ConstraintViolation<Object>> result = validator.validate(object);
        if(!result.isEmpty()){
            throw new ConstraintViolationException(result);
        }
    }
}
