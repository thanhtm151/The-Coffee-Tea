package com.backend.dream.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidationServiceImp implements ValidationService{

    @Override
    public ErrorResponse validation(BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse error = new ErrorResponse();
        error.setErrors(errors);

        return error;
    }
}
