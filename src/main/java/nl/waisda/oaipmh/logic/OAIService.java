package nl.waisda.oaipmh.logic;

import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMH;

/**
 * User: Danny
 * Date: 10-9-13
 * Time: 13:06
 */
public interface OAIService {
    OAIPMH createIdentify();
    OAIPMH createGetRecordType(String identifier, String metadataPrefix);
    OAIPMH createListIdentifiers(String from, String until, String set, String resumptionToken, String metadataPrefix);
    OAIPMH createListRecords(String from, String until, String set, String resumptionToken, String metadataPrefix);
    OAIPMH createListMetadataFormats(String identifier);
    OAIPMH createListSets();
}
