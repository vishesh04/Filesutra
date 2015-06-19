package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by vishesh on 06/05/15.
 */
class Box {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.box.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.box.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.box.REDIRECT_URI

    private static final String AUTH_URL = "https://app.box.com/api"
    private static final String API_URL = "https://api.box.com"

    static def getLoginUrl() {
        def params = [
                response_type: "code",
                client_id    : CLIENT_ID,
                redirect_uri : REDIRECT_URI
        ]
        def url = "$AUTH_URL/oauth2/authorize?" + params.collect { k, v -> "$k=$v" }.join('&')
        return url
    }

    static def exchangeCode(String code) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/oauth2/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       redirect_uri: REDIRECT_URI, code: code, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }

    public static def getEmailId(String accessToken) {
        def restClient = new RESTClient(API_URL)
        restClient.headers."Authorization" = "Bearer $accessToken"
        def resp = restClient.get(path: '/2.0/users/me')
        return resp.data.login
    }

    static def listItems(folderId, String accessToken) {
        def restClient = new RESTClient(API_URL)
        restClient.headers.Authorization = "Bearer $accessToken"
        def resp = restClient.get(path: "/2.0/folders/$folderId/items")
        return resp.data.entries
    }

    static def refreshToken(String refreshToken) {
        //TODO: review
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/oauth2/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       refresh_token: refreshToken, grant_type: 'refresh_token'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }

    static URLConnection getDownloadUrlConnection(String fileId, String accessToken) {
        URL url = new URL(API_URL+"/2.0/files/$fileId/content")
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", 'Bearer ' + accessToken);
        connection.connect()
        return connection
    }
}
