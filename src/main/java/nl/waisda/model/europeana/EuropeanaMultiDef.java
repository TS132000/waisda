package nl.waisda.model.europeana;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 10-3-13
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaMultiDef {
    private List<String> def;

    public List<String> getDef() {
        return def;
    }

    public void setDef(List<String> def) {
        this.def = def;
    }
}
