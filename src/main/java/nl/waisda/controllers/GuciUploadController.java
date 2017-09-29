package nl.waisda.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import nl.waisda.forms.GuciImportForm;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
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
        map.put(FORM_ATTRIBUTE, form);
        return "guciimport";
    }

    @RequestMapping(value="/guci", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, ModelMap map) throws IOException {
        File store = File.createTempFile("guci", "upload");
        try {
            GuciImportForm form = new GuciImportForm();
            form.setImportRunning(running);
            map.put(FORM_ATTRIBUTE, form);
            file.transferTo(store);
            executor.submit(createCallable(store));
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
                    running = true;
                    CSVFormat format = CSVFormat.DEFAULT.withHeader("Guci", "Title", "Delete").withAllowMissingColumnNames().withIgnoreEmptyLines().withSkipHeaderRecord();
                    CSVParser parser = CSVParser.parse(store, Charset.forName("UTF-8"), format);
                    Iterator<CSVRecord> recordIterator = parser.iterator();
                    while(recordIterator.hasNext()) {
                        CSVRecord record = recordIterator.next();
                        LOGGER.info(record);
                    }
                } finally {
                    running = false;
                }
                return null;
            }
        };
    }
}
