package filesutra

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class DropboxService {

    def callAPI(Closure c, Access access) {
        def accessInfo = JSON.parse(access.accessInfo)
        c(accessInfo.accessToken)

    }

    def listItems(String folderId, Access access) {
        callAPI({ accessToken->
            return Dropbox.listItems(folderId, accessToken)
        }, access)
    }
}
