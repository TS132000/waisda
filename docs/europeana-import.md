# Europeana item importer

## Preface

Waisda provides means to import video URLs (and possibly keywords as well) from Europeana. Waisda provides a
simple importer that uses Europeana web API and a webbased user interface which allows the user to import
items based on a standard Europeana Search Query

The following sections describe the importer and user interface.

## Usage

### Create a admin user

To import Europeana data the user must log in with an admin enabled user first. To create an admin user, register
a new user in waisda frontend and set corresponding User table record's admin flag to '1'.

### Enter a search query in import page
When logged in, browse to URL http://waisda.host.name/europeanaimport/start. The screen shown provides the user
with a search input box where the user can enter a Europeana search query. The items Europeana finds based on this
search query are imported into the Waisda database.

After entering a search query and hitting the 'start' button, the import page feeds back the total number of items
found (any type, not only VIDEO). If this number meets the expectation, hit the 'start' button again to actually
start importing the items. If the search query doesn't meet the expectation, another query can be entered and the
import page show the total number of items to be imported again.

Note that a search wil be batched into one or more subsearches, with each subsearch fetching at most a configured 
number of items (see Config and Setup).

### Search Query formats:

Please have a look at Europeana API documentation to learn more about query formats. Basically, Europeana can query
most fields it stores. For example, to look for a set of items of a given data provider use the following search
query:
    provider_aggregation_edm_dataProvider:"Collection name"

Where 'collection name' is the name of the dataset to import (for example: 'Open Beelden')

### Import filtering

The importer applies a filter to perform a best effort
importing only valid video's. A video is considered valid if:
- imageUrl <= 1024 characters
- sourceUrl <= 1024 characters
- sourceUrl matches one of the 'accepted URL' expressions (see Config and Setup section)
- the video source has duration filled in correctly
- the video hasn't been imported yet, which is determined by. Videos that have been imported already will be updated
  instead

### Import progress

When an import is running the importer page shows a summary that indicates the progress and the import log.
The progress indication shows:
- currently imported title
- current index number
- number of total items

The import log shows the result of each imported item using the log levels INFO, WARNING and ERROR. So it logs when
an URL is too long, no duration filled in etc.

Beside the frontend logging, waisda also creates a import log file named europeanaimport.log.

### Stop the importer

The importer can run only one process at a time. It doesn't allow to start more than 1 import (at least when
deployed as a single active node). To stop a currently running import, hit the stop button. The import log will show
that the stop command was issued.

## Config and Setup

The importer configuration is set in config.properties. The following parameters must be set:
waisda.import.europeana.baseurl         the Europeana base URL, currently: http://preview.europeana.eu/api/v2/search.json
waisda.import.europeana.apikey          the Europeana API key, must be requested at Europeana
waisda.import.europeana.privkey         the Europeana Private key, must be requested at Europeana     
waisda.import.europeana.rowsperquery    the maximum number of items we request from Europeana per search
waisda.import.europeana.profile         the profile used in a search. Set this to 'minimal'
waisda.import.europeana.validvideourls  cumma separated list of regular expressions a videoUrl must match with to accept
                                        it as valid input

## Technical Description

This section describes the technical aspects of the Europeana importer:

### Overview

An overview of the steps the application performs in case of a Europeana import

[ROLE: USER]
- The user enters a search query in the importer frontend

[ROLE: SYSTEM]
- The import service starts the import in a background thread using Spring TaskExecutor
- The import service queries Europeana API for a subset of items using the maximum rows per query
- The Europeana result is converted to Java model objects using Jackson
- The import service loops the received items and fetches item detailed data from Europeana API
- The basic data contains the title and image URL. The item's detailed data contains the video URL and duration
- The import service validates the length for image and source URLs, sourceUrl's validity and cuts the title to
  a maximum of 255 characters. In case of validation error, the item is skipped
- The subset of items is inserted (when new) or updated (when it exists already) into the database
- In case of failure, a complete subset is rolled back in the database and therefor will not be imported. The process
  will try to execute the next subsets though
- The import service updates the log and progress summary. Note that aside the file log, the log is kept locally 
  in memory for frontend progress requests
- The import service continues fetching the next subset of items and processes it

[ROLE: USER]
- The importer frontend updates the progress notification and log every 4 seconds using jQuery
- The importer frontend sends a 'stop request' when the user hits the stop button

### Classes

This section describes the classes that implement the Importer

#### Importer logic
nl.waisda.services.EuropeanaImportServiceIF     Interface for Europeana import service, provides methods to
                                                - fetch the number of items to be imported with a given search
                                                - execute an import
                                                - execute an import running detached from the main thread
                                                - stop current import
                                                - request the progress summary fields
                                                - request the progress log

nl.waisda.services.EuropeanaImportService       Implementation for Europeana import service

nl.waisda.services.TransactionServiceIF         Interface for TransactionService. Basically a facade to
                                                execute Callables in transactional context. Currently only provides
                                                a method to:
                                                - run the Callable in a new Transaction (Propagation.REQUIRES_NEW)

nl.waisda.services.TransactionService           Implementation of TransactionService.

#### Importer model
nl.waisda.exceptions.EuropeanaImportException   Thrown when a functional error occurs during import

nl.waisda.model.europeana.EuropeanaResponse     Europeana API response DTO for Json marshalling
nl.waisda.model.europeana.EuropeanaRecord       Europeana item DTO
nl.waisda.model.europeana.EuropeanaObject       Europeana object DTO (adds a few fields to Europeana Record)

nl.waisda.model.europeana.EuropeanaProxy        Entry for Europeana data category 'Proxies'
nl.waisda.model.europeana.EuropeanaAggregation  Entry for Europeana data category 'Aggregation'

nl.waisda.model.europeana.EuropeanaAbout        Europeana 'About' key value pair
nl.waisda.model.europeana.EuropeanaSingleDef    Europeana 'Default' key value pair (single value)
nl.waisda.model.europeana.EuropeanaMultiDef     Europeana 'Default' key value pair (multi value)

#### Importer frontend
nl.waisda.controllers.EuropeanaImportController Frontend controller for Europeana import service. The user
                                                must have isAdmin flag set to access pages

### Changes
- Added 'isAdmin' field to User table and DTO
- Added 'findBySourceUrl()' method to VideoRepository
- Modified Video.imageUrl and Video.sourceUrl field lengths to 1024
- Added unique constraint and index to Video.imageUrl field

