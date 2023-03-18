package exceptions

import javassist.NotFoundException

class UnsupportedGlassException extends Exception {

    String message
    Exception exception

    public UnsupportedGlassException(String message, Exception exception) {
        super(message, exception)
        this.message = message
        this.exception = exception
    }

    public UnsupportedGlassException(String message) {
        this(message, new NotFoundException(""))
    }
}
