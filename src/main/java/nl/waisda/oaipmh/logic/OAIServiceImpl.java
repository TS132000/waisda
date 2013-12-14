package nl.waisda.oaipmh.logic;

import java.io.*;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.OAIListRequestInfo;
import nl.waisda.oaipmh.model.OAIListResult;
import nl.waisda.oaipmh.model.jaxb.pmh.*;

/**
 * User: Danny
 * Date: 10-9-13
 * Time: 13:06
 */
@Service
public class OAIServiceImpl implements OAIService {

    private static final Logger logger = LoggerFactory.getLogger(OAIServiceImpl.class);

    public static final ObjectFactory JAXB_OBJECT_FACTORY_PMH =
        new ObjectFactory();

    private static final String HEADER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    private OAIGetRecordDelegate getRecordDelegate;

    @Autowired
    private OAIListIdentifiersDelegate listIdentifiersDelegate;

    @Autowired
    private OAIListRecordsDelegate listRecordsDelegate;

    @Autowired
    private OAIIdentifyFeaturesDelegate identifyFeaturesDelegate;

    @Autowired
    private OAIRequestTypeDelegate requestTypeDelegate;

    @Override
    public OAIPMH createIdentify() {
        IdentifyType identify = identifyFeaturesDelegate.getIdentify();
        OAIPMH oaiPmh= JAXB_OBJECT_FACTORY_PMH.createOAIPMH();

        RequestType requestType = requestTypeDelegate.getRequestTypeBase(VerbType.IDENTIFY, null, null);

        oaiPmh.setResponseDate(null);
        oaiPmh.setRequest(requestType);
        oaiPmh.setIdentify(identify);

        return oaiPmh;

    }

    @Override
    public OAIPMH createListMetadataFormats(String identifier) {

        OAIPMH oaiPmh = JAXB_OBJECT_FACTORY_PMH.createOAIPMH();
        oaiPmh.setResponseDate(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));

        RequestType requestType = requestTypeDelegate.getRequestTypeBase(VerbType.LIST_METADATA_FORMATS, identifier, null);

        oaiPmh.setRequest(requestType);

        ListMetadataFormatsType metaDataFormats = new ListMetadataFormatsType();

        MetadataFormatType metadataFormatImmix = new MetadataFormatType();
        metadataFormatImmix.setMetadataPrefix("rdf");
        metadataFormatImmix.setSchema(null);
        metadataFormatImmix.setMetadataNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns");

        metaDataFormats.getMetadataFormats().add(metadataFormatImmix);

        oaiPmh.setListMetadataFormats(metaDataFormats);

        return oaiPmh;
    }

    @Override
    public OAIPMH createListSets() {
        OAIPMH oaiPmh = JAXB_OBJECT_FACTORY_PMH.createOAIPMH();
        oaiPmh.setResponseDate(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));

        RequestType requestType = requestTypeDelegate.getRequestTypeBase(VerbType.LIST_SETS, null, null);
        oaiPmh.setRequest(requestType);

        OAIPMHerrorType errorType = JAXB_OBJECT_FACTORY_PMH.createOAIPMHerrorType();
        errorType.setCode(OAIPMHerrorcodeType.NO_SET_HIERARCHY);
        errorType.setValue("This repository does not support sets");
        oaiPmh.getErrors().add(errorType);

        return oaiPmh;
    }

    @Override
    public OAIPMH createListIdentifiers(String from, String until, String set, String resumptionToken, String metadataPrefix) {
        OAIListResult<HeaderType> oaiListResult;
        OAIListRequestInfo listRequestInfo;

        OAIPMH oaiPmh = JAXB_OBJECT_FACTORY_PMH.createOAIPMH();
        ListIdentifiersType listIdentifiers = JAXB_OBJECT_FACTORY_PMH.createListIdentifiersType();

        try {
            oaiPmh.setResponseDate(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));
            oaiPmh.setRequest(createRequest(VerbType.LIST_IDENTIFIERS, from, until, set, resumptionToken, metadataPrefix));

            // validate and parse input dates
            Date fromDate = parseUTCDate(from);
            Date untilDate = parseUTCDate(until);
            validateFromAndUntilDates(fromDate, untilDate);

            // validate the exclusives
            validateOneOfAvailable(metadataPrefix, resumptionToken);

            listRequestInfo = createListRequestInfo(metadataPrefix, resumptionToken);

            // perform the actual fetch
            oaiListResult = listIdentifiersDelegate.getOAIListIdentifiers(listRequestInfo.getResumptionInfo(), listRequestInfo.getMetadataPrefix(), fromDate, untilDate);
            listIdentifiers.getHeaders().addAll(oaiListResult.getEntries());

            listIdentifiers.setResumptionToken(createResumptionTokenType(oaiListResult, listRequestInfo.getMetadataPrefix()));

            oaiPmh.setListIdentifiers(listIdentifiers);
        } catch(OAIException e) {
            addOAIExceptionToOAIPMH(e, oaiPmh);
        } catch(Throwable e) { // jdk7, dont want extra lines...  Error | Exception
            addExceptionToOAIPMH(e, oaiPmh);
        }

        return oaiPmh;
    }

    @Override
    public OAIPMH createListRecords(String from, String until, String set, String resumptionToken, String metadataPrefix) {
        OAIListResult<RecordType> oaiListResult;
        OAIListRequestInfo listRequestInfo;

        OAIPMH oaiPmh = JAXB_OBJECT_FACTORY_PMH.createOAIPMH();
        ListRecordsType listRecords = JAXB_OBJECT_FACTORY_PMH.createListRecordsType();

        try {
            // populate oai phm type
            oaiPmh.setResponseDate(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));
            oaiPmh.setRequest(createRequest(VerbType.LIST_RECORDS,from,  until, set, resumptionToken, metadataPrefix));

            // validate and parse input dates
            final Date fromDate = parseUTCDate(from);
            final Date untilDate = parseUTCDate(until);
            validateFromAndUntilDates(fromDate, untilDate);

            // validate the exclusives
            validateOneOfAvailable(metadataPrefix, resumptionToken);

            listRequestInfo = createListRequestInfo(metadataPrefix, resumptionToken);

            // perform the actual fetch
            oaiListResult = listRecordsDelegate.getOAIListRecords(listRequestInfo.getResumptionInfo(), listRequestInfo.getMetadataPrefix(), fromDate, untilDate);
            listRecords.getRecords().addAll(oaiListResult.getEntries());

            listRecords.setResumptionToken(createResumptionTokenType(oaiListResult, listRequestInfo.getMetadataPrefix()));

            oaiPmh.setListRecords(listRecords);

        } catch(OAIException e) {
            addOAIExceptionToOAIPMH(e, oaiPmh);
        } catch(Throwable /*Error | Exception*/ e) {
            addExceptionToOAIPMH(e, oaiPmh);
        }

        return oaiPmh;
    }

    @Override
    public OAIPMH createGetRecordType(String identifier, String metadataPrefix) {
        OAIPMH oaiPmh = JAXB_OBJECT_FACTORY_PMH.createOAIPMH();

        // populate request type
        RequestType requestType = requestTypeDelegate.getRequestTypeBase(VerbType.GET_RECORD, identifier, metadataPrefix);

        // populate oai phm type
        oaiPmh.setResponseDate(new XMLGregorianCalendarImpl(DateTime.now().toGregorianCalendar()));
        oaiPmh.setRequest(requestType);

        try {
            GetRecordType getRecordType = JAXB_OBJECT_FACTORY_PMH.createGetRecordType();
            // populate the getRecord type stuff from database
            getRecordType.setRecord(getRecordDelegate.getOAIRecord(identifier, metadataPrefix));
            oaiPmh.setGetRecord(getRecordType);

        } catch(OAIException e) {
            addOAIExceptionToOAIPMH(e, oaiPmh);
        } catch(Throwable/* Error | Exception */ e) {
            addExceptionToOAIPMH(e, oaiPmh);
        }

        return oaiPmh;
    }

    private void addOAIExceptionToOAIPMH(OAIException e, OAIPMH oaiPmh) {
        OAIPMHerrorType errorType = JAXB_OBJECT_FACTORY_PMH.createOAIPMHerrorType();
        errorType.setCode(e.getErrorType());
        errorType.setValue(e.getMessage());
        oaiPmh.getErrors().add(errorType);
    }

    private void addExceptionToOAIPMH(Throwable e, OAIPMH oaiPmh) {
        OAIPMHerrorType errorType = JAXB_OBJECT_FACTORY_PMH.createOAIPMHerrorType();
        String message = "An internal error occurred, please check logs. Internal message: " + e.getMessage();
        errorType.setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        errorType.setValue(message);
        oaiPmh.getErrors().add(errorType);
        logger.error(message, e);
    }

    private RequestType createRequest(VerbType verbType, String from, String until, String set, String resumptionToken, String metadataPrefix) {
        RequestType request = requestTypeDelegate.getRequestTypeBase(verbType, null, metadataPrefix);
        request = requestTypeDelegate.enrichRequestType(request, from, until, set, resumptionToken);
        return request;
    }

    private void validateFromAndUntilDates(Date fromDate, Date untilDate) throws OAIException {
        if (fromDate != null && untilDate != null && untilDate.getTime() <= fromDate.getTime()) {
            SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "Until date must be greater than from date (" + sdf.format(fromDate) + " < " + sdf.format(untilDate) + ")");
        }
    }

    /**
     * Parse a date-string into a Date object.
     * @param date String representing a date. Can be empty.
     * @return A date that represents the date-string or null if the date-string was empty.
     * @throws OAIException in case the date-string could not be interpreted as a date.
     */
    private Date parseUTCDate(String date) throws OAIException {
        // If the date is empty then just return a null.
        if (StringUtils.isBlank(date)) {
            return null;
        }

        // The date contains data. Try to format it into a Date object.
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // Consider time provided as String as if it was in UTC/GMT.
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "Date '" + date + "' is not in UTC format (YYYY-MM-DDThh:mm:ssZ)");
        }
    }
    
    private void validateOneOfAvailable(String metadataPrefix, String resumptionToken) throws OAIException {
        if (StringUtils.isNotBlank(metadataPrefix) && StringUtils.isBlank(resumptionToken)) {
            return;
        }
        if (StringUtils.isNotBlank(resumptionToken) && StringUtils.isBlank(metadataPrefix)) {
            return;
        }
        throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "Either one of metadataFormat or resumptionToken must be supplied");
    }

    private OAIListRequestInfo createListRequestInfo(String metadataPrefix, String resumptionInfo) throws OAIException {
        if (StringUtils.isNotBlank(resumptionInfo)) {
            // breakdown the resumptionToken to metadataPrefix and local resumptionToken
            try {
                byte[] resumptionInfoBytes = Base64.decodeBase64(resumptionInfo);
                ByteArrayInputStream bais = new ByteArrayInputStream(resumptionInfoBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);

                byte[] localToken = (byte[]) ois.readObject();
                String localPrefix = (String) ois.readObject();

                return new OAIListRequestInfo(localPrefix, localToken);

            } catch(Throwable /*ClassNotFoundException | IOException*/ e) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, "Resumption token not valid. Reported message: " + e.getMessage());
            }
        } else {
            return new OAIListRequestInfo(metadataPrefix, null);
        }
    }

    private ResumptionTokenType createResumptionTokenType(OAIListResult oaiListResult, String metadataPrefix) {
        ResumptionTokenType resumptionTokenType = JAXB_OBJECT_FACTORY_PMH.createResumptionTokenType();
        resumptionTokenType.setValue(createResumptionTokenString(oaiListResult.getResumptionInfo(), metadataPrefix));
        resumptionTokenType.setExpirationDate(new XMLGregorianCalendarImpl(oaiListResult.getExpirationDate().toGregorianCalendar()));

        return resumptionTokenType;
    }


    private String createResumptionTokenString(byte[] resumptionInfo, String localPrefix) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(resumptionInfo);
            oos.writeObject(localPrefix);

            return URLEncoder.encode(Base64.encodeBase64String(baos.toByteArray()), "UTF8");
        } catch(IOException e) {
            throw new IllegalArgumentException("Cannot construct resumptionToken. Reported message: " + e.getMessage());
        }
    }
    
}
