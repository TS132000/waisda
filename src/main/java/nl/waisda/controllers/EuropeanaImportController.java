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

    @RequestMapping(method = RequestMethod.GET, value = PAGE_IMPORT_REQUEST)
    public String showImportPage(
        @PathVariable() String action,
                        ModelMap map) {
        EuropeanaImportForm form = new EuropeanaImportForm();

        if (!checkAuth()) {
            return PAGE_LOGON_REDIR;
        }

        form.setImportRunning(importService.isRunning());
        form.setImportingProgress(importService.getImportingProgress());
        form.setImportingTitle(importService.getImportingTitle());
        form.setImportingTotalItems(importService.getImportingQuantity());
        form.setImportingQuery(importService.getRunningQuery());

        form.setLog(importService.getImportLog());

        map.put(FORM_ATTRIBUTE, form);

        if ("stats".equals(action)) {
            return PAGE_IMPORT_STATS;
        }
        // just return this for any other url
        return PAGE_IMPORT;

    }

    @RequestMapping(method = RequestMethod.POST, value = PAGE_IMPORT_REQUEST)
    public String handleImportPageCommand(
        @ModelAttribute(FORM_ATTRIBUTE) EuropeanaImportForm form,
                                        BindingResult result) {

        Assert.notNull(form);

        if (!checkAuth()) {
            return PAGE_LOGON_REDIR;
        }

        try {
            if (StringUtils.isNotEmpty(form.getStart()) && StringUtils.isNotEmpty(form.getImportRequestString()) && (form.getImportSummary() == null || !form.getImportRequestString().equals(form.getPrevImportRequestString()))) {
                form.setImportSummary(importService.requestSummary(form.getImportRequestString()));
                form.setPrevImportRequestString(form.getImportRequestString());
            } else if (StringUtils.isNotEmpty(form.getStart()) && StringUtils.isNotEmpty(form.getImportRequestString())) {
                importService.runDetached(form.getImportRequestString());
                return PAGE_IMPORT_REDIR;
            } else if (StringUtils.isNotEmpty(form.getStop())) {
                importService.requestStop();
                return PAGE_IMPORT_REDIR;
            } else  {
                result.rejectValue("importRequestString", null, "Please enter a query");
            }
        } catch(EuropeanaImportException eie) {
            result.reject(null, eie.getMessage());
        }

        return PAGE_IMPORT;
    }
}
