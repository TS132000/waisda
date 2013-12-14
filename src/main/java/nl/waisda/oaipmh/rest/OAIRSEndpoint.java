package nl.waisda.oaipmh.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nl.waisda.oaipmh.logic.OAIService;
import nl.waisda.oaipmh.model.jaxb.pmh.OAIPMH;

/**
 * User: Danny
 * Date: 9-9-13
 * Time: 13:36
 */
@Controller
public class OAIRSEndpoint {

    @Autowired
    private OAIService service;

    /**
     * Arguments
     *
     * @param identifier a required argument that specifies the unique identifier of the item in the repository from which the record must be disseminated.
     * @param metadataPrefix a required argument that specifies the metadataPrefix of the format that should be included in the metadata part of the returned record . A record should only be returned if the format specified by the metadataPrefix can be disseminated from the item identified by the value of the identifier argument. The metadata formats supported by a repository and for a particular record can be retrieved using the ListMetadataFormats request.
     *   Error and Exception Conditions

     * error badArgument - The request includes illegal arguments or is missing required arguments.
     * error cannotDisseminateFormat - The value of the metadataPrefix argument is not supported by the item identified by the value of the identifier argument.
     * error idDoesNotExist - The value of the identifier argument is unknown or illegal in this repository.
     *
     * @return OAIPMH element
     */
    @RequestMapping(value="/oai", params = "verb=GetRecord", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH getRecord(
        @RequestParam(required = true) String identifier,
        @RequestParam(required = true) String metadataPrefix) {

        return service.createGetRecordType(identifier, metadataPrefix);
    }

    @RequestMapping(value="/oai", params = "verb=Identify", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH identify() {

        return service.createIdentify();
    }

    @RequestMapping(value="/oai", params = "verb=ListMetadataFormats", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH listMetadataFormats(@RequestParam(required = false) String identifier) {
        return service.createListMetadataFormats(identifier);
    }

    @RequestMapping(value="/oai", params = "verb=ListSets", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH listSets() {
        return service.createListSets();
    }

    @RequestMapping(value="/oai", params = "verb=ListIdentifiers", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH listIdentifiers(
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String until,
        @RequestParam(required = false) String set,
        @RequestParam(required = false) String resumptionToken,
        @RequestParam(required = false) String metadataPrefix) {

        return service.createListIdentifiers(from, until, set, resumptionToken, metadataPrefix);
    }

    @RequestMapping(value="/oai", params = "verb=ListRecords", method={RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
    public @ResponseBody OAIPMH listRecords(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String until,
            @RequestParam(required = false) String set,
            @RequestParam(required = false) String resumptionToken,
            @RequestParam(required = false) String metadataPrefix) {

        return service.createListRecords(from, until, set, resumptionToken, metadataPrefix);
    }
}
