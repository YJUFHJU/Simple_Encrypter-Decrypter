package encryptdecrypt;

@SuppressWarnings("serial")
public class InvalidArgumentsException extends RuntimeException {
    private final String msg;

    InvalidArgumentsException(String msg) {
        this.msg = ": " + msg;
    }

    @Override
    public String toString() {
        return super.toString() + msg;
    }
}
