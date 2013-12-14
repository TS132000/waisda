package nl.waisda.oaipmh.model;

import java.util.List;

import org.joda.time.DateTime;

/**
 * User: Danny
 * Date: 15-10-13
 * Time: 16:26
 */
public class OAIListResult<E> {
    private byte[]      resumptionInfo;
    private DateTime    expirationDate;
    private List<E> dataEntries;

    public OAIListResult(byte[] resumptionInfo, DateTime expirationDate, List<E> dataEntries) {
        this.resumptionInfo = resumptionInfo;
        this.expirationDate = expirationDate;
        this.dataEntries = dataEntries;
    }

    public byte[] getResumptionInfo() {
        return resumptionInfo;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public List<E> getEntries() {
        return dataEntries;
    }
}
