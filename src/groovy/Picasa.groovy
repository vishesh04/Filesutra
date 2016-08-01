package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient
import groovy.json.JsonBuilder
import grails.converters.JSON;

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by Karthik on 20/05/16.
 */
class Picasa {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.picasa.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.picasa.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.picasa.REDIRECT_URI

    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth"
    private static final String API_URL = "https://www.googleapis.com"
    private static final String PIC_URL = "https://picasaweb.google.com"


    static def getLoginUrl() {
        def params = [
                response_type: "code",
                redirect_uri : REDIRECT_URI,
                client_id    : CLIENT_ID,                
                access_type  : "offline",
                scope        : "https://picasaweb.google.com/data",
                approval_prompt:"force"
        ]
        def url = "$AUTH_URL?" + params.collect { k, v -> "$k=$v" }.join('&')
        return url
    }

    static def exchangeCode(String code) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/oauth2/v3/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       redirect_uri: REDIRECT_URI, code: code, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }

    static def getEmailId(String accessToken) {
     
        return "Picasa_User"
    }

    static def listItems(String folderId, String accessToken) {
          def restClient = new RESTClient(PIC_URL)
          restClient.headers.Authorization = "Bearer $accessToken"
            if(folderId == "picasa"){
                def resp = restClient.get(path: '/data/feed/api/user/default',params:[alt:"json"])
                def json = new JsonBuilder(resp.data).toPrettyString()
                def value = JSON.parse(json)
                return value.feed.entry;
            }else{
                def pathVal = "/data/feed/api/user/default/albumid/"+folderId
                def resp = restClient.get(path: pathVal,params:[alt:"json"])
                def json = new JsonBuilder(resp.data).toPrettyString()
                def value = JSON.parse(json)
                return value.feed.entry;
            }
    }

    static def getFile(String fileId, String accessToken) {
         def restClient = new RESTClient(PIC_URL)
         restClient.headers.Authorization = "Bearer $accessToken"
         def resp = restClient.get(path: "/data/feed/api/user/default/photoid/$fileId",params:[alt:"json"])
         def json = new JsonBuilder(resp.data).toPrettyString()
         def value = JSON.parse(json)
        return value.feed.media$group.media$content[0].url;
    }

    

    static URLConnection getDownloadUrlConnection(String fileId, String accessToken) {
        def file = getFile(fileId, accessToken)
        URL url = new URL(file)
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", 'Bearer ' + accessToken);
        connection.connect()
        return connection
    }

    static def refreshToken(String refreshToken) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/oauth2/v3/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       refresh_token: refreshToken, grant_type: 'refresh_token'],
                requestContentType: URLENC)
        return resp.data.access_token
    }
}
