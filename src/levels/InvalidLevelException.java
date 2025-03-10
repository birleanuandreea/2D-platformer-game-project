package levels;

public class InvalidLevelException extends Exception{

    public InvalidLevelException(){
        super("Invalid level index");
    }
}
