package nl.waisda.oaipmh.logic;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import nl.waisda.domain.TagEntry;
import nl.waisda.domain.Video;
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
        switch(oaiIdentifier.getType()) {
        case tagentry: {
            TagEntry tagEntry = obtainTagEntry(oaiIdentifier);
            output = formatOutputWaisdaTagEntry(tagEntry);
            headerType = createHeaderTagEntry(tagEntry);
            break;
        }
        case annotation: {
            Video video = obtainVideo(oaiIdentifier); // either a video or an exception
            List<TagEntry> tagEntries = tagEntryRepository.getTopTagEntries(video.getId(), 100);
            output = formatOutputWaisdaAnnotation(video, tagEntries);
            headerType = createHeaderAnnotation(video);
            break;
        }
        case video: {
            Video video = obtainVideo(oaiIdentifier); // either a video or an exception
            output = formatOutputWaisdaVideo(video);
            headerType = createHeaderVideo(video);
            break;
        }
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

    private <T> T obtainWithinTryCatch(OAIIdentifier identifier, Callable<T> callable) throws OAIException {
        try {
            T obj = callable.call();
            if (obj != null) {
                return callable.call();
            }
        } catch(Exception e) {
            throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "Cannot find the object with id '" +
                identifier.getIdentifier() + "' of type '" + identifier.getType() + "'");
        }
        throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "type '" + identifier.getType() + "' does not exist");
    }

    private TagEntry obtainTagEntry(OAIIdentifier identifier) throws OAIException {
        final OAIIdentifier identifierConst = identifier;
        return obtainWithinTryCatch(identifier, new Callable<TagEntry>() {
            @Override
            public TagEntry call() throws Exception {
                return tagEntryRepository.getById(Integer.parseInt(identifierConst.getIdentifier()));
            }
        });
    }

    private Video obtainVideo(OAIIdentifier identifier) throws OAIException {
        final OAIIdentifier identifierConst = identifier;
        return obtainWithinTryCatch(identifier, new Callable<Video>() {
            @Override
            public Video call() throws Exception {
                return videoRepository.getById(Integer.parseInt(identifierConst.getIdentifier()));
            }
        });
    }


}
