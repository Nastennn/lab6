package story;
public class LocationDoesNotExistException extends RuntimeException {
    public LocationDoesNotExistException(String message){
        super(message);
    }
}
