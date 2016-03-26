package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by vishesh on 03/25/16.
 */
class AmazonCloudDrive {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.amazon.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.amazon.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.amazon.REDIRECT_URI

    private static final String AUTH_URL = "https://www.amazon.com/ap/oa"
    private static final String API_URL = "https://api.amazon.com"
    private static final String ENDPOINT_API_URL = "https://drive.amazonaws.com"

    static def getLoginUrl() {
        def params = [
                response_type: "code",
                client_id    : CLIENT_ID,
                redirect_uri : REDIRECT_URI,
                scope        : "clouddrive:read_all"
        ]
        def url = "$AUTH_URL?" + params.collect { k, v -> "$k=$v" }.join('&')
        return url
    }

    static def exchangeCode(String code) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/auth/o2/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       redirect_uri: REDIRECT_URI, code: code, grant_type: 'authorization_code'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }

    static def refreshToken(String refreshToken) {
        def restClient = new RESTClient(API_URL)
        def resp = restClient.post(
                path: '/auth/o2/token',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       refresh_token: refreshToken, grant_type: 'refresh_token'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }

    static def getUserEndpoints(String accessToken) {
        def restClient = new RESTClient(ENDPOINT_API_URL)
        restClient.headers.Authorization = "Bearer $accessToken"
        def endpoints =  restClient.get(
                path: '/drive/v1/account/endpoint',
                query: [access_token: accessToken],
                requestContentType: URLENC)
        return endpoints.data
    }

    static def getUserInfo(String endpoint, String accessToken) {
        URL url = new URL(endpoint)
        def restClient = new RESTClient(url.protocol + "://" + url.host)
        restClient.headers.Authorization = "Bearer $accessToken"
        def user =  restClient.get(
                path: "$url.path/account/info",
                requestContentType: URLENC)
        return user.data
    }

    static def getRootFolderId(String endpoint, String accessToken) {
        URL url = new URL(endpoint)
        def restClient = new RESTClient(url.protocol + "://" + url.host)
        restClient.headers.Authorization = "Bearer $accessToken"
        def rootFolder =  restClient.get(
                path: "$url.path/nodes",
                query: [filters: "isRoot:true"],
                requestContentType: URLENC)
        return rootFolder.data.data[0].id
    }

    static def listItems(String endpoint, folderId, String accessToken) {
        URL url = new URL(endpoint)
        def restClient = new RESTClient(url.protocol + "://" + url.host)
        restClient.headers.Authorization = "Bearer $accessToken"
        def resp = restClient.get(path: "${url.path}nodes/$folderId/children")
        return resp.data.data
    }

    static URLConnection getDownloadUrlConnection(String endpoint, String fileId, String accessToken) {
        URL url = new URL(endpoint+"nodes/$fileId/content")
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Authorization", 'Bearer ' + accessToken);
        connection.connect()
        return connection
    }
}
