package Exceptions;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(String msg){
        super(msg);
    }
}
