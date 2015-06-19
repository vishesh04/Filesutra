package filesutra

import grails.util.Holders
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by vishesh on 06/05/15.
 */
class Onedrive {

    def static grailsApplication = Holders.getGrailsApplication()

    private static final String CLIENT_ID     = grailsApplication.config.auth.onedrive.CLIENT_ID
    private static final String CLIENT_SECRET = grailsApplication.config.auth.onedrive.CLIENT_SECRET
    private static final String REDIRECT_URI = grailsApplication.config.auth.onedrive.REDIRECT_URI

    private static final String AUTH_URL = "https://login.live.com"
    private static final String API_URL = "https://apis.live.net"

    static def getLoginUrl() {
        def params = [
                response_type: "code",
                client_id    : CLIENT_ID,
                redirect_uri : REDIRECT_URI,
                scope        : "wl.signin wl.emails wl.offline_access wl.skydrive"
        ]
        def url = "$AUTH_URL/oauth20_authorize.srf?" + params.collect { k, v -> "$k=$v" }.join('&')
        return url
    }

    static def exchangeCode(String code) {
        def restClient = new RESTClient(AUTH_URL)
        def resp = restClient.post(
                path: '/oauth20_token.srf',
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
        def user =  restClient.get(
                path: '/v5.0/me',
                query: [access_token: accessToken],
                requestContentType: URLENC)
        return user.data.emails?.preferred
    }

    static def listItems(folderId, String accessToken) {
        def restClient = new RESTClient(API_URL)
        restClient.headers.Authorization = "Bearer $accessToken"
        def resp = restClient.get(path: "/v5.0/$folderId/files")
        return resp.data.data
    }

    static def refreshToken(String refreshToken) {
        def restClient = new RESTClient(AUTH_URL)
        def resp = restClient.post(
                path: '/oauth20_token.srf',
                body: [client_id   : CLIENT_ID, client_secret: CLIENT_SECRET,
                       refresh_token: refreshToken, grant_type: 'refresh_token'],
                requestContentType: URLENC)
        return [
                accessToken: resp.data.access_token,
                refreshToken: resp.data.refresh_token
        ]
    }
}
