package com.pda.utils.exception;



import com.pda.utils.exception.investment_tests.AnswerNotFoundException;
import com.pda.utils.exception.investment_tests.QuestionNotFoundException;
import com.pda.utils.exception.investment_tests.UserAnswerNotFoundException;
import com.pda.utils.exception.login.NotCorrectPasswordException;
import com.pda.utils.exception.login.NotFoundUserException;
import com.pda.utils.api_utils.ApiUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.pda.utils.api_utils.ApiUtils.error;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtils.ApiResult<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException errors) {
        Map<String, String> errorMessages = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String errorField = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.put(errorField, errorMessage);
        }
        return error(errorMessages, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiUtils.ApiResult<String> handleDuplicatedEmailException(DuplicatedEmailException error) {
        String errorMessage = error.getMessage();
        return error(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotFoundUserException.class, NotCorrectPasswordException.class,
            QuestionNotFoundException.class, AnswerNotFoundException.class, UserAnswerNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiUtils.ApiResult<String> handleBadRequestException(RuntimeException error) {
        String errorMessage = error.getMessage();
        return error(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
