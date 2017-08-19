package nl.waisda.model.europeana;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 8-3-13
 * Time: 15:19
 * To change this template use ile | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaResponse {
    @JsonIgnore
    private String JSON;
    private Boolean success;
    private String error;
    private Integer requestNumber;
    private Integer itemsCount;
    private Integer totalResults;
    @JsonProperty("items")
    private List<EuropeanaRecord> items;
    private EuropeanaObject object;


    public String getJSON() {
        return JSON;
    }

    public void setJSON(String JSON) {
        this.JSON = JSON;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Integer requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<EuropeanaRecord> getItems() {
        return items;
    }

    public void setItems(List<EuropeanaRecord> items) {
        this.items = items;
    }

    public EuropeanaObject getObject() {
        return object;
    }

    public void setObject(EuropeanaObject object) {
        this.object = object;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("EuropeanaResponse");
        sb.append("{JSON='").append(JSON).append('\'');
        sb.append(", success=").append(success);
        sb.append(", error='").append(error).append('\'');
        sb.append(", requestNumber=").append(requestNumber);
        sb.append(", itemsCount=").append(itemsCount);
        sb.append(", totalResults=").append(totalResults);
        sb.append(", items=").append(items);
        sb.append(", object=").append(object);
        sb.append('}');
        return sb.toString();
    }
}
