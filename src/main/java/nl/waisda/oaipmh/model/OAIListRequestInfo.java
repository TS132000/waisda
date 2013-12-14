package nl.waisda.oaipmh.model;

/**
 * User: Danny
 * Date: 15-10-13
 * Time: 17:28
 */
public class OAIListRequestInfo {
    private byte[] resumptionInfo;
    private String metadataPrefix;

    public OAIListRequestInfo(String metadataPrefix, byte[] resumptionInfo) {

        this.resumptionInfo = resumptionInfo;
        this.metadataPrefix = metadataPrefix;
    }

    public byte[] getResumptionInfo() {
        return resumptionInfo;
    }

    public String getMetadataPrefix() {
        return metadataPrefix;
    }

}
