package com.pda.utils.exception;



import com.pda.utils.exception.login.NotCorrectPasswordException;
import com.pda.utils.exception.login.NotFoundUserException;
import com.pda.utils.api_utils.ApiUtils;
import lombok.extern.slf4j.Slf4j;
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

    @ExceptionHandler({NotFoundUserException.class, NotCorrectPasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtils.ApiResult<String> handleBadRequestException(RuntimeException error) {
        String errorMessage = error.getMessage();
        return error(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
