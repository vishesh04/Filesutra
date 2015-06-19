package filesutra

import grails.converters.JSON

class FilesAPIController {

    def googleService
    def dropboxService
    def boxService
    def onedriveService

    def googleFiles(String folderId) {
        folderId = folderId ? folderId : "root"
        def items =  googleService.listItems(folderId, Access.get(session.googleAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.id = it.id
            mItem.type = it.mimeType == "application/vnd.google-apps.folder" ? "folder" : "file"
            mItem.name = it.title
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def dropboxFiles(String folderId) {
        folderId = folderId ? "dropbox" + folderId : "dropbox"
        def items =  dropboxService.listItems(folderId, Access.get(session.dropboxAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.id = URLEncoder.encode(it.path, "UTF-8")
            mItem.type = it.is_dir == true ? "folder" : "file"
            mItem.name = it.path.substring(it.path.lastIndexOf("/")+1)
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def boxFiles(Long folderId) {
        folderId = folderId ? folderId : 0
        def items =  boxService.listItems(folderId, Access.get(session.boxAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.id = it.id
            mItem.type = it.type
            mItem.name = it.name
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def onedriveFiles(String folderId) {
        folderId = folderId ? folderId : "me/skydrive"
        def items =  onedriveService.listItems(folderId, Access.get(session.onedriveAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.id = it.id
            mItem.type = it.type
            mItem.name = it.name
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def importGoogleFile(String fileId, String fileName) {
        def input = request.JSON
        Access access = Access.get(session.googleAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.GOOGLE, access: access, name: input.fileName)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                downloadUrl: request.serverName + (request.serverPort ? ":$request.serverPort" : "") + "/download/$file.id"
                //TODO: map file.id to non guessable random string
        ]
        render fileResponse as JSON
    }
}
