package nl.waisda.oaipmh.logic;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import nl.waisda.domain.TagEntry;
import nl.waisda.oaipmh.model.MetadataPrefix;
import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.OAIListResult;
import nl.waisda.oaipmh.model.jaxb.pmh.HeaderType;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 13:18
 */
@Component
public class OAIListIdentifiersDelegate extends OAIPMHDelegateBase {

    /**
     * creates a list of headers for all documents in given format and in the given date range
     * When resumptiontoken has a value, it is used to retrieve the proper page
     * @param resumptionInfo the resumption data to retrieve the next page
     * @param metadataPrefix the format to get the list in
     * @param from from date (nullable)
     * @param until until date (nullable)
     * @return the list of headers
     * @throws OAIException
     */
    public OAIListResult<HeaderType> getOAIListIdentifiers(byte[] resumptionInfo, String metadataPrefix, Date from, Date until) throws OAIException {
        List<HeaderType> resultList;
        MetadataPrefix metadataFormat =  validateAndGetMetadataPrefix(metadataPrefix);
        int page = 1;

        if (resumptionInfo != null) {
            try {
                page = Integer.parseInt(new String(resumptionInfo));
            } catch(Exception e) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, "Error parsing resumptiontoken");
            }
        }

        // get the data
        switch (metadataFormat) {
            case rdf:
                // set status
                resultList = buildHeaderTypeListWaisdaTagEntry(page);
            break;
            default:
                resultList = null; // shut up compiler
            break;

        }

        // generate the new resumption token
        resumptionInfo = String.valueOf(page + 1).getBytes();

        // DRS TODO: expiration date
        return new OAIListResult<HeaderType>(resumptionInfo, new DateTime(), resultList);
    }

    private List<HeaderType> buildHeaderTypeListWaisdaTagEntry(int page) throws OAIException {
        List<HeaderType> headerTypeList = new LinkedList<HeaderType>();
        HeaderType headerType;

        // read the data
        List<TagEntry> tagEntries = tagEntryRepository.getTags((page - 1) * PAGE_SIZE, PAGE_SIZE, 0);

        if (tagEntries.size() == 0) {
            if (page > 1) {
                throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, "Reached end of list");
            } else {
                throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, "Empty set");
            }
        }


        for (TagEntry tagEntry : tagEntries) {
            headerType = createHeader(tagEntry);
            headerTypeList.add(headerType);
        }

        return headerTypeList;
    }
}
