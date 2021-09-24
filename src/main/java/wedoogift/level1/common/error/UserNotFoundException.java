package wedoogift.level1.common.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(int id) {
        super("User " + id + " not found");
    }
}
