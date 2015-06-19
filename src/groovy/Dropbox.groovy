package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by vishesh on 06/05/15.
 */
class Dropbox {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.dropbox.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.dropbox.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.dropbox.REDIRECT_URI

    private static final String AUTH_URL = "https://www.dropbox.com/1"
    private static final String API_URL = "https://api.dropbox.com"
    private static final String DOWNLOAD_URL = "https://api-content.dropbox.com/1/files/auto"

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
                path: '/1/oauth2/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       redirect_uri: REDIRECT_URI, code: code, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token/*,
                refreshToken: resp.data.refresh_token*/
        ]
    }

    public static def getEmailId(String accessToken) {
        def restClient = new RESTClient(API_URL)
        restClient.headers."Authorization" = "Bearer $accessToken"
        def resp = restClient.get(path: '/1/account/info')
        return resp.data.email
    }

    static def listItems(String folderId, String accessToken) {
        def restClient = new RESTClient(API_URL)
        restClient.headers.Authorization = "Bearer $accessToken"
        def resp = restClient.get(path: "/1/metadata/$folderId")
        return resp.data.contents
    }

    static URLConnection getDownloadUrlConnection(String fileId, String accessToken) {
        URL url = new URL(DOWNLOAD_URL+fileId)
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
                       refresh_token: refreshToken, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return resp.data.access_token
    }
}
