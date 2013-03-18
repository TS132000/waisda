package nl.waisda.controllers;

import org.hibernate.tool.hbm2x.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import nl.waisda.domain.User;
import nl.waisda.exceptions.EuropeanaImportException;
import nl.waisda.forms.EuropeanaImportForm;
import nl.waisda.services.EuropeanaImportServiceIF;
import nl.waisda.services.UserSessionService;

/**
 * Very simple controller to control Europeana imports
 * It features a method to build and show the initial page
 * and a single method to handle form input. The latter tests the
 * button pressed and performs the adequate action
 * @author Danny Sedney (sdengineering77@gmail.com)
 */
@Controller
public class EuropeanaImportController {
    private static final String FORM_ATTRIBUTE = "form";
    private static final String PAGE_IMPORT_REQUEST = "/europeanaimport/{action}";
    private static final String PAGE_IMPORT = "europeanaimport";
    private static final String PAGE_IMPORT_STATS = "europeanaimport-stats";
    private static final String PAGE_IMPORT_REDIR = "redirect:start";
    private static final String PAGE_LOGON_REDIR = "redirect:/inloggen";

    @Autowired
    private EuropeanaImportServiceIF importService;
    @Autowired
    private UserSessionService userSessionService;

    /**
     * Does current user have priviledges to run the importer?
     * @return
     */
    private boolean checkAuth() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attributes != null) {
            User user = userSessionService.getCurrentUser(attributes.getRequest().getSession());
            if (user != null && Boolean.TRUE.equals(user.getAdmin())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Shows the import gui. Note that the import gui consists of two facets: a holder page that just defines
     * the superficial page (i.e. head and body) and a contents part, that defines the searchbox, progress section
     * and logs. When first called, the holderpage is returned. This page @include's the contents part.
     * When the import process is started, jQuery updates the content part of the page every X seconds.
     * Note: this has been set up very simplistic
     * @param action
     * @param map
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = PAGE_IMPORT_REQUEST)
    public String showImportPage(
        @PathVariable() String action,
                        ModelMap map) {
        EuropeanaImportForm form = new EuropeanaImportForm();

        if (!checkAuth()) {
            // user must be logged in with admin account
            return PAGE_LOGON_REDIR;
        }

        form.setImportRunning(importService.isRunning());
        form.setImportingProgress(importService.getImportingProgress());
        form.setImportingTitle(importService.getImportingTitle());
        form.setImportingTotalItems(importService.getImportingQuantity());
        form.setImportingQuery(importService.getRunningQuery());

        form.setLog(importService.getImportLog());

        map.put(FORM_ATTRIBUTE, form);

        if ("stats".equals(action)) { // show the contents part (perhaps a bit misnamed)
            return PAGE_IMPORT_STATS;
        }
        // just return the holder part for any other url requested
        return PAGE_IMPORT;

    }

    /**
     * Handle the import gui's input. It handles three actions:
     * - when the user entered a new search query, the number of items found is returned
     * - when the user confirms the result, the import process is started, running detached from current thread
     * - when the user hits stop, the import process is stopped
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = PAGE_IMPORT_REQUEST)
    public String handleImportPageCommand(
        @ModelAttribute(FORM_ATTRIBUTE) EuropeanaImportForm form,
                                        BindingResult result) {

        Assert.notNull(form);

        if (!checkAuth()) {
            // user must be logged in with admin account
            return PAGE_LOGON_REDIR;
        }

        try {
            if (StringUtils.isNotEmpty(form.getStart()) && StringUtils.isNotEmpty(form.getImportRequestString()) && (form.getImportSummary() == null || !form.getImportRequestString().equals(form.getPrevImportRequestString()))) {
                // return the resulting number of items for given search query
                form.setImportSummary(importService.requestSummary(form.getImportRequestString()));
                form.setPrevImportRequestString(form.getImportRequestString());
            } else if (StringUtils.isNotEmpty(form.getStart()) && StringUtils.isNotEmpty(form.getImportRequestString())) {
                // execute the import
                importService.runDetached(form.getImportRequestString());
                form.setPrevImportRequestString(null);
                return PAGE_IMPORT_REDIR;
            } else if (StringUtils.isNotEmpty(form.getStop())) {
                // stop the importer
                importService.requestStop();
                return PAGE_IMPORT_REDIR;
            } else  {
                // search query most probably not filled
                result.rejectValue("importRequestString", null, "Please enter a query");
            }
        } catch(EuropeanaImportException eie) {
            // functional exceptions are shown to user
            result.reject(null, eie.getMessage());
        }

        return PAGE_IMPORT;
    }
}
