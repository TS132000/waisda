package nl.waisda.oaipmh.model;

import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;

/**
 * User: Danny
 * Date: 9-9-13
 * Time: 16:13
 */
public class OAIException extends Exception {
    private OAIPMHerrorcodeType errorType;

    public OAIException(OAIPMHerrorcodeType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public OAIPMHerrorcodeType getErrorType() {
        return errorType;
    }
}
