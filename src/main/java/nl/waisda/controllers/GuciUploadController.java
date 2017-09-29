package nl.waisda.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.waisda.domain.PlayerType;
import nl.waisda.domain.Video;
import nl.waisda.forms.GuciImportForm;
import nl.waisda.repositories.VideoRepository;
import nl.waisda.services.TransactionServiceIF;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * User: Danny
 * Date: 26-9-17
 * Time: 23:02
 */
@Controller
public class GuciUploadController implements DisposableBean {
    private static final Logger LOGGER = Logger.getLogger(GuciUploadController.class);
    private static final String FORM_ATTRIBUTE = "form";
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10));
    public static boolean running = false;
    private List<String> importLog = new LinkedList<String>();
    private String importingTitle;
    private int importingProgress;
    private int importingTotalItems;


    @Autowired
    private HttpClient httpClient;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TransactionServiceIF transactionService;

    @Override
    public void destroy() throws Exception {
        executor.shutdownNow();
    }

    @RequestMapping(value="/guci", method = RequestMethod.GET)
    public String getUploadPage(ModelMap map) {
        GuciImportForm form = new GuciImportForm();
        form.setImportRunning(running);
        map.put(FORM_ATTRIBUTE, form);
        return "guciimport";
    }

    @RequestMapping(value="/guci/stats", method = RequestMethod.GET)
    public String getStatsPage(ModelMap map) {
        GuciImportForm form = new GuciImportForm();
        form.setImportRunning(running);
        form.setLog(importLog);
        form.setImportingProgress(importingProgress);
        form.setImportingTotalItems(importingTotalItems);
        form.setImportingTitle(importingTitle);
        map.put(FORM_ATTRIBUTE, form);
        return "guciimport";
    }

    @RequestMapping(value="/guci", method = RequestMethod.POST)
    public synchronized String upload(@RequestParam("file") MultipartFile file, ModelMap map) throws IOException {
        File store = File.createTempFile("guci", "upload");
        try {
            if (!running) {
                running = true;
                GuciImportForm form = new GuciImportForm();
                importLog.clear();
                importingProgress = 0;
                importingTotalItems = 0;
                importingTitle = null;
                form.setImportRunning(running);
                form.setLog(importLog);
                map.put(FORM_ATTRIBUTE, form);
                file.transferTo(store);
                executor.submit(createCallable(store));
            }
        } catch(Exception e) {
            store.delete();
        }
        return "guciimport";
    }

    private Callable createCallable(final File store) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    CSVFormat format = CSVFormat.DEFAULT.withHeader("Guci", "Title", "Delete").withDelimiter(';').withAllowMissingColumnNames().withIgnoreEmptyLines().withSkipHeaderRecord();
                    CSVParser parser = CSVParser.parse(store, Charset.forName("UTF-8"), format);
                    List<CSVRecord> records = parser.getRecords();
                    importingTotalItems = records.size();
                    Iterator<CSVRecord> recordIterator = records.iterator();
                    while(recordIterator.hasNext()) {
                        final CSVRecord record = recordIterator.next();
                        LOGGER.info(record);
                        transactionService.runInNewTransaction(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                store(record);
                                return null;
                            }
                        });
                    }
                } finally {
                    running = false;
                }
                return null;
            }
        };
    }

    private DocumentContext createJsonPath(String guci) throws Exception {
        HttpGet get = new HttpGet("http://api2.video.beeldengeluid.videodock.com/api/2.0/guci/" +guci);
        HttpResponse response = httpClient.execute(get);
        InputStream inputStream = response.getEntity().getContent();
        DocumentContext dc = JsonPath.parse(inputStream);
        return dc;
    }

    private String getImageUrl(DocumentContext dc) throws Exception {
        String imageUrl = dc.read("$.player.posterUrl");
        if (StringUtils.isBlank(imageUrl)) {
            imageUrl = "/static/img/BenG_testbeeld.jpg";
        }
        return imageUrl;
    }

    private int getDuration(DocumentContext dc) throws Exception {
        try {
            String hlsUrl = dc.read("$.player.stream.hlsUrl");
            if (StringUtils.isNotBlank(hlsUrl)) {
                int lastM3U8Idx = hlsUrl.lastIndexOf(",");
                int lastPathSeparator = hlsUrl.lastIndexOf("/");
                if (lastM3U8Idx < 0) {
                    lastM3U8Idx = lastPathSeparator;
                }
                String M3U8Url = hlsUrl.substring(0, lastPathSeparator) + "/" + hlsUrl.substring(lastM3U8Idx + 1);
                LOGGER.info(M3U8Url);
                HttpGet get = new HttpGet(M3U8Url);
                HttpResponse response = httpClient.execute(get);
                InputStream inputStream = response.getEntity().getContent();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    int duration = 0;
                    while((line = reader.readLine()) != null) {
                        Pattern p = Pattern.compile("#EXTINF:([\\d\\.]+),");
                        Matcher m = p.matcher(line);
                        if (m.matches()) {
                            long time = (long) (Double.parseDouble(m.group(1)) * 1000);
                            duration += time;
                        }
                    }
                    return duration;
                } finally {
                    inputStream.close();
                }
            }
        } catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return -1;
    }

    public static void main(String[] args) {
        Pattern p = Pattern.compile("#EXTINF:([\\d\\.]+),");
        Matcher m = p.matcher("#EXTINF:5.360,");
        if (m.matches()) {
            System.out.println("m " + m.group(1));
        }

    }

    private void store(CSVRecord record) throws Exception {
        String action;
        String guci = record.get("Guci");
        String title = record.get("Title");
        importingProgress = (int) record.getRecordNumber();
        importingTitle = title;
        // only when we detected a video url
        // try to find an existing video with these properties
        // we try to use url as unique identifier
        Video video = videoRepository.getBySourceUrl(guci);
        if (video == null) {
            // the repository takes care of insert or update so we can just create a new one
            // if no existing found
            video = new Video();
            action = "Imported";
        } else {
            action = "Updated";
        }
        try {
            DocumentContext dc = createJsonPath(guci);
            int duration = getDuration(dc);
            String imageUrl = getImageUrl(dc);
            LOGGER.info(guci + " " + duration);

            video.setDuration(duration);
            video.setEnabled(true);
            video.setFragmentID(null);
            video.setImageUrl(imageUrl);
            video.setPlayerType(PlayerType.VIDEODOCK);
            video.setSourceUrl(guci);
            video.setStartTime(0);
            video.setTitle(StringUtils.left(title, 255));
            video.setGuci(guci);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(video.toString());
            }
            videoRepository.store(video);
            importLog.add("SUCCESS: " + action + " " + video.toString());

        } catch(Exception e) {
            importLog.add("FAIL: " + action + " " + video.toString());
            LOGGER.error(e.getMessage(), e);
        }
    }
}
