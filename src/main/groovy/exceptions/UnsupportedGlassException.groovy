package exceptions

class UnsupportedGlassException extends Exception {

    String message
    Exception exception

    public UnsupportedGlassException(String message, Exception exception) {
        super(message, exception)
    }

    public UnsupportedGlassException(String message) {
        this(message, this)
    }
}
