package ies.castillodeluna.ad.ejercicios.errors;

public class DataAccessRuntimeException extends RuntimeException {

    public DataAccessRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public DataAccessRuntimeException(Throwable cause) {
        super(cause);
    }

    public DataAccessRuntimeException(String message) {
        super(message);
    }
    
}
