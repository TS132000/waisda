package nl.waisda.oaipmh.logic;

import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.*;

import nl.waisda.domain.TagEntry;
import nl.waisda.domain.Video;
import nl.waisda.oaipmh.model.MetadataPrefix;
import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.jaxb.pmh.HeaderType;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;
import nl.waisda.oaipmh.model.jaxb.pmh.ObjectFactory;
import nl.waisda.repositories.TagEntryRepository;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 9:39
 */
@Component
public class OAIPMHDelegateBase {

    private static final String NAMESPACE_IMMIX = "http://instituut.beeldengeluid.nl/namespace/iMMix";
    private static final String NAMESPACE_IMMIX_PREFIX = "iMMix";

    protected static final ObjectFactory JAXB_OBJECT_FACTORY_PMH = new ObjectFactory();

    protected static final String HEADER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    protected static final int PAGE_SIZE = 20;

    @Autowired
    TagEntryRepository tagEntryRepository;

    private String encode(String data) {
        if (data == null) {
            return "";
        }
        return StringEscapeUtils.escapeXml(data);
    }

    /**
     * formats given storable for OAI-PMH format
     * @param entry the storable
     * @return W3C Element
     * @throws OAIException OAIPMHerrorcodeType.BAD_ARGUMENT in case of IMMIX XML cannot be read,
     *                      OAIPMHerrorcodeType.BAD_ARGUMENT in case of missing docId attribute in IMMIX XML
     */
    protected Element formatOutputWaisdaTagEntry(TagEntry entry) throws OAIException {
        Assert.notNull(entry);
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        // DRS TODO: needs getRecord verb prefix on about and oac:hasTarget elements
        String baseUrl = createBaseURL() + "?verb=GetRecord&identifier=%s&metadataPrefix=rdf";
        String xml =
            "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
            "         xmlns:oac=\"http://www.openannotation.org/ns/\"\n" +
            "         xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
            "         xmlns:dcterms=\"http://purl.org/dc/terms/\"\n" +
            "         xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"\n" +
            "         xmlns:wte=\"http://www.beeldengeluid.nl/waisda/tagentry/\">\n" +
            "  <rdf:Description rdf:about=\"" + encode(String.format(baseUrl, createTagEntryId(entry))) + "\">\n" +
            "    <rdf:type rdf:resource=\"http://www.openannotation.org/ns/Annotation\"/>\n" +
            "    <dc:title>TagEntry annotation for waisda metadata</dc:title>\n" +
            "    <dcterms:creator>" + encode(entry.getOwner().getName()) + "</dcterms:creator>\n" +
            "    <dcterms:created>" + encode(sdf.format(entry.getCreationDate())) + "</dcterms:created>\n" +
            "    <oac:hasTarget rdf:resource=\"" + encode(String.format(baseUrl, createVideoId(entry.getGame().getVideo()))) + "\"/>\n" +
            "    <oac:hasBody>\n" +
            "      <wte:timestamp rdf:datatype=\"http://www.w3.org/2001/XMLSchema#time\">" + encode(entry.getTimestampHHmmss()) + "</wte:timestamp>\n" +
            "      <skos:prefLabel xml:lang=\"nl\">" + encode(entry.getTag()) + "</skos:prefLabel>\n" +
            "      <skos:hiddenLabel xml:lang=\"nl\">" + encode(entry.getNormalizedTag()) + "</skos:hiddenLabel>\n" +
            "      <skos:note xml:lang=\"nl\">User entered tag</skos:note>\n" +
            "      <skos:inScheme rdf:resource=\"http://data.beeldengeluid.nl/gtaa/" + encode(entry.getDictionary()) + "\"/>\n" +
            "    </oac:hasBody>\n" +
            "  </rdf:Description>\n" +
            "</rdf:RDF>\n";

        Element doc = readDocumentFromXmlContent(xml);

        return doc;
    }

    /**
     * validates and convert the metadata prefix
     * @param metadataPrefix the metadata type
     * @return MetadataPrefix
     * @throws OAIException
     */
    protected MetadataPrefix validateAndGetMetadataPrefix(String metadataPrefix) throws OAIException {
        MetadataPrefix metadataFormat;
        try {
            metadataFormat = MetadataPrefix.valueOf(metadataPrefix);
        } catch(IllegalArgumentException e) {
            throw new OAIException(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT,
                "Supported metadataPrefixes: " + StringUtils.join(MetadataPrefix.values(), ", "));
        }
        return metadataFormat;
    }


    /**
     * compose the base url for the host this is running on
     * @return the base url
     */
    protected String createBaseURL() {
        String baseURL = null;
        try {
            RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
            if (ra != null && ra instanceof ServletRequestAttributes) {
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                String servletPath = sra.getRequest().getServletPath();
                String pathInfo = sra.getRequest().getPathInfo();
                baseURL = sra.getRequest().getScheme() + "://"+ sra.getRequest().getServerName() + ":" + sra.getRequest().getServerPort() +
                            (servletPath != null ? "/" + servletPath : "") + (pathInfo != null ? "/" + pathInfo : "");
                baseURL = baseURL.replaceAll("(?<=[^:]{1})/{2,}+", "/");
            }
        } catch(IllegalStateException ise) {
            // RequestAttributes not available
        }
        if (baseURL == null) {
            baseURL = "http://oai.placeholder.nl:80/oai";
        }
        return baseURL;
    }

    private Element readDocumentFromXmlContent(String documentContent) throws OAIException {
        try {
            Document doc;
            DocumentBuilder documentBuilder;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setNamespaceAware(true);
            documentBuilder = factory.newDocumentBuilder();
            doc = documentBuilder.parse(IOUtils.toInputStream(documentContent, "UTF-8"));
            return doc.getDocumentElement();
        } catch (Exception e) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "Error while parsing the content. Returned message: " +
                        e.getMessage() + "\n\nOriginalContent: " + documentContent);
        }
    }

    private Element updateDocumentPath(Element doc, String actualPath) throws OAIException {
        Assert.notNull(doc, "Document cannot be null");
        NodeList docList = doc.getElementsByTagName("doc");
        if (docList != null) {
            if (docList.getLength() > 0) {
                NamedNodeMap attributeMap = docList.item(0).getAttributes();
                if (attributeMap != null) {
                    Node idAttribute = attributeMap.getNamedItem("id");
                    if (idAttribute != null) {
                        idAttribute.setNodeValue(actualPath);
                        return doc;
                    }
                }
            }
        }
        throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "The iMMix document seems to be invalid since it misses the document path element");
    }

    private Node copyToNamespace(Node node, String namespace, String prefix) {
        Element nodeWithNS;
        NodeList childNodes;
        NamedNodeMap nodeAttributes;

        Document owner = (node instanceof Document ? (Document) node : node.getOwnerDocument());

        if (node.getNodeType() == Node.TEXT_NODE) {
            Text textNode = owner.createTextNode(node.getNodeValue());
            return textNode;
        } else if (node.getNodeType() == Node.COMMENT_NODE) {
            Comment comment = owner.createComment(node.getNodeValue());
            return comment;
        }

        nodeWithNS = owner.createElementNS(namespace, node.getNodeName());
        nodeWithNS.setPrefix(prefix);
        nodeWithNS.setNodeValue(node.getNodeValue());

        // copy attributes
        nodeAttributes = node.getAttributes();
        for (int i=0; i<nodeAttributes.getLength(); i++) {
            Node attribute = nodeAttributes.item(i);
            nodeWithNS.setAttributeNS(attribute.getNamespaceURI(), attribute.getNodeName(), attribute.getNodeValue());
        }

        // process children
        childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            Node result = copyToNamespace(child, namespace, prefix);
            nodeWithNS.appendChild(result);
        }
        return nodeWithNS;

    }

    HeaderType createHeader(TagEntry tagEntry) {
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        HeaderType headerType = new HeaderType();
        headerType.setStatus(null);
        headerType.setDatestamp(null);

        headerType.setIdentifier(createTagEntryId(tagEntry));

        headerType.getSetSpecs().add("All");

        return headerType;
    }

    String createTagEntryId(TagEntry tagEntry) {
        // DRS TODO: need enum for type (tagentry, video)
        return String.format("oai:waisda.beeldengeluid.nl:tagentry:%s", tagEntry.getId());
    }

    String createVideoId(Video video) {
        return String.format("oai:waisda.beeldengeluid.nl:video:%s", video.getId());
    }
}
