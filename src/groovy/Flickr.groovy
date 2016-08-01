package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient
import java.security.MessageDigest
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject;



import static groovyx.net.http.ContentType.URLENC

/**
 * Created by kartthik on 19/05/16.
 */
class Flickr {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.flickr.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.flickr.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.flickr.REDIRECT_URI

    private static final String AUTH_URL = "https://flickr.com/services/auth"
    private static final String API_URL = "https://api.flickr.com"

    static def getLoginUrl() {
        def sig = getSignature();
        def params = [
                api_key    : CLIENT_ID,
                perms        : "read",
                api_sig      :sig
        ]
        def url = "$AUTH_URL?" + params.collect { k, v -> "$k=$v" }.join('&')
        return url
    }
    
    static def exchangeCode(String code) {
         def frobSig = getFrobSignature(code);
         def restClient = new RESTClient(API_URL)
         def resp = restClient.get(path: "/services/rest", params : [method: "flickr.auth.getToken",api_key: CLIENT_ID, frob: code, api_sig: frobSig])
         String val = resp.data;
         val = val.substring(0, val.length() - 5);
        return [
                accessToken: val,
                //refreshToken: resp.data.refresh_token
        ]
    }


    private static String getSignature(){
        String claimedContent = CLIENT_SECRET+"api_key"+CLIENT_ID+"permsread";
        String hashFromContent = claimedContent.encodeAsMD5();
        return hashFromContent;
    }
     private static String getFrobSignature(String frobVal){
        String claimedContent = CLIENT_SECRET+"api_key"+CLIENT_ID+"frob"+frobVal+"methodflickr.auth.getToken";
        String hashFromContent = claimedContent.encodeAsMD5();
        return hashFromContent;
    }

    private static String getTokenSignature(String authToken, String method){
        String claimedContent = CLIENT_SECRET+"api_key"+CLIENT_ID+"auth_token"+authToken+"method"+method;
        String hashFromContent = claimedContent.encodeAsMD5();
        return hashFromContent;
    }


    static def getEmailId(String accessToken) {
        def frobSig = getTokenSignature(accessToken,"flickr.test.login");
        def restClient = new RESTClient(API_URL)
        def resp = restClient.get(path: "/services/rest", params : [method: "flickr.test.login",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig])
        String name = resp.data.user;      
       
        return name;
    }

    /*static def listItems(String folderId, String accessToken) {
        String newValue = accessToken+"extrasurl_mformatjson"
        def frobSig = getTokenSignature(newValue,"flickr.people.getPhotosnojsoncallback1per_page500user_idme");
        def restClient = new RESTClient(API_URL)
        def resp = restClient.get(path: "/services/rest/", params : [method: "flickr.people.getPhotos",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig,user_id:"me", format:"json",nojsoncallback:"1",extras:"url_m",per_page:"500"])
        return resp.data.photos.photo
    }*/
    static def listItems(String folderId, String after, String accessToken) {
        
        def restClient = new RESTClient(API_URL)
        if(folderId == "flickr"){
             String newValue = accessToken+"formatjson"
             def frobSig = getTokenSignature(newValue,"flickr.photosets.getListnojsoncallback1");
             def resp = restClient.get(path: "/services/rest/", params : [method: "flickr.photosets.getList",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig,format:"json",nojsoncallback:"1"])

             return resp.data.photosets
        }else if(folderId == "untitled"){
            def pageNumber;
            if(after!=''){
                pageNumber = ++after
            }else{
                pageNumber = 1;
            }
            String newValue = accessToken+"extrasurl_mformatjson"
            def frobSig = getTokenSignature(newValue,"flickr.photos.getNotInSetnojsoncallback1page"+pageNumber+"per_page25photoset_id"+folderId+"privacy_filter%5B1%2C2%2C3%2C4%2C5%5D");

            def resp = restClient.get(path: "/services/rest/", params : [method: "flickr.photos.getNotInSet",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig, format:"json",nojsoncallback:"1",extras:"url_m", page:pageNumber, per_page:"25", photoset_id:folderId, privacy_filter:"%5B1%2C2%2C3%2C4%2C5%5D"])
            return resp.data.photos
        }else{
            def pageNumber;
            if(after!=''){
                pageNumber = ++after
            }else{
                pageNumber = 1;
            }
            String newValue = accessToken+"extrasurl_mformatjson"
            def frobSig = getTokenSignature(newValue,"flickr.photosets.getPhotosnojsoncallback1page"+pageNumber+"per_page25photoset_id"+folderId+"privacy_filter%5B1%2C2%2C3%2C4%2C5%5D");

            def resp = restClient.get(path: "/services/rest/", params : [method: "flickr.photosets.getPhotos",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig, format:"json",nojsoncallback:"1",extras:"url_m", page:pageNumber, per_page:"25", photoset_id:folderId, privacy_filter:"%5B1%2C2%2C3%2C4%2C5%5D"])
            return resp.data.photoset
        }
    }

    static def getFile(String fileId, String accessToken) {
        String newValu = accessToken+"formatjson"
        String newMethod = "flickr.photos.getSizesnojsoncallback1photo_id"+fileId
        def frobSig = getTokenSignature(newValu,newMethod);
        def restClient = new RESTClient(API_URL)
        def resp = restClient.get(path: "/services/rest/", params : [method: "flickr.photos.getSizes",api_key: CLIENT_ID, auth_token: accessToken, api_sig: frobSig, format:"json",nojsoncallback:"1",photo_id:fileId])
        return resp.data.sizes.size
    }

    static URLConnection getDownloadUrlConnection(String fileId, String accessToken) {
        def file = getFile(fileId, accessToken)
        String contentUrl
       file.each {
            if(it.label == "Original"){
               contentUrl = it.source;
            }
        }
        URL url = new URL(contentUrl)
        URLConnection connection = url.openConnection();
        connection.connect()
        return connection
    }


    static def refreshToken(String refreshToken) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/v2.6/oauth/access_token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       refresh_token: refreshToken, grant_type: 'refresh_token'],
                requestContentType: URLENC)
        return resp.data.access_token
    }
}
