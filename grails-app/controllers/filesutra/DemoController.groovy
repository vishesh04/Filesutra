package filesutra

import grails.converters.JSON
import groovy.json.JsonBuilder

import java.io.File;
import org.apache.commons.io.FileUtils;




class DemoController {

    // mix max integration
    def mixmaxEditor() {

    }

    def mixmaxResolver() {
        def input = JSON.parse(params.params)
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Origin", "https://compose.mixmax.com")
        println params.fileName
        def resp = [
                raw: true,
                body: "<a href='$input.downloadUrl'>$input.fileName</a>"
        ]
        render resp as JSON
    }

    def upload(){
      def resourcesInfo = [];    
      def rootDir =  grailsApplication.config.fileOps.resources.rootDir; 
      def folder = new File( rootDir )//folder location where images needs to be saved
        if( !folder.exists() ) {
          folder.mkdirs()//create folder if not exists
        }         
      request.getMultiFileMap().resources.each { f ->
       def name = Utils.generateSafeFileName(f.originalFilename);
        f.transferTo(new File(folder.toString()+"/"+name));//storing the file on the server
        resourcesInfo.add([size:f.size, url: folder.toString()+"/"+name,contentType:f.contentType, originalFilename:f.originalFilename]);
    }
        render resourcesInfo as JSON 

    }    
}



