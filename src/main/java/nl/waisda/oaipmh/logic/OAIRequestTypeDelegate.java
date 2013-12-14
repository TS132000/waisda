package nl.waisda.oaipmh.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import nl.waisda.oaipmh.model.jaxb.pmh.RequestType;
import nl.waisda.oaipmh.model.jaxb.pmh.VerbType;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 14:04
 */
@Component
public class OAIRequestTypeDelegate extends OAIPMHDelegateBase {
    /**
     * create a request info type with given parameters
     * None of the arguments is mandatory
     * @param verbType the verb
     * @param identifier the identifier
     * @param metadataPrefix the metadataprefix
     * @return request info type
     */
    public RequestType getRequestTypeBase(VerbType verbType, String identifier, String metadataPrefix) {
        RequestType requestType = new RequestType();

        requestType.setVerb(verbType);
        requestType.setValue(createBaseURL());
        requestType.setIdentifier(identifier);
        requestType.setMetadataPrefix(metadataPrefix);

        return requestType;
    }

    /**
     *
     * @param requestType the request info type to enrich
     * @param from the from date
     * @param until the until date
     * @param set the set name
     * @param resumptionToken the resumption token
     * @return request info type (the same as passed into this method)
     */
    public RequestType enrichRequestType(RequestType requestType, String from, String until, String set, String resumptionToken) {
        requestType.setFrom(from);
        requestType.setUntil(until);
        requestType.setSet(set);
        try {
            requestType.setResumptionToken(resumptionToken != null ? URLEncoder.encode(resumptionToken, "UTF8") : null);
        } catch(UnsupportedEncodingException uee) { /* bs */ }

        return requestType;
    }

}
