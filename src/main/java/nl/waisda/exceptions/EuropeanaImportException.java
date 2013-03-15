package nl.waisda.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 8-3-13
 * Time: 13:57
 * To change this template use File | Settings | File Templates.
 */
public class EuropeanaImportException extends Exception {
    public EuropeanaImportException(String message) {
        super(message);
    }

    public EuropeanaImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public EuropeanaImportException(Throwable cause) {
        super(cause);
    }
}
