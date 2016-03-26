package filesutra

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class AmazonService {

    def callAPI(Closure c, Access access) {
        def accessInfo = JSON.parse(access.accessInfo)
        try {
            c(accessInfo.accessToken)
        } catch (e) {
            if (e.hasProperty("response") && e.response?.status == 401) {
                accessInfo.accessToken = AmazonCloudDrive.refreshToken(accessInfo.refreshToken).accessToken
                access.accessInfo = Utils.jsonToString(accessInfo)
                access.save(flush: true, failOnError: true)
                c(accessInfo.accessToken)
            } else {
                throw e
            }
        }
    }

    def getUserEndpoints(Access access) {
        callAPI({ accessToken->
            return AmazonCloudDrive.getUserEndpoints(accessToken)
        }, access)
    }

    def listItems(String folderId, Access access, String endpoint) {
        callAPI({ accessToken->
            return AmazonCloudDrive.listItems(endpoint, folderId, accessToken)
        }, access)
    }

    URLConnection getDownloadUrlConnection(String fileId, Access access, String endpoint) {
        callAPI({ accessToken ->
            if (!endpoint) {
                endpoint = getUserEndpoints(access).contentUrl
            }
            return AmazonCloudDrive.getDownloadUrlConnection(endpoint, fileId, accessToken)
        }, access)
    }
}
