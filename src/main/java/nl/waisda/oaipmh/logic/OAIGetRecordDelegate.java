package nl.waisda.oaipmh.logic;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import nl.waisda.domain.TagEntry;
import nl.waisda.oaipmh.model.MetadataPrefix;
import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.OAIIdentifier;
import nl.waisda.oaipmh.model.jaxb.pmh.HeaderType;
import nl.waisda.oaipmh.model.jaxb.pmh.MetadataType;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;
import nl.waisda.oaipmh.model.jaxb.pmh.RecordType;

/**
 * User: Danny, Bogdan
 * Date: 2-10-13
 * Time: 11:08
 */
@Component
public class OAIGetRecordDelegate extends OAIPMHDelegateBase {
    /**
     * creates a OAI Record for document with given identifier and given format
     * @param identifier the document's OAI identifier string (ww:xx:yy:zz)
     * @param metadataPrefix the format it should be output to (oai_dc, iMMix)
     * @return OAI RecordType instance
     * @throws OAIException
     */
    public RecordType getOAIRecord(String identifier, String metadataPrefix) throws OAIException {
        Assert.hasLength(identifier, "identifier cannot be empty");
        Assert.hasLength(metadataPrefix, "metadataPrefix cannot be empty");

        Object output;
        HeaderType headerType;

        MetadataPrefix metadataFormat = validateAndGetMetadataPrefix(metadataPrefix);

        RecordType recordType = JAXB_OBJECT_FACTORY_PMH.createRecordType();
        MetadataType metadataType = JAXB_OBJECT_FACTORY_PMH.createMetadataType();

        OAIIdentifier oaiIdentifier = OAIIdentifier.newInstance(identifier);

        // fill metadata DRS TODO: we need to switch on identifier, not prefix as only rdf is supported
        switch(metadataFormat) {
        case rdf:
            TagEntry tagEntry = obtainTagEntry(oaiIdentifier);
            output = formatOutputWaisdaTagEntry(tagEntry);
            headerType = createHeader(tagEntry);
            break;
        default:
            // was validated indirectly by MetadataPrefix so this code is unreachable
            output = null;
            headerType = null;
            break;
        }
        metadataType.setAny(output);

        recordType.setHeader(headerType);
        recordType.setMetadata(metadataType);

        return recordType;
    }

    private TagEntry obtainTagEntry(OAIIdentifier identifier) throws OAIException {
        TagEntry tagEntry;

        try {
            if ("tagentry".equals(identifier.getType().toLowerCase())) {
                return tagEntryRepository.getById(Integer.parseInt(identifier.getIdentifier()));
            }
        } catch(Exception e) {
            throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "Cannot find the object with id '" +
                identifier.getIdentifier() + "' of type '" + identifier.getType() + "'");
        }
        throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "type '" + identifier.getType() + "' does not exist");
    }

}
