package nl.waisda.oaipmh.model;

import org.springframework.util.Assert;

import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMHerrorcodeType;

/**
 * User: Danny
 * Date: 2-10-13
 * Time: 9:59
 */
public class OAIIdentifier {
    private String prefix;
    private String domain;
    private String type;
    private String identifier;

    /**
     * Creates a OAI identifier instance for given identifier string
     * throws Illegal argument exception in case of erroneous identifier
     * @param identifier the identifier
     * @throws OAIException
     */
    public static OAIIdentifier newInstance(String identifier) throws OAIException {
        return new OAIIdentifier(identifier);
    }

    /**
     * throws Illegal argument exception in case of erroneous identifier
     * @param identifier the identifier
     * @throws OAIException
     */
    protected OAIIdentifier(String identifier) throws OAIException {
        Assert.hasLength(identifier, "identifier cannot be empty");

        String[] idElements = identifier.split(":");
        validateDocumentType(identifier, idElements);

        this.prefix = idElements[0];
        this.domain = idElements[1];
        this.type = idElements[2];
        this.identifier = idElements[3];
    }

    private void validateDocumentType(String identifier, String[] idElements) throws OAIException {
        String documentType;
        if (idElements == null) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT,
                "identifier must have colons");
        }
        if (idElements.length != 4) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT,
                "identifier must consist of 4 parts ('oai:beeldengeluid.nl:documentType:documentID')");
        }
        documentType = idElements[2];

        if (!"tagentry".equals(documentType)) {

            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, String.format(
                    "Identifier '%s' contains type '%s' which is not recognized. Identifier must " +
                            "have format ww:xx:yy:zz, whery yy is either 'tagentry'.",
                    identifier, documentType));
        }
    }

    /**
     * checks if identifier supports DC format
     * @return true when supported
     * @throws OAIException
     */
    public boolean supportsDC() throws OAIException {
        if ("Selectie".equals(type) || "Expressie".equals(type)) {
            return true;
        }

        return false;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDomain() {
        return domain;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(prefix).append(':');
        sb.append(domain).append(':');
        sb.append(type).append(':');
        sb.append(identifier);

        return sb.toString();
    }
}
