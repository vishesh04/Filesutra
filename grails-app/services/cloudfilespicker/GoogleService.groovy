package cloudfilespicker

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class GoogleService {

    def callAPI(Closure c, Access access) {
        def accessInfo = JSON.parse(access.accessInfo)
        try {
            c(accessInfo.accessToken)
        } catch (e) {
            if (e.response?.status == 401) {
                accessInfo.accessToken = Google.refreshToken(access.refreshToken)
                access.accessInfo = Utils.jsonToString(accessInfo)
                access.save(flush: true, failOnError: true)
                c(accessInfo.accessToken)
            }
        }
    }

    def getContent(String folderId, Access access) {
        callAPI({ accessToken->
            return Google.getContent(folderId, accessToken)
        }, access)
    }
}
