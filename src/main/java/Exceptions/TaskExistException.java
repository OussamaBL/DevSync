package Exceptions;

public class TaskExistException extends RuntimeException{
    public TaskExistException(String msg){
        super(msg);
    }
}
