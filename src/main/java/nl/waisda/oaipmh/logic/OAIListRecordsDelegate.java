package nl.waisda.oaipmh.logic;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import nl.waisda.domain.TagEntry;
import nl.waisda.domain.Video;
import nl.waisda.oaipmh.model.MetadataPrefix;
import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.OAIListResult;
import nl.waisda.oaipmh.model.jaxb.pmh.HeaderType;
import nl.waisda.oaipmh.model.jaxb.pmh.MetadataType;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;
import nl.waisda.oaipmh.model.jaxb.pmh.RecordType;

/**
 * User: Vincent Hartsteen
 * Date: 09-10-13
 */
@Component
public class OAIListRecordsDelegate extends OAIPMHDelegateBase{
    /**
     * creates a list of records for all documents in given format and in the given date range
     * When resumptiontoken has a value, it is used to retrieve the proper page
     * @param resumptionInfo the resumption data to retrieve the next page
     * @param metadataPrefix the format to get the list in
     * @param from from date (nullable)
     * @param until until date (nullable)
     * @return the list of records
     * @throws OAIException
     */
    public OAIListResult<RecordType> getOAIListRecords(byte[] resumptionInfo, String metadataPrefix, Date from, Date until) throws OAIException {
        List<RecordType> resultList;
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
                resultList = buildRecordTypeListWaisdaAnnotation(page);
            break;
            default:
                resultList = null; // shut up compiler
            break;

        }

        // generate the new resumption token
        resumptionInfo = String.valueOf(page + 1).getBytes();

        // DRS TODO: expiration date
        return new OAIListResult<RecordType>(resumptionInfo, new DateTime(), resultList);
    }

    private List<RecordType> buildRecordTypeListWaisdaAnnotation(int page) throws OAIException {
        MetadataType metadata;
        HeaderType headerType;
        RecordType recordType;
        List<RecordType> recordTypeList = new LinkedList<RecordType>();

        // read the data (DRS TODO: same as ListIdentifiers)
        List<Video> videos = videoRepository.getVideos((page - 1) * PAGE_SIZE, PAGE_SIZE, 0);

        if (videos.size() == 0) {
            if (page > 1) {
                throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, "Reached end of list");
            } else {
                throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, "Empty set");
            }
        }

        for (Video video : videos) {
            List<TagEntry> tagEntries = tagEntryRepository.getTopTagEntries(video.getId(), 100);

            metadata = JAXB_OBJECT_FACTORY_PMH.createMetadataType();
            recordType = JAXB_OBJECT_FACTORY_PMH.createRecordType();
            headerType = createHeaderAnnotation(video);

            metadata.setAny(formatOutputWaisdaAnnotation(video, tagEntries));

            recordType.setHeader(headerType);
            recordType.setMetadata(metadata);

            recordTypeList.add(recordType);
        }
        return recordTypeList;
    }

}
