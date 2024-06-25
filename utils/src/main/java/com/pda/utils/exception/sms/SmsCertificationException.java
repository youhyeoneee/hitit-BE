package com.pda.utils.exception.sms;

public class SmsCertificationException {

    public static class Exception extends RuntimeException {
        public Exception(String message) { super(message);}
    }

    public static class SmsCertificationNumberMismatchException extends RuntimeException{
        public SmsCertificationNumberMismatchException(String message){super(message);}
    }
}
