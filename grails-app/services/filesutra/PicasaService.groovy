package filesutra

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class PicasaService {

    def callAPI(Closure c, Access access) {
        def accessInfo = JSON.parse(access.accessInfo)
            c(accessInfo.accessToken)
       
    }

    def listItems(String folderId, Access access) {
        callAPI({ accessToken->
            return Picasa.listItems(folderId, accessToken)
        }, access)
    }

    def getDownloadUrlConnection(String fileId, Access access) {
        callAPI({ accessToken ->
            return Picasa.getDownloadUrlConnection(fileId, accessToken)
        }, access)
    }
}
