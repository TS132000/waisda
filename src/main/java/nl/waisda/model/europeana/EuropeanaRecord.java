package nl.waisda.model.europeana;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 8-3-13
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EuropeanaRecord {
    private String id;
    @JsonProperty("provider")
    private List<String> providerList;
    private Integer europeanaCompleteness;
    @JsonProperty("language")
    private List<String> languageList;
    @JsonProperty("year")
    private List<String> yearList;
    @JsonProperty("rights")
    private List<String> rightsList;
    @JsonProperty("title")
    private List<String> titleList;
    @JsonProperty("dcCreator")
    private List<String> dcCreatorList;
    @JsonProperty("edmTimespanLabel")
    private List<EuropeanaSingleDef> edmTimespanLabelList;
    @JsonProperty("edmPreview")
    private List<String> edmPreviewList;
    private Integer completeness;
    @JsonProperty("europeanaCollectionName")
    private List<String> europeanaCollectionNameList;
    private Integer index;
    private String type;
    @JsonProperty("dataProvider")
    private List<String> dataProviderList;
    @JsonProperty("edmPlaceLabel")
    private List<EuropeanaSingleDef> edmPlaceLabel;
    private String link;
    private String guid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<String> providerList) {
        this.providerList = providerList;
    }

    public Integer getEuropeanaCompleteness() {
        return europeanaCompleteness;
    }

    public void setEuropeanaCompleteness(Integer europeanaCompleteness) {
        this.europeanaCompleteness = europeanaCompleteness;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    public List<String> getYearList() {
        return yearList;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public List<String> getRightsList() {
        return rightsList;
    }

    public void setRightsList(List<String> rightsList) {
        this.rightsList = rightsList;
    }

    public List<String> getTitleList() {
        return titleList;
    }

    public void setTitleList(List<String> titleList) {
        this.titleList = titleList;
    }

    public List<String> getDcCreatorList() {
        return dcCreatorList;
    }

    public void setDcCreatorList(List<String> dcCreatorList) {
        this.dcCreatorList = dcCreatorList;
    }

    public List<EuropeanaSingleDef> getEdmTimespanLabelList() {
        return edmTimespanLabelList;
    }

    public void setEdmTimespanLabelList(List<EuropeanaSingleDef> edmTimespanLabelList) {
        this.edmTimespanLabelList = edmTimespanLabelList;
    }

    public List<String> getEdmPreviewList() {
        return edmPreviewList;
    }

    public void setEdmPreviewList(List<String> edmPreviewList) {
        this.edmPreviewList = edmPreviewList;
    }

    public Integer getCompleteness() {
        return completeness;
    }

    public void setCompleteness(Integer completeness) {
        this.completeness = completeness;
    }

    public List<String> getEuropeanaCollectionNameList() {
        return europeanaCollectionNameList;
    }

    public void setEuropeanaCollectionNameList(List<String> europeanaCollectionNameList) {
        this.europeanaCollectionNameList = europeanaCollectionNameList;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getDataProviderList() {
        return dataProviderList;
    }

    public void setDataProviderList(List<String> dataProviderList) {
        this.dataProviderList = dataProviderList;
    }

    public List<EuropeanaSingleDef> getEdmPlaceLabel() {
        return edmPlaceLabel;
    }

    public void setEdmPlaceLabel(List<EuropeanaSingleDef> edmPlaceLabel) {
        this.edmPlaceLabel = edmPlaceLabel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
