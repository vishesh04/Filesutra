package filesutra

import grails.converters.JSON

class FilesAPIController {

    def googleService
    def dropboxService
    def boxService
    def onedriveService
    def amazonService

    def googleFiles(String folderId) {
        folderId = folderId ? folderId : "root"
        def items =  googleService.listItems(folderId, Access.get(session.googleAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item()
            mItem.id = it.id
            mItem.type = it.mimeType == "application/vnd.google-apps.folder" ? "folder" : "file"
            mItem.name = it.title
            mItem.size = it.fileSize ? it.fileSize as long : null
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
            mItem.size = it.bytes ? it.bytes as long : null
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
            mItem.size = it.size ? it.size as long : null
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
            mItem.type = it.type in ['folder', 'album'] ? 'folder' : 'file'
            mItem.name = it.name
            mItem.size = it.size  ? it.size as long : null
            itemResponse.push(mItem)
        }
        render itemResponse as JSON
    }

    def amazonFiles(String folderId) {
        Access access = Access.get(session.amazonAccessId)
        folderId = folderId ? folderId : access.emailId
        def items =  amazonService.listItems(folderId, access, session[AmazonCloudDriveAPIType.METADATA.toString()])
        def itemResponse = []
        items.each {
            if (it.kind != "ASSET") {
                def mItem = new ApiResponse.Item()
                mItem.id = it.id
                mItem.type = it.kind == 'FOLDER' ? 'folder' : 'file'
                mItem.name = it.name
                mItem.size = it.contentProperties?.size  ? it.contentProperties?.size as long : null
                itemResponse.push(mItem)
            }
        }
        render itemResponse as JSON
    }

    def importGoogleFile() {
        def input = request.JSON
        Access access = Access.get(session.googleAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.GOOGLE, access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                fileName: file.name,
                downloadUrl: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size
        ]
        render fileResponse as JSON
    }

    def importBoxFile() {
        def input = request.JSON
        Access access = Access.get(session.boxAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.BOX, access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                fileName: file.name,
                downloadUrl: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size
        ]
        render fileResponse as JSON
    }

    def importDropboxFile() {
        def input = request.JSON
        Access access = Access.get(session.dropboxAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.DROPBOX, access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                fileName: file.name,
                downloadUrl: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size
        ]
        render fileResponse as JSON
    }

    def importOnedriveFile() {
        def input = request.JSON
        Access access = Access.get(session.onedriveAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.ONEDRIVE, access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                fileName: file.name,
                downloadUrl: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size
        ]
        render fileResponse as JSON
    }

    def importAmazonFile() {
        def input = request.JSON
        Access access = Access.get(session.amazonAccessId)
        File file = new File(fileId: input.fileId, type: StorageType.AMAZON_CLOUD_DRIVE, access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                fileName: file.name,
                downloadUrl: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size
        ]
        render fileResponse as JSON
    }
}
