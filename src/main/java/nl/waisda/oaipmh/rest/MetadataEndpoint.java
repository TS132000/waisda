package nl.waisda.oaipmh.rest;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nl.waisda.oaipmh.logic.OAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Element;

/**
 * User: Danny
 * Date: 9-9-13
 * Time: 13:36
 */
@Controller
public class MetadataEndpoint {

    @Autowired
    private OAIService service;

    @RequestMapping(value="/video/{id}",method={RequestMethod.GET}, produces = "application/xml")
    @ResponseBody
    public void getVideo(@PathVariable String id, HttpServletResponse response) {
        Assert.hasLength(id);
        Element el = (Element) service.createGetRecordType(String.format("oai:waisda.beeldengeluid.nl:video:%s", id), "rdf").getGetRecord().getRecord().getMetadata().getAny();
        try {
            Result result = new StreamResult(response.getOutputStream());
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(el), result);
        } catch(Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    @RequestMapping(value="/annotation/{id}",method={RequestMethod.GET}, produces = "application/xml")
    @ResponseBody
    public void getAnnotation(@PathVariable String id, HttpServletResponse response) {
        Assert.hasLength(id);
        Element el = (Element) service.createGetRecordType(String.format("oai:waisda.beeldengeluid.nl:tagentry:%s", id), "rdf").getGetRecord().getRecord().getMetadata().getAny();
        try {
            Result result = new StreamResult(response.getOutputStream());
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(el), result);
        } catch(Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}
