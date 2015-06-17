package cloudfilespicker

import grails.converters.JSON

class FilesAPIController {

    def googleService

    def googleFiles() {
        render googleService.listItems("root", Access.get(session.googleAccessId)) as JSON
    }

    def pickGoogleFile(String googleFileId, String fileName) {
        Access access = Access.get(session.googleAccessId)
        File file = new File(fileId: googleFileId, type: StorageType.GOOGLE, access: access, name: fileName)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                id: file.id,
                downloadUrl: request.serverName + (request.serverPort ? ":$request.serverPort" : "") + "/File/downloadFile/$file.id"
        ]
        render fileResponse as JSON
    }
}
