package nl.waisda.model.europeana;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 11-3-13
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaAggregation {
    private String edmIsShownBy;
    private List<EuropeanaAbout> webResources;

    public String getEdmIsShownBy() {
        return edmIsShownBy;
    }

    public void setEdmIsShownBy(String edmIsShownBy) {
        this.edmIsShownBy = edmIsShownBy;
    }

    public List<EuropeanaAbout> getWebResources() {
        return webResources;
    }

    public void setWebResources(List<EuropeanaAbout> webResources) {
        this.webResources = webResources;
    }
}
