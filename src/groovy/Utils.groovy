package filesutra

import groovy.json.JsonBuilder

/**
 * Created by vishesh on 06/05/15.
 */
class Utils {

	private static final Random FILE_NAME_GENEROTR = new Random();

    static def jsonToString(json) {
        def builder = new JsonBuilder()
        builder(json)
        return builder.toString()
    }

    static def randomString(int length) {
        return org.apache.commons.lang.RandomStringUtils.random(length, true, true)
    }

    static String generateSafeFileName(String name) {
		//returning random integer (between 1-1000) as file name along with original extension
        return "" + (FILE_NAME_GENEROTR.nextInt(1000-1+1)+1) + getCleanFileExtension(name) 
	}

    public static getCleanFileExtension(String fileName){
		String extension = ""
		fileName = fileName?.trim()
		if(!fileName || fileName == "")
			return extension
		
		int beginIndex = fileName.lastIndexOf(".")
		extension = (beginIndex > -1 && beginIndex+1 != fileName.size()) ? fileName.substring(beginIndex) : ""
        if(extension.size() > 5) extension = "";
		return extension
	}
}
