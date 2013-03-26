package nl.waisda.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import nl.waisda.domain.PlayerType;
import nl.waisda.domain.Video;
import nl.waisda.exceptions.EuropeanaImportException;
import nl.waisda.model.europeana.*;
import nl.waisda.repositories.VideoRepository;

/**
 * Implementation for the Europeana import service
 * @author Danny Sedney (sdengineering77@gmail.com)
 */
@Service
public class EuropeanaImportService implements EuropeanaImportServiceIF, InitializingBean {

    private static final Logger LOG = Logger.getLogger(EuropeanaImportService.class);
    private static final Integer TWO_HOURS = 2 * 60 * 60 * 1000;
    private static final String XML_DURATION_PATTERN_STRING_HR = "(\\d*)H";
    private static final String XML_DURATION_PATTERN_STRING_MN = "(\\d*)M";
    private static final String XML_DURATION_PATTERN_STRING_SE = "(\\d*)S";
    private static final Pattern XML_DURATION_PATTERN_HR = Pattern.compile(XML_DURATION_PATTERN_STRING_HR);
    private static final Pattern XML_DURATION_PATTERN_MN = Pattern.compile(XML_DURATION_PATTERN_STRING_MN);
    private static final Pattern XML_DURATION_PATTERN_SE = Pattern.compile(XML_DURATION_PATTERN_STRING_SE);

    private static final String MESSAGE_NO_RESPONSE = "No response received";
    private static final String MESSAGE_NO_DATA = "No data received";

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TransactionServiceIF transactionService;

    @Autowired
    private TaskExecutor europeanaImportThreadPool;

    @Value("${waisda.import.europeana.baseurl}")
    private String europeanaBaseURL;

    @Value("${waisda.import.europeana.apikey}")
    private String europeanaAPIKey;

    @Value("${waisda.import.europeana.privkey}")
    private String europeanaPrivKey;

    @Value("${waisda.import.europeana.rowsperquery}")
    private int rowsPerQuery;

    @Value("${waisda.import.europeana.profile}")
    private String profile;

    @Value("${waisda.import.europeana.validvideourls}")
    private String validVideoUrls;
    private List<Pattern> validVideoUrlList = new LinkedList<Pattern>();

    private boolean isRunning = false;
    private boolean stopRequested = false;
    private String  runningQuery = null;
    private String  importingTitle = null;
    private int     importingQuantity = 0;
    private int     importingProgress = 0;
    private List<String> importLog = new LinkedList<String>();

    /**
     * Stop the process when the JVM tries to destroy this class
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        requestStop();
    }

    /**
     * Spring bean initialization
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        String[] validVideoUrlsSplit = this.validVideoUrls.split(",");
        for (String validUrlExpression : validVideoUrlsSplit) {
            validVideoUrlList.add(Pattern.compile(validUrlExpression.trim()));
        }
    }

    private void logWarn(final String text) {
        LOG.warn(text);
        importLog.add("WARN: " + text);
    }

    private void logError(final String text, Throwable t) {
        if (t != null) {
            LOG.error(text, t);
        } else {
            LOG.error(text);
        }
        importLog.add("FAIL: " + text);
    }

    private void logInfo(final String text) {
        LOG.info(text);
        importLog.add("INFO: " + text);
    }

    /**
     * stop current running process
     */
    public void requestStop() {
        stopRequested = true;
        logInfo("Stop command issued");
    }


    public boolean isRunning() {
        return isRunning;
    }

    public int requestSummary(final String query) throws EuropeanaImportException {
        StatusLine status = null;
        HttpResponse response = null;
        EuropeanaResponse data = null;

        synchronized(this) {
            if (isRunning) {
                throw new EuropeanaImportException("Import process is already running");
            }
        }

        response = queryServer(query, 0, 0); // get zero results, we're just interested in the header
        if (response == null) {
            logError(MESSAGE_NO_RESPONSE, null);
            throw new RuntimeException(MESSAGE_NO_RESPONSE);
        }
        data = extractEuropeanaData(response.getEntity());
        if (data == null) {
            logError(MESSAGE_NO_DATA, null);
            throw new RuntimeException(MESSAGE_NO_DATA);
        }
        status = response.getStatusLine();
        if (status != null && status.getStatusCode() == 200) {
            return data.getTotalResults();
        } else if (StringUtils.isNotEmpty(data.getError()))    {
            throw new EuropeanaImportException(data.getError());
        }
        throw new RuntimeException("An error occurred during Europeana request. Returned JSON: " + data.getJSON());
    }

    public void runDetached(final String query) throws EuropeanaImportException {
        final EuropeanaImportService self = this;

        synchronized(this) {
            if (isRunning) {
                throw new EuropeanaImportException("Import process is already running");
            }
        }

        europeanaImportThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    self.importEuropeanaData(query);
                } catch(Throwable t) {
                    LOG.error("Error while importing data in detached process. Message: " + t.getMessage(), t);
                }
            }
        });

        // wait until it runs
        while(!isRunning) {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {

            }
        }
    }

    public void importEuropeanaData(final String query) throws EuropeanaImportException {
        StatusLine status = null;
        HttpResponse response = null;
        EuropeanaResponse data = null;
        int start = 0;
        int end = Integer.MAX_VALUE;

        logInfo("===========================================================");
        logInfo("Request import for query '" + query + "'");
        logInfo("===========================================================");
        synchronized(this) {
            if (isRunning) {
                logError("Import process already running, please start query '" + query + "' later", null);
                throw new EuropeanaImportException("Import process is already running");
            }
            isRunning = true;
            importingTitle = null;
            importingQuantity = 0;
            importingProgress = 0;
            runningQuery = new String(query);
            importLog.clear();
        }

        try {
            while (start < end && !stopRequested) {
                response = queryServer(query, start, null);
                if (response == null) {
                    logError(MESSAGE_NO_RESPONSE, null);
                    throw new RuntimeException(MESSAGE_NO_RESPONSE);
                }
                data = extractEuropeanaData(response.getEntity());
                if (data == null) {
                    logError(MESSAGE_NO_DATA, null);
                    throw new RuntimeException(MESSAGE_NO_DATA);
                }
                status = response.getStatusLine();

                // exported values
                importingProgress = start + 1;
                logInfo(String.format("> About to import items %d - %d", importingProgress, importingProgress + rowsPerQuery));

                // process control
                start += rowsPerQuery;
                if (data.getTotalResults() !=null) {
                    end = data.getTotalResults();
                } else {
                    end = 0; // stop
                }

                // exported values
                importingQuantity = end;

                if (status.getStatusCode() == 200) {
                    // import the data
                    try {
                        final List<EuropeanaRecord> itemList = data.getItems();
                        transactionService.runInNewTransaction(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                storeVideoData(itemList);
                                return null;
                            }
                        });
                    } catch(Throwable t) {
                        // we still try the next batch but log this error
                        logError(String.format("Error while processing videos. None of items %d - %d have been imported! Error message: %s", importingProgress, importingProgress + rowsPerQuery, t.getMessage()), t);
                    }
                } else {
                    // stop process
                    String message = String.format("Error while querying server. Server returned status code: %d, JSON: %s", status.getStatusCode(), data.getJSON());
                    logError(message, null);
                    throw new RuntimeException(message);
                }
            }
        } finally {
            if (stopRequested) {
                logError("Import for query '" + query + "' was stopped!", null);
            } else {
                logInfo("Import for query '" + query + "' finished");
            }
            isRunning = false;
            stopRequested = false;
            runningQuery = null;
            importingTitle = null;
        }
    }

    private void storeVideoData(List<EuropeanaRecord> recordList) {
        Assert.notNull(recordList);
        for (EuropeanaRecord record : recordList) {
            if (this.stopRequested) {
                break;
            }
            if ("VIDEO".equals(record.getType())) { // import video only...
                importingTitle = new String(getFirst(record.getTitleList()));
                EuropeanaObject detailedRecord = getDetailedRecord(record);
                if (detailedRecord != null) {
                    String action = null;
                    Video  video = null;

                    String videoUrl = extractVideoUrl(detailedRecord);
                    String imageUrl = getFirst(record.getEdmPreviewList());
                    Integer mediaLength = getMediaLength(detailedRecord);

                    if (mediaLength == null || mediaLength == -1) {
                        logError("VIDEO record with no duration set: title[0]: '" + getFirst(record.getTitleList()) + "' dcCreator[0]: '" + getFirst(record.getDcCreatorList()) + "'", null);
                        continue;
                    }

                    if (StringUtils.isEmpty(videoUrl)) {
                        logError("VIDEO record with empty video URL: title[0]: '" + getFirst(record.getTitleList()) + "' dcCreator[0]: '" + getFirst(record.getDcCreatorList()) + "'", null);
                        continue;
                    } else if (videoUrl.length() > 1024) {
                        logError("VIDEO record with too long video URL: title[0]: '" + getFirst(record.getTitleList()) + "' url: '" + videoUrl + "'", null);
                        continue;
                    }

                    if (StringUtils.isEmpty(imageUrl)) {
                        logError("VIDEO record with empty image URL: title[0]: '" + getFirst(record.getTitleList()) + "' dcCreator[0]: '" + getFirst(record.getDcCreatorList()) + "'", null);
                        continue;
                    } else if (videoUrl.length() > 1024) {
                        logError("VIDEO record with too long image URL: title[0]: '" + getFirst(record.getTitleList()) + "' url: '" + imageUrl + "'", null);
                        continue;
                    }

                    // only when we detected a video url
                    // try to find an existing video with these properties
                    // we try to use url as unique identifier
                    video = videoRepository.getBySourceUrl(videoUrl);
                    if (video == null) {
                        // the repository takes care of insert or update so we can just create a new one
                        // if no existing found
                        video = new Video();
                        action = "Imported";
                    } else {
                        action = "Updated";
                    }

                    video.setDuration(mediaLength);
                    video.setEnabled(true);
                    video.setFragmentID(null);
                    video.setImageUrl(imageUrl);
                    video.setPlayerType(PlayerType.JW);
                    video.setSourceUrl(videoUrl);
                    video.setStartTime(0);
                    video.setTitle(StringUtils.left(getFirst(record.getTitleList()), 255));
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(video.toString());
                    }
                    videoRepository.store(video);
                    logInfo(action + " record with title: '" + getFirst(record.getTitleList()) + "' dcCreator: '" + getFirst(record.getDcCreatorList()) + "'");
                } else {
                    logError("No detailed record (OBJECT) found for record with title: '" + getFirst(record.getTitleList()) + "' dcCreator[0]: '" + getFirst(record.getDcCreatorList()) + "'", null);
                }
            } else {
                LOG.debug("Skipped record with title: '" + getFirst(record.getTitleList()) + "' dcCreator[0]: '" + getFirst(record.getDcCreatorList()) + "' because its type isn't VIDEO");
            }
        }
    }

    private Integer getMediaLength(final EuropeanaObject object) {
        if (object.getProxies() != null) {
            List<EuropeanaProxy> proxyList = object.getProxies();
            if (proxyList != null) {
                for (EuropeanaProxy proxy : proxyList) {
                    EuropeanaMultiDef def = proxy.getDctermsExtent();
                    if (def != null) {
                        List<String> extendList = def.getDef();
                        String extend = getFirst(extendList);
                        if (StringUtils.isNotEmpty(extend)) {
                            long duration = fromXmlDuration(extend);
                            if (duration > 0) {
                                return Integer.valueOf((int) duration);
                            }
                        }
                    }
                }
            }
        }
        /*MediaPlayer player = new MediaPlayer();
        player.setMediaLocation(videoUrl);
        try {
            Time duration = null;
            player.start();
            duration = player.getDuration();
            if (duration != MediaPlayer.DURATION_UNKNOWN) {
                return (int) (duration.getSeconds() * 1000.0d);
            }
        } finally {
            player.stop();
        } */
        return -1; // no duration set
    }

    private long fromXmlDuration(String duration) {
        // PnYnMnDTnHnMnS
        Matcher matcher = null;
        int hours = 0, minutes = 0, seconds = 0;

        if (duration != null && duration.startsWith("P")) {
            matcher = XML_DURATION_PATTERN_HR.matcher(duration);
            if (matcher.find()) {
                hours = Integer.parseInt(matcher.group(1));
            }
            matcher = XML_DURATION_PATTERN_MN.matcher(duration);
            if (matcher.find()) {
                minutes = Integer.parseInt(matcher.group(1));
            }
            matcher = XML_DURATION_PATTERN_SE.matcher(duration);
            if (matcher.find()) {
                seconds = Integer.parseInt(matcher.group(1));
            }
            return 1000 * (hours * 60 * 60 + minutes * 60 + seconds);
        }
        return -1;
    }

    private boolean isValidVideoUrl(String videoUrl) {
        for (Pattern pattern : validVideoUrlList) {
            Matcher matcher = pattern.matcher(videoUrl);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    private String extractVideoUrl(EuropeanaObject record) {
        Assert.notNull(record);

        List<EuropeanaAggregation> aggregationList = record.getAggregations();
        if (aggregationList != null && aggregationList.size() > 0) {
            // find URL using the preferred method
            for (EuropeanaAggregation aggregation : aggregationList) {
                if (StringUtils.isNotEmpty(aggregation.getEdmIsShownBy())) {
                    String videoUrl = aggregation.getEdmIsShownBy();
                    // this is the preferred way of obtaining the stream's URL
                    if (isValidVideoUrl(videoUrl)) {
                        return aggregation.getEdmIsShownBy();
                    }
                }
            }
            // find URL using fallback method (examine the webresources)
            logWarn("Record with title: '" + getFirst(record.getTitleList()) + "' dcCreator: '" + getFirst(record.getDcCreatorList()) + "' has no video URL available in field 'EdmIsShownBy'. Now examining webresources!");
            for (EuropeanaAggregation aggregation : aggregationList) {
                List<EuropeanaAbout> webresourcesList = aggregation.getWebResources();
                if (webresourcesList != null) {
                    for (EuropeanaAbout about : webresourcesList) {
                        String videoUrl = about.getAbout();
                        if (isValidVideoUrl(videoUrl)) {
                            // yes, this is a video link
                            logWarn("Using video URL found in webresources!");
                            return videoUrl;
                        }

                    }
                }
            }
        }
        return null;
    }

    private EuropeanaObject getDetailedRecord(EuropeanaRecord record) {
        InputStream is = null;
        String link = record.getLink();
        try {
            if (StringUtils.isNotEmpty(link)) {
                HttpResponse httpResponse;
                HttpUriRequest request = new HttpGet(link);
                httpResponse = httpClient.execute(request);
                if (httpResponse != null && httpResponse.getStatusLine() != null && httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    if (entity != null) {
                        String line = null;
                        is = entity.getContent(); // DRS TODO: cleanup
                        StringBuilder jsonbuild = new StringBuilder(); // debug code
                        BufferedReader reader = null;

                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                        while((line = reader.readLine()) != null) {
                            jsonbuild.append(line);
                        }
                        if(LOG.isDebugEnabled()){
                            LOG.debug(jsonbuild.toString());
                        }

                        JsonFactory fact = new JsonFactory();
                        ObjectMapper mapper = new ObjectMapper(fact);
                        EuropeanaResponse response = mapper.readValue(jsonbuild.toString(), EuropeanaResponse.class);

                        return response.getObject();
                    }
                }
                throw new RuntimeException("getDetailedRecord failed");
            }
        } catch (JsonParseException e) {
            throw new RuntimeException("JsonParseException while parsing details: " + e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException("JsonMappingException while parsing details: " + e.getMessage(), e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException("ClientProtocolException while parsing details: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IOException while parsing details: " + e.getMessage(), e);
        } finally {
            if (is != null) {
                try { is.close(); } catch (IOException e) { }
            }
        }
        return null;
    }

    private <T> T getFirst(List<T> list) {
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private EuropeanaResponse extractEuropeanaData(HttpEntity entity) {
        EuropeanaResponse response = null;
        String line = null; // debug code
        StringBuilder jsonbuild = new StringBuilder(); // debug code
        BufferedReader reader = null;

        Assert.notNull(entity);

        try {
            reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));

            while((line = reader.readLine()) != null) {
                jsonbuild.append(line);
            }
            if(LOG.isDebugEnabled()){
                LOG.debug(jsonbuild.toString());
            }
            JsonFactory fact = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(fact);
            response = mapper.readValue(jsonbuild.toString(), EuropeanaResponse.class);

            response.setJSON(jsonbuild.toString());

        } catch(Exception e) {
            throw new RuntimeException("An error occured. Errormsg: " + e.getMessage(), e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch(Exception e) {}
        }
        return response;
    }

    private HttpResponse queryServer(final String query, int start, Integer overrideRowsPerQuery) {
        if (overrideRowsPerQuery == null) {
            overrideRowsPerQuery = rowsPerQuery;
        }
        try {
            HttpUriRequest request = new HttpGet(createQueryURI(URLEncoder.encode(query, "UTF8"), start, overrideRowsPerQuery, profile));

            return httpClient.execute(request);

        } catch(URISyntaxException use) {
            throw new IllegalArgumentException("The import URL is malformed, please correct the config file. Errormsg: " + use.getMessage(), use);
        } catch (Exception e) {
            throw new RuntimeException("An error occured. Errormsg: " + e.getMessage(), e);
        }
    }

    private URI createQueryURI(String query, int start, int rows, String profile) throws URISyntaxException {
        String sUri = String.format("%s?wskey=%s&query=%s&start=%d&rows=%d&profile=%s", europeanaBaseURL, europeanaAPIKey, query, start+1, rows, profile);
        return new URI(sUri);
    }

    public List<String> getImportLog() {
        return importLog;
    }

    public int getImportingProgress() {
        return importingProgress;
    }

    public int getImportingQuantity() {
        return importingQuantity;
    }

    public String getImportingTitle() {
        return importingTitle;
    }

    public String getRunningQuery() {
        return runningQuery;
    }

}
