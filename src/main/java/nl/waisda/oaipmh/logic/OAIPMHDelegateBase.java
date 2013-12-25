package nl.waisda.oaipmh.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import nl.waisda.domain.User;
import nl.waisda.domain.Video;
import nl.waisda.oaipmh.model.MetadataPrefix;
import nl.waisda.oaipmh.model.OAIException;
import nl.waisda.oaipmh.model.jaxb.pmh.HeaderType;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;
import nl.waisda.oaipmh.model.jaxb.pmh.ObjectFactory;
import nl.waisda.repositories.TagEntryRepository;
import nl.waisda.repositories.VideoRepository;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 9:39
 */
@Component
public class OAIPMHDelegateBase {

    protected static final ObjectFactory JAXB_OBJECT_FACTORY_PMH = new ObjectFactory();

    protected static final String HEADER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    protected static final int PAGE_SIZE = 20;

    @Autowired
    TagEntryRepository tagEntryRepository;

    @Autowired
    VideoRepository videoRepository;

    private String encode(String data) {
        if (data == null) {
            return "";
        }
        return StringEscapeUtils.escapeXml(data);
    }

    protected Element formatOutputWaisdaVideo(Video video) throws OAIException {
        Assert.notNull(video, "video cannot be null");

        SimpleDateFormat headerFormat = new SimpleDateFormat(HEADER_DATE_FORMAT);
        SimpleDateFormat durationFormat = new SimpleDateFormat("'PT'HH'H'mm'M'ss'S'");
        durationFormat.setTimeZone(TimeZone.getTimeZone("+0"));
        // DRS TODO: needs getRecord verb prefix on about and oac:hasTarget elements
        String base = createBaseURL();
        String baseUrl = base + "?verb=GetRecord&identifier=%s&metadataPrefix=rdf";
        String xml =
            "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n" +
            "         xmlns:ore=\"http://www.openarchives.org/ore/terms/\" \n" +
            "         xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\" \n" +
            "         xmlns:dcterms=\"http://purl.org/dc/terms/\" \n" +
            "         xmlns:dc=\"http://purl.org/dc/elements/1.1/\" \n" +
            "         xmlns:edm=\"http://www.europeana.eu/schemas/edm/\">\n" +
            "  <edm:ProvidedCHO rdf:about=\"" + encode(createVideoId(video)) + "\">\n" +
            "    <dc:title xml:lang=\"nl\">" + encode(video.getTitle())+ "</dc:title>\n" +
            //"    <dc:subject xml:lang=\"en\">Steger, E.A.M.A.</dc:subject>\n" + // DRS TODO: import
            "    <dc:language>nl</dc:language>\n" +
            "    <dc:type>Moving Image</dc:type>\n" +
            //"    <dc:publisher xml:lang=\"nl\">Nederlands Instituut voor Beeld en Geluid</dc:publisher>\n" +
            "    <dc:identifier>" + encode(createVideoId(video)) + "</dc:identifier>\n" +
            "    <dcterms:extent>" + encode(durationFormat.format(new Date(video.getDuration()))) + "</dcterms:extent>\n" +
            "    <dcterms:medium>film</dcterms:medium>\n" +
            "    <edm:type>VIDEO</edm:type>\n" +
            "  </edm:ProvidedCHO>\n" +
            "  <ore:Aggregation rdf:about=\"" + encode(String.format(baseUrl, createVideoId(video))) + "\">\n" +
            "    <edm:aggregatedCHO rdf:resource=\"" + encode(createAnnotationId(video)) + "\"/>\n" +
            //"    <edm:dataProvider>Waisda</edm:dataProvider>\n" +
            //"    <edm:isShownAt>http://www.openbeelden.nl/media/165084</edm:isShownAt>\n" +
            "    <edm:isShownBy>" + encode(video.getSourceUrl()) + "</edm:isShownBy>\n" +
            "    <edm:object>" + encode(video.getImageUrl()) + "</edm:object>\n" +
            "    <edm:provider>Waisda</edm:provider>\n" +
            //"    <edm:rights>http://creativecommons.org/licenses/by-sa/3.0/nl/</edm:rights>\n" +
            "  </ore:Aggregation>\n" +
            "</rdf:RDF>\n";

        Element doc = readDocumentFromXmlContent(xml);

        return doc;

    }

    protected Element formatOutputWaisdaAnnotation(Video video, List<TagEntry> entries) throws OAIException {
        Assert.notNull(video, "video cannot be null");
        Assert.notNull(entries, "entries cannot be null");
        Element doc;
        //        NodeList list;
        //        Node bag;
        //        Document ownerDocument;
        String xml;

        String base = createBaseURL();
        String baseUrl = base + "?verb=GetRecord&identifier=%s&metadataPrefix=rdf";
        StringBuilder bag = new StringBuilder();
        for (TagEntry entry : entries) {
            bag.append("<w:TagEntry rdf:resource=\"" + encode(String.format(baseUrl, createTagEntryId(entry))) + "\"/>\n");
        }


        xml =
            "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
            "         xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
            "         xmlns:dcterms=\"http://purl.org/dc/terms/\"\n" +
            "         xmlns:oac=\"http://www.openannotation.org/ns/\" \n" +
            "         xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"\n" +
            "         xmlns:w=\"http://www.beeldengeluid.nl/waisda/\">\n" +
            "  <oac:Annotation rdf:about=\"" + encode(createAnnotationId(video)) + "\">\n" +
            "    <dc:title>TagEntry annotation for waisda metadata</dc:title>\n" +
            "    <oac:hasTarget rdf:resource=\"" + encode(String.format(baseUrl, createVideoId(video))) + "\"/>\n" +
            "    <oac:hasBody>\n" +
            "      <w:TagEntries rdf:about=\"" + encode(createVideoId(video)) + "\">\n" +
            //"        <rdf:Bag>\n" +
            bag.toString() +
            //"        </rdf:Bag>\n" +
            "      </w:TagEntries>\n" +
            "    </oac:hasBody>\n" +
            "  </oac:Annotation>\n" +
            "</rdf:RDF>\n";
        doc = readDocumentFromXmlContent(xml);

        //        list = doc.getElementsByTagName("rdf:Bag");
        //        bag = list.item(0);
        //        ownerDocument = bag.getOwnerDocument();
        //        for (TagEntry entry : entries) {
        //            Node entryNode = ownerDocument.importNode(formatOutputWaisdaTagEntry(entry), true);
        //            bag.appendChild(entryNode);
        //        }

        return doc;

    }

    public static void main(String[] args) {
        List<TagEntry> entries = new ArrayList<TagEntry>();
        Video video = new Video();
        video.setId(1001);
        TagEntry entry = new TagEntry();
        entry.setTag("mijn tag");
        entry.setGameTime(12305);
        entry.setOwner(new User());
        entry.setCreationDate(new Date());
        entries.add(entry);
        OAIPMHDelegateBase base = new OAIPMHDelegateBase();
        try {
            Element element = base.formatOutputWaisdaAnnotation(video, entries);
            System.out.println(element);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    /**
     * formats given tagentry for OAI-PMH format
     * @param entry the storable
     * @return W3C Element
     * @throws OAIException OAIPMHerrorcodeType.BAD_ARGUMENT in case of IMMIX XML cannot be read,
     *                      OAIPMHerrorcodeType.BAD_ARGUMENT in case of missing docId attribute in IMMIX XML
     */
    protected Element formatOutputWaisdaTagEntry(TagEntry entry) throws OAIException {
        Assert.notNull(entry);

        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        // DRS TODO: needs getRecord verb prefix on about and oac:hasTarget elements
        //String base = createBaseURL();
        //String baseUrl = base + "?verb=GetRecord&identifier=%s&metadataPrefix=rdf";
        String xml =
            "<w:TagEntry xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
            "            xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
            "            xmlns:dcterms=\"http://purl.org/dc/terms/\"\n" +
            "            xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"\n" +
            "            xmlns:w=\"http://www.beeldengeluid.nl/waisda/\"\n" +
            "            rdf:about=\"" + encode(createTagEntryId(entry)) + "\">\n" +
            "  <w:timestamp rdf:datatype=\"http://www.w3.org/2001/XMLSchema#time\">" + encode(entry.getTimestampHHmmss()) + "</w:timestamp>\n" +
            "  <w:tag rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\" xml:lang=\"nl\">" + encode(entry.getTag()) + "</w:tag>\n" +
            "  <dc:title>TagEntry for waisda metadata</dc:title>\n" +
            "  <dcterms:creator>" + encode(entry.getOwner().getName()) + "</dcterms:creator>\n" +
            "  <dcterms:created>" + encode(sdf.format(entry.getCreationDate())) + "</dcterms:created>\n" +
            "  <dcterms:spatial>\n" +
            "    <skos:Concept rdf:about=\"" + encode(createTagEntryId(entry)) + "\">\n" +
            "      <skos:prefLabel xml:lang=\"nl\">" + encode(entry.getTag()) + "</skos:prefLabel>\n" +
            "      <skos:note xml:lang=\"nl\">User entered tag</skos:note>\n" +
            "      <skos:inScheme rdf:resource=\"http://localhost:8080/oai/gtaa/" + encode(entry.getDictionary()) + "\"/>\n" +
            "    </skos:Concept>\n" +
            "  </dcterms:spatial>\n" +
            "</w:TagEntry>\n";


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

    HeaderType createHeaderTagEntry(TagEntry tagEntry) {
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        HeaderType headerType = new HeaderType();
        headerType.setStatus(null);
        headerType.setDatestamp(null);

        headerType.setIdentifier(createTagEntryId(tagEntry));

        headerType.getSetSpecs().add("All");

        return headerType;
    }

    HeaderType createHeaderVideo(Video video) { // DRS TODO: needs latest game for date value
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        HeaderType headerType = new HeaderType();
        headerType.setStatus(null);
        headerType.setDatestamp(null);

        headerType.setIdentifier(createVideoId(video));

        headerType.getSetSpecs().add("All");

        return headerType;
    }

    HeaderType createHeaderAnnotation(Video video) { // DRS TODO: needs latest game for date value
        SimpleDateFormat sdf = new SimpleDateFormat(HEADER_DATE_FORMAT);
        HeaderType headerType = new HeaderType();
        headerType.setStatus(null);
        headerType.setDatestamp(null);

        headerType.setIdentifier(createAnnotationId(video));

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

    String createAnnotationId(Video video) {
        return String.format("oai:waisda.beeldengeluid.nl:annotation:%s", video.getId());
    }

}
