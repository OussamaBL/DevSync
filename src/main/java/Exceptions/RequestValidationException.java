package Exceptions;

public class RequestValidationException extends RuntimeException{
    public RequestValidationException(String msg){
        super(msg);
    }
}
