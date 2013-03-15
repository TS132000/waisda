# Getting started

You will need at least Java 6, Maven and MySQL to run the server in debug mode. The scripts and examples below will look for the `java` and `mvn` executables.

## Downloading the source code

The source code can be found on the [GitHub project page](https://github.com/beeldengeluid/waisda).

You can either clone (or fork) the repository (`git clone git@github.com:beeldengeluid/waisda.git`), or you can [download the source as a ZIP file](https://github.com/beeldengeluid/waisda/zipball/master).

## Creating the database

Waisda requires a MySQL database. To help you setup this database and the required tables the source tree contains the script files `sql/create-database.sql` and `sql/create-tables.sql`. In the following instructions we assume mysql is available from the command line using the root account. To see how to specify username, password, hostname and other parameters, run `mysql --help`. 

Start by creating the database: 
```
$ mysql -u root < sql/create-database.sql
```

This creates a database named `waisda` and specifies UTF-8 as the default character encoding and `utf8_general_ci` as the default collation. This is important when storing tag entries with accented characters. Also, for table indexes to work, the two columns on which SQL joins are executed need to be encoded with the same encoding and collation.

Now add the tables to the `waisda` database:
```
$ mysql -u root waisda < sql/create-tables.sql
```

The database `waisda` should now contain 8 tables. Consult the mysql documentation if you want to see the content of your database.

Finally you need to create a user account that has access to the `waisda` database: 
```
$ mysql -u root < sql/create-user.sql
```
By default Waisda is configured to access the database using the account `waisda` with password `waisda`. The user is granted selection and modification rights.


## [NEW] Setup Bitronix transactionmanager in Tomcat

This application uses Bitronix transactionmanager to provide transaction management. The application's /webapp/META-INF/context.xml file contains the configuration settings. To make the whole
work correctly, Bitronix must be installed into Tomcat. The following steps are required to do so:

- download Bitronix transactionmanger 2.1.3 (btm-dist-2.1.3.zip) from http://docs.codehaus.org/display/BTM/Home
- try to follow the Tomcat setup guide http://docs.codehaus.org/display/BTM/Tomcat2x. If this doesn't result in a working situation, follow the steps below.

- extract the following libs contained in btm-dist-2.1.3.zip to Tomcat/lib directory:
    - btm-2.1.3.jar
    - btm-tomcat55-lifecycle-2.1.3.jar
    - geronimo-jta_1.1_spec-1.1.1.jar
    - slf4j-api-1.6.4.jar
    - slf4j-jdk14-1.6.4.jar
- copy mysql jdbc driver lib (mysql-connector-java-5.1.7-bin.jar) to Tomcat/lib directory
- define the btm lifecycle listener in Tomcat by adding the following line directly below the <Server> element in /tomcat/conf/Server.xml:
    <Listener className="bitronix.tm.integration.tomcat55.BTMLifecycleListener" />
- Specify where btm can find its properties file by adding the 'btm.root' and 'bitronix.tm.resource.configuration' to the tomcat VM options (can be done for example by
  changing CATALINA_OPTS environment variable or adding it to the IDE's Tomcat VM settings):
    -Dbtm.root=%CATALINA_HOME%/apache-tomcat-6.0.18 -Dbitronix.tm.resource.configuration=%CATALINA_HOME%/apache-tomcat-6.0.18/conf/resources.properties
  NOTE: If %CATALINA_HOME% environment variable is not set, please replace it with the actual tomcat install directory
        Ofcourse when using unix, replace %CATALINA_HOME% by $CATALINA_HOME
- create BTM config file in /tomcat/conf. Name the file 'resources.properties'. The file requires the following content, make sure driverProperties values get set
  to the correct values:
    resource.ds.className=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource
    resource.ds.uniqueName=pooledDataSource
    resource.ds.minPoolSize=5
    resource.ds.maxPoolSize=20
    resource.ds.allowLocalTransactions=true
    resource.ds.driverProperties.url=jdbc:mysql://localhost:3306/waisda?characterEncoding=UTF-8&amp;useUnicode=true
    resource.ds.driverProperties.user=USER
    resource.ds.driverProperties.password=PASSWORD
- NOTE: Make sure there is no JTA library in Waisda's WEB-INF/lib directory

## Running in debug mode

The source tree has a Makefile which lists commands for various useful scenarios. The first (and default) target is called `run`, which runs the website in debug mode using the Jetty webserver available to Maven.

If you used the default database name and user account this should work straightaway. If you use a different database name or user account you have to update the web server configuration. Open `src/main/webapp/WEB-INF/jetty.xml` and find the tag with `name="driverProperties"`. Change the parameters to the appropriate values, then run `make` from the root of the source tree. 

The first time, Maven will download lots of libraries the application depends on. It will take a few minutes. Subsequent times will be much faster.

## Deploying

To deploy the website in a proper J2EE container, run `make deploy`. This will create a `.war` file which you can then place, say, in a Tomcat container. The website expects to be the root website (all its URLs must be top-level), so you will probably have to rename the file to `ROOT.war` (at least in the case of Tomcat).

The database settings for running the website this way are read from `src/main/resources/config.properties` rather than `jetty.xml`. Look for the properties whose names start with `jdbc.`.

## Adding your own videos

To add your own videos fill the `Video` table with records. They will then
become available in the website for playing games and earning points. The
default video selection for the channels on the homepage chooses randomly
(uniformly) from all available videos. The structure of the table is explained
in the "Backend architecture" chapter.

If you have existing data available in SQL format, you can import it using various MySQL graphical tools, or using the command line.

If you have video data in CSV format, again you can use one of the many MySQL tools out there, or use the command line utility [`mysqlimport`](https://dev.mysql.com/doc/refman/5.0/en/mysqlimport.html). It allows you to map the various columns in the CSV file to specific columns in the `Video` table.

For testing purposes the source code provides and example SQL file in the directory `sql`. The file [preligner_videos.sql](https://github.com/beeldengeluid/waisda/blob/master/sql/preliger_videos.sql) contains the required metadata for 1252 videos from the [Prelinger archive](http://archive.org/details/prelinger) hosted on [archive.org](http://archive.org/). To import these videos type:

```
mysql -u root waisda < sql/prelinger_videos.sql
```

Now you can start your server and point your browser to `localhost:8080`.

```
make run
```

## Adding your own style

### Changing colors

All colors are defined in [variables.less](../src/main/webapp/static/styles/less/variables.less). The easiest way to change a color throughout the whole project is changing it's RGB-value in this less-file. To establish a link between the RGB-values and what you see on screen the variable-name should describe the color. This makes it easier to recognize and visualize the values and their on-screen representation. If a variable's name does not describe the new color very well please change it accordingly and run a simple search and replace action through all the less files.

### Adding your logo

The logo is placed in the header. To change the logo place your logo-image in the [images folder](../src/main/webapp/static/img). And change the path for the dummy-logo image to your logo in the file [body.tag](../src/main/webapp/WEB-INF/tags/body.tag). To find the dummy-logo image in the code search for alt="LOGO" within this file. The logo should leave enough space for the tag-line about the amount of tags and matches. In the current setup a logo should not be wider than 220 pixels. 

### Changing the grid

The document about [Front-end architecture](frontend.md) contains a description of how the grid can be accustomed to your specific needs. The current grid is based on 12 columns of 60 pixels wide divided by a 20 pixel wide gutter. This is also the document where you can find more in-debt descriptions about the front-end architecture. It's a starting point in case you need to make more changes to the visual design or layout. 
