# Filesutra

*Filesutra has now evolved into [CloudFiles](https://cloudfilesapp.co), we will soon opensource the filesutra component of CloudFiles. At CloudFiles, we have re-written Filesutra component using NodeJS & ReactJS.*

----

Filesutra is an easy way to implement imports and uploads from all popular cloud storage. Supports google drive, onedrive, box, dropbox.

Filesutra backend is written using [grails framework](https://grails.org/) and frontend is written using [angularjs](https://angularjs.org/).

To use Filesutra in a web application, include `filesutra.js` from the server where Filesutra app is running. A demo application is hosted at filesutra.com, so you can quickly use the Filesutra in your application.

Add following script to your html page -

    <script src="http://filesutra.com/js/filesutra.js"></script>

Then add following code to click handler of import button(assuming jquery is already included in the page) - 

    $("#importBtn").click(function() {
      filesutra.importFiles(function(data) {
      // File import response is available in 'data' variable
      /** sample response
      {
        "fileName": "sample.pdf",
        "downloadUrl": "http://filesutra.com/download/teGhzSfIp4zJLW9",
        "size": 12345
      }
      **/
      console.log(data);
      });
    });

To use Filesutra in your production web apps, please host Filesutra on your own server by following instructions below. Then add `filesutra.js` from your own running instance of Filesutra.

### How to run Filesutra - 
Clone this repository.  Install [postgreSQL](http://www.postgresql.org/) database. Create a user in postgres and a blank database using following commands in a terminal - 

    psql -U postgres
    
    CREATE ROLE <user_name>;
    ALTER ROLE <user_name> WITH LOGIN PASSWORD '<password>' NOSUPERUSER NOCREATEDB NOCREATEROLE;
    CREATE DATABASE filesutradb OWNER <user_name>;
    REVOKE ALL ON DATABASE filesutradb FROM PUBLIC;
    GRANT CONNECT ON DATABASE filesutradb TO <user_name>;
    GRANT ALL ON DATABASE filesutradb TO <user_name>;

### Configuration - 	
 `cd` to Filesutra repo.  Copy the file `filesutra-config.groovy` from `docs/` to any place where you want to keep the configuration file, just keep is outside the cloned repo. Edit the config file and insert the appropriate values for parameters. You will need to create API projects for Google, OneDrive, Box and OneDrive. Use the following links:

Google - https://console.developers.google.com/project
Redirect URI - <`serverurl`>/auth/googleCallback
        
Box - https://app.box.com/developers/services/edit/
Type - Box Content
Redirect URI - <`server url`>/auth/boxCallback
    		  
Dropbox - https://www.dropbox.com/developers/apps/create
Type - Dropbox API app
Redirect URI - <`server url`>/auth/dropboxCallback

OneDrive - https://account.live.com/developers/applications/create
Redirect URI - <`server url`>/auth/onedriveCallback

After creating these projects, copy the CLIENT_ID, CLIENT_SECRET and REDIRECT_URI to `filesutra-config.groovy` at appropriate places

Once the configuration is in place, set the following Environment variable - `FILESUTRA_CONFIG` to the location where you copied the configuration file. Then run the grails app -

On *.nix system, execute `./grailsw`

On windows system, run `grailsw.bat`

### How to contribute -
Fork this repository, make your changes and send a pull request.
