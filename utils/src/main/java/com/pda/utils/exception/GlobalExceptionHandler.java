package com.pda.utils.exception;



import com.pda.utils.api_utils.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.pda.utils.api_utils.ApiUtils.error;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiUtils.ApiResult<String> handleDuplicatedEmailException(DuplicatedEmailException error) {
        String errorMessage = error.getMessage();
        return error(errorMessage, HttpStatus.CONFLICT);
    }
}
