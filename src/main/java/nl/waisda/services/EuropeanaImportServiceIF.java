package nl.waisda.services;

import java.util.List;

import nl.waisda.exceptions.EuropeanaImportException;

/**
 * Interface for the Europeana import service
 * @author Danny Sedney (sdengineering77@gmail.com)
 */
public interface EuropeanaImportServiceIF {

    /**
     * Runs the import process in separate Thread, detached from the main thread
     * @param query
     * @throws EuropeanaImportException
     */
    void runDetached(final String query) throws EuropeanaImportException;

    /**
     * Import all video metadata from Europeana as retrieved by given query.
     * NOTE: Only one import can run at a time
     * @param query
     * @throws EuropeanaImportException
     */
    void importEuropeanaData(final String query) throws EuropeanaImportException;

    /**
     * Returns the number of items found with given query
     * @param query
     * @return int
     * @throws EuropeanaImportException
     */
    int requestSummary(final String query) throws EuropeanaImportException;

    /**
     * Stop any current running import process. NOTE: it can take some time for
     * the service to actually stop
     */
    void requestStop();

    /**
     * The importlog, is cleared before a new import request
     * @return
     */
    List<String> getImportLog();

    /**
     * Returns true when process is running
     * @return
     */
    boolean isRunning();

    /**
     * Get the currently processed item number
     * @return
     */
    int getImportingProgress();

    /**
     * Get the number of total items being processed
     * @return
     */
    int getImportingQuantity();

    /**
     * Get the currently processed title
     * @return
     */
    String getImportingTitle();

    /**
     * Get the currently running query
     * @return
     */
    String getRunningQuery();
}
