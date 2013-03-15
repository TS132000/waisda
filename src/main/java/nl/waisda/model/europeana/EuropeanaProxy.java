package nl.waisda.model.europeana;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 12-3-13
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaProxy {

    private EuropeanaMultiDef dctermsExtent;

    public EuropeanaMultiDef getDctermsExtent() {
        return dctermsExtent;
    }

    public void setDctermsExtent(EuropeanaMultiDef dctermsExtent) {
        this.dctermsExtent = dctermsExtent;
    }

}
