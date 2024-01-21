package com.backend.dream.util;

import org.springframework.validation.BindingResult;

public interface ValidationService {

    ErrorResponse validation(BindingResult bindingResult);
}
