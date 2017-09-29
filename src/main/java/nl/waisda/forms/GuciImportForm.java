package nl.waisda.forms;

import java.util.List;

/**
 * User: Danny
 * Date: 28-9-17
 * Time: 23:02
 */
public class GuciImportForm {
    private Boolean importRunning;
    private List<String> log;
    private String importingTitle;
    private int importingProgress;
    private int importingTotalItems;

    public Boolean getImportRunning() {
        return importRunning;
    }

    public void setImportRunning(Boolean importRunning) {
        this.importRunning = importRunning;
    }

    public List<String> getLog() {
        return log;
    }

    public void setLog(List<String> log) {
        this.log = log;
    }

    public String getImportingTitle() {
        return importingTitle;
    }

    public void setImportingTitle(String importingTitle) {
        this.importingTitle = importingTitle;
    }

    public int getImportingProgress() {
        return importingProgress;
    }

    public void setImportingProgress(int importingProgress) {
        this.importingProgress = importingProgress;
    }

    public int getImportingTotalItems() {
        return importingTotalItems;
    }

    public void setImportingTotalItems(int importingTotalItems) {
        this.importingTotalItems = importingTotalItems;
    }
}
