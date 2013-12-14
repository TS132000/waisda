package nl.waisda.oaipmh.logic;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import nl.waisda.oaipmh.model.jaxb.pmh.DeletedRecordType;
import nl.waisda.oaipmh.model.jaxb.pmh.DescriptionType;
import nl.waisda.oaipmh.model.jaxb.pmh.GranularityType;
import nl.waisda.oaipmh.model.jaxb.pmh.IdentifyType;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 13:39
 */
@Component
public class OAIIdentifyFeaturesDelegate extends OAIPMHDelegateBase {

    private static final Logger logger = LoggerFactory.getLogger(OAIIdentifyFeaturesDelegate.class);

    public IdentifyType getIdentify() {
        String baseURL = createBaseURL();
        IdentifyType identify = new IdentifyType();

        identify.setRepositoryName("Nederlands Instituut voor Beeld en Geluid OAI-PMH repository");
        identify.setBaseURL(baseURL);
        identify.setProtocolVersion("2.0");
        identify.getAdminEmails().add("applicatiebeheer@beeldengeluid.nl");
        identify.setEarliestDatestamp(getEarliestTimestamp());
        identify.setDeletedRecord(DeletedRecordType.TRANSIENT);
        identify.setGranularity(GranularityType.YYYY_MM_DD_THH_MM_SS_Z);

        DescriptionType descriptionType = new DescriptionType();

        try {
            descriptionType.setAny(createDescriptionForOaiFormat());
            identify.getDescriptions().add(descriptionType);
        } catch (Exception e) {
            logger.info("Failed to parse description XML, description will be empty");
        }

        return identify;
    }

    private Element createDescriptionForOaiFormat() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        return docBuilder.parse(
                new InputSource(new StringReader("<oai-identifier xmlns=\"http://www.openarchives.org/OAI/2.0/oai-identifier\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai-identifier http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\"><scheme>oai</scheme><repositoryIdentifier>beeldengeluid.nl</repositoryIdentifier><delimiter>:</delimiter><sampleIdentifier>oai:beeldengeluid.nl:tagentry:12345</sampleIdentifier></oai-identifier>")))
                .getDocumentElement();
    }

    private String getEarliestTimestamp() {
        return "TODO";
    }

}
