package filesutra

import grails.converters.JSON



class FilesAPIController {

    def googleService
    def dropboxService
    def boxService
    def onedriveService
    def amazonService
    def facebookService
    def flickrService
    def picasaService
    String type = null;
    def size = null;



    def googleFiles(String folderId) {
        folderId = folderId ? folderId : "root"
        def items;
        if(folderId == "untitled"){
            items =  googleService.listItems("root", Access.get(session.googleAccessId))
        }else{
            items =  googleService.listItems(folderId, Access.get(session.googleAccessId))
        }
        def itemResponse = []
         if(folderId == "root"){
                itemResponse.push(["id":"untitled","type":"folder","name":"Not in folder"])
            }
        items.each {
            if(folderId == "root"){
                if(it.mimeType == "application/vnd.google-apps.folder"){
                    def mItem = new ApiResponse.Item()
                    mItem.id = it.id
                    mItem.type = "folder"
                    mItem.name = it.title
                    mItem.size = it.fileSize ? it.fileSize as long : null
                    itemResponse.push(mItem)
                }
            }else {
                if(it.fileExtension == "jpg" || it.fileExtension == "png" || it.fileExtension == "JPG" || it.fileExtension == "PNG"){
                    def mItem = new ApiResponse.Item()
                    mItem.id = it.id
                    mItem.type = "file"
                    mItem.name = it.title
                    mItem.thumbnail =it.thumbnailLink
                    mItem.iconurl =it.webContentLink
                    mItem.size = it.fileSize ? it.fileSize as long : null
                    mItem.mimetype = it.mimeType
                    itemResponse.push(mItem)
                }else if(it.mimeType == "application/vnd.google-apps.folder" && folderId != "untitled"){
                    def mItem = new ApiResponse.Item()
                    mItem.id = it.id
                    mItem.type = "folder"
                    mItem.name = it.title
                    mItem.size = it.fileSize ? it.fileSize as long : null
                    itemResponse.push(mItem)
                }
            }
        }
        render itemResponse as JSON
    }
    def facebookFiles(String folderId, String after) {
        def typeSize;
        folderId = folderId ? folderId : "facebook"
        def items =  facebookService.listItems(folderId, after, Access.get(session.facebookAccessId))
        def itemResponse = []
        items.data.each {
            def mItem = new ApiResponse.Item();
            mItem.id = it.id
            if(folderId == "facebook"){
                mItem.type = "folder"
                mItem.name = it.name
            }else{
                mItem.type = "file"
                if(it.name){
                    mItem.name = it.name
                }else{
                    mItem.name = it.id
                }
                mItem.iconurl = it.images[0]['source']
                typeSize = getMimeType(mItem.iconurl)
                mItem.mimetype = typeSize[0]
                mItem.size = typeSize[1]
            }
            itemResponse.push(mItem)
        }
        render(contentType: 'text/json') {
            [listresponse:itemResponse,afterval:items.paging.cursors.after]
        }
    }

   def flickrFiles(String folderId, String after) {
        def typeSize ;
        folderId = folderId ? folderId : "flickr"
        def items =  flickrService.listItems(folderId, after, Access.get(session.flickrAccessId))
        def itemResponse = []
        if(folderId == "flickr"){
            itemResponse.push(["id":"untitled","type":"folder","name":"Not in folder"])
            items.photoset.each {
                def mItem = new ApiResponse.Item();
                mItem.id = it.id
                mItem.type = "folder"
                mItem.name = it.title["_content"]
                itemResponse.push(mItem)
            }
            render(contentType: 'text/json') {
                [listresponse:itemResponse,afterval:""]
            }
        }else {
            items.photo.each {
                def mItem = new ApiResponse.Item();
                mItem.id = it.id
                mItem.type = "file"
                mItem.name = it.title
                mItem.iconurl = it.url_m
                typeSize = getMimeType(mItem.iconurl)
                mItem.size = typeSize[1]
                mItem.mimetype = typeSize[0]
                itemResponse.push(mItem)
            }
            render(contentType: 'text/json') {
                [listresponse:itemResponse,afterval:items.page]
            }
        }
    
    }
    def picasaFiles(String folderId) {
        folderId = folderId ? folderId : "picasa"
        def items =  picasaService.listItems(folderId, Access.get(session.picasaAccessId))
        def itemResponse = []
        items.each {
            def mItem = new ApiResponse.Item();
            mItem.id = it.gphoto$id.$t
            if(folderId == "picasa"){
                mItem.type = "folder"
                mItem.name = it.title.$t
            }else{
                mItem.type = "file"
                mItem.name = it.title.$t
                mItem.iconurl = it.media$group.media$content[0].url
                mItem.size = it.gphoto$size.$t ? it.gphoto$size.$t as long : null
                mItem.mimetype = it.media$group.media$content[0].type
            }
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
        File file = new File(fileId: input.fileId, type: "GOOGLE", access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                filename: file.name,
                url: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size,
                mimetype: input.mimetype
        ]
        render fileResponse as JSON
    }

    def importFacebookFile() {
        def input = request.JSON
        Access access = Access.get(session.facebookAccessId)
        File file = new File(fileId: input.fileId, type: "FACEBOOK", access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                filename: file.name,
                url: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size,
                mimetype: input.mimetype
        ]
        render fileResponse as JSON
    }

     def importFlickrFile() {
        def input = request.JSON
        Access access = Access.get(session.flickrAccessId)
        File file = new File(fileId: input.fileId, type: "FLICKR", access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                filename: file.name,
                url: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size,
                mimetype: input.mimetype
        ]
        render fileResponse as JSON
    }

     def importPicasaFile() {
        def input = request.JSON
        Access access = Access.get(session.picasaAccessId)
        File file = new File(fileId: input.fileId, type: "PICASA", access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                filename: file.name,
                url: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size,
                mimetype: input.mimetype
        ]
        render fileResponse as JSON
    }


    def importDropboxFile() {
        def input = request.JSON
        Access access = Access.get(session.dropboxAccessId)
        File file = new File(fileId: input.fileId, type: "DROPBOX", access: access,
                name: input.fileName, size: input.size)
        file.localFileId = Utils.randomString(15)
        file.save(flush: true, failOnError: true)
        def fileResponse = [
                filename: file.name,
                url: request.isSecure() ? "https://" : "http://" +
                        request.serverName + (request.serverPort && request.serverPort != 80 ? ":$request.serverPort" : "") +
                        "/download/$file.localFileId",
                size: file.size,
                mimetype: input.mimetype
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
    def getMimeType(fileLink){
        URL u = new URL(fileLink);
        URLConnection uc = null;
        uc = u.openConnection();
        type = uc.getContentType();
        size = uc.getContentLength();
        return [type, size]
    }

}
