package nl.waisda.model.europeana;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 11-3-13
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaObject extends EuropeanaRecord {

    private List<EuropeanaProxy> proxies;
    private List<EuropeanaAggregation> aggregations;

    public List<EuropeanaProxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<EuropeanaProxy> proxies) {
        this.proxies = proxies;
    }

    public List<EuropeanaAggregation> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<EuropeanaAggregation> aggregations) {
        this.aggregations = aggregations;
    }


}
