package Exceptions;

public class TaskValidationException extends RuntimeException{
    public TaskValidationException(String msg){
        super(msg);
    }
}
