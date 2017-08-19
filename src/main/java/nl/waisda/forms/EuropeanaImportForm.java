package nl.waisda.forms;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Danny
 * Date: 13-3-13
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class EuropeanaImportForm {

    private Boolean importRunning;
    private String importingTitle;
    private Integer importingProgress;
    private Integer importingTotalItems;
    private String importingQuery;

    private List<String> log;

    private Integer importSummary;
    private String importRequestString;
    private String prevImportRequestString;
    private String start;
    private String stop;

    public Boolean getImportRunning() {
        return importRunning;
    }

    public void setImportRunning(Boolean importRunning) {
        this.importRunning = importRunning;
    }

    public String getImportingTitle() {
        return importingTitle;
    }

    public void setImportingTitle(String importingTitle) {
        this.importingTitle = importingTitle;
    }

    public Integer getImportingProgress() {
        return importingProgress;
    }

    public void setImportingProgress(Integer importingProgress) {
        this.importingProgress = importingProgress;
    }

    public Integer getImportingTotalItems() {
        return importingTotalItems;
    }

    public void setImportingTotalItems(Integer importingTotalItems) {
        this.importingTotalItems = importingTotalItems;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
        // DRS: this is a cheap and filthy way of adding some color to the logs...
        if (log != null) {
            int size = log.size();
            for (int i=0; i<size; i++) {
                String line = log.get(i);
                if (line.indexOf("INFO") > -1) {
                    line = "<font style=\"color: #0F0\">" + line + "</font>";
                } else if (line.indexOf("WARN") > -1) {
                    line = "<font style=\"color: #F80\">" + line + "</font>";
                } else if (line.indexOf("FAIL") > -1) {
                    line = "<font style=\"color: #F00\">" + line + "</font>";
                }
                log.set(i, line);
            }
        }
    }

    public String getImportingQuery() {
        return importingQuery;
    }

    public void setImportingQuery(String importingQuery) {
        this.importingQuery = importingQuery;
    }

    public Integer getImportSummary() {
        return importSummary;
    }

    public void setImportSummary(Integer importSummary) {
        this.importSummary = importSummary;
    }

    public String getImportRequestString() {
        return importRequestString;
    }

    public void setImportRequestString(String importRequestString) {
        this.importRequestString = importRequestString;
    }

    public String getPrevImportRequestString() {
        return prevImportRequestString;
    }

    public void setPrevImportRequestString(String prevImportRequestString) {
        this.prevImportRequestString = prevImportRequestString;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
