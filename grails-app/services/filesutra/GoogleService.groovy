package filesutra

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class GoogleService {

    def callAPI(Closure c, Access access) {
        def accessInfo = JSON.parse(access.accessInfo)
        try {
            c(accessInfo.accessToken)
        } catch (e) {
            if (e.hasProperty("response") && e.response?.status == 401) {
                accessInfo.accessToken = Google.refreshToken(accessInfo.refreshToken)
                access.accessInfo = Utils.jsonToString(accessInfo)
                access.save(flush: true, failOnError: true)
                c(accessInfo.accessToken)
            } else {
                throw e
            }
        }
    }

    def listItems(String folderId, Access access) {
        callAPI({ accessToken->
            return Google.listItems(folderId, accessToken)
        }, access)
    }

    URLConnection getDownloadUrlConnection(String fileId, Access access) {
        callAPI({ accessToken ->
            return Google.getDownloadUrlConnection(fileId, accessToken)
        }, access)
    }
}
