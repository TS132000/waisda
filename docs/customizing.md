# Customizing Waisda?

## Adding your own videos

To add your own videos, simply fill the `Video` table with records. They will then become available in the website for playing sessions with them and earning scores. The default video selection for the channels on the homepage chooses randomly (uniformly) from all available videos. The structure of the table is explained in the "Backend architecture" chapter.

If you have existing data available in SQL format, you can import it using various MySQL graphical tools, or using the command line:

```
$ mysql < videos.sql
```

Be sure to pass extra options to `mysql` to tell it how to connect to the database.

If you have video data in CSV format, again you can use one of the many MySQL tools out there, or use the command line utility [`mysqlimport`](https://dev.mysql.com/doc/refman/5.0/en/mysqlimport.html). It allows you to map the various columns in the CSV file to specific columns in the `Video` table.

## Customizing channels

By default, the channels on the homepage show a uniform random selection of all available videos. If you would like to customize this, the place to do that is in method `getChannelContent` of class `VideoService`. This method is called by the `HomeController` to determine what channels to show. The `VideoService` talks to the `VideoRepository` to actually query the database. By writing your own SQL queries to select videos, you can make the video selection as complex as you prefer.

## Adding matching tags

Table `MatchingTag` contains tuples of normalized tags that *match* one another. When users enter tags, they receive higher scores if their tags match other tags, as explained in detail in the introduction of the documentation.

To supply your own matching tags (for example, pairs of synonyms), populate the `MatchingTag` table with them. Make sure that every tuple's elements are lexicographically sorted. E.g. use `('aalmoes', 'gift')` rather than `('gift', 'aalmoes')`.

## Adding dictionaries

## Adding your own style
### Changing colors
All colors are defined in [variables.less](https://github.com/beeldengeluid/waisda/tree/master/src/main/webapp/static/styles/less/variables.less). The easiest way to change a color throughout the whole project is changing it's RGB-value in this less-file. To establish a link between the RGB-values and what you see on screen the variable-name should describe the color. This makes it easier to recognize and visualize the values and their on-screen representation. If a variable's name does not describe the new color very well please change it accordingly and run a simple search and replace action through all the less files.

### Adding your logo
The logo is placed in the header. To change the logo place your logo-image in the [images folder](https://github.com/beeldengeluid/waisda/tree/master/src/main/webapp/static/img). And change the path  for the dummy-logo image to your logo in the file [body.tag](https://github.com/beeldengeluid/waisda/blob/master/src/main/webapp/WEB-INF/tags/body.tag). To find the dummy-logo image in the code search for alt="LOGO" within this file. The logo should leave enough space for the tag-line about the amount of tags and matches. In the current setup a logo should not be wider than 220 pixels. 

### Changing the grid
The document about [Front-end architecture](https://github.com/beeldengeluid/waisda/blob/master/docs/frontend.md) contains a description of how the grid can be accustomed to your specific needs. The current grid is based on 12 columns of 60 pixels wide divided by a 20 pixel wide gutter. This is also the document where you can find more in-debt descriptions about the front-end architecture. It's a starting point in case you need to make more changes to the visual design or layout. 

## Translating pages

## Customizing scoring

## Modifying the database structure

* Adding tables
* Adding columns
* Hibernate mappings

## Adding new pages

* Add controller method
* Add JSP view
* Possibly create new view model class
