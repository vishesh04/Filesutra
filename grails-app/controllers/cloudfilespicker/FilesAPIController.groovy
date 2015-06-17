package cloudfilespicker

import grails.converters.JSON

class FilesAPIController {

    def googleService
    def dropboxService
    def boxService
    def onedriveService

    def googleFiles() {
        def items =  googleService.listItems("root", Access.get(session.googleAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.type = it.kind == "drive#file" ? "file" : "folder"
            mItem.name = it.title
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def dropboxFiles() {
        def items =  dropboxService.listItems("dropbox", Access.get(session.dropboxAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.type = it.is_dir == true ? "folder" : "file"
            mItem.name = it.path.substring(it.path.lastIndexOf("/")+1)
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def boxFiles() {
        def items =  onedriveService.listItems(0, Access.get(session.boxAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.type = it.type
            mItem.name = it.name
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def onedriveFiles() {
        def items =  onedriveService.listItems("me/skydrive", Access.get(session.onedriveAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.type = it.type
            mItem.name = it.name
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
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
