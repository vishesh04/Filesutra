package filesutra

class FileController {

    def googleService
    def boxService
    def dropboxService
    def onedriveService
    def amazonService
    def facebookService

    def downloadFile(String id) {
        def files = File.findAllByLocalFileId(id)
        if (files.size() > 1) {
            // Error - notify dev
            throw new Exception("Internal server error")
        } else if (files.size() == 1) {
            File file = files[0]
            def connection
            switch ((StorageType) file.type) {
                case StorageType.GOOGLE:
                    def googleFile = googleService.getDownloadUrlConnection(file.fileId, file.access)
                    def fileName = file.name
                    if (googleFile.extension) {
                        fileName += ".$googleFile.extension"
                    }
                    connection = googleFile.connection
                    proxyUrlConnection(connection, file, fileName, response)
                    break
                case StorageType.FACEBOOK:
                    connection = facebookService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, file.name, response)
                    break
                case StorageType.BOX:
                    connection = boxService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, file.name, response)
                    break
                case StorageType.DROPBOX:
                    connection = dropboxService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, file.name, response)
                    break
                case StorageType.ONEDRIVE:
                    connection = onedriveService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, file.name, response)
                case StorageType.AMAZON_CLOUD_DRIVE:
                    connection = amazonService.getDownloadUrlConnection(file.fileId, file.access, session[AmazonCloudDriveAPIType.NODE.toString()])
                    proxyUrlConnection(connection, file, file.name, response)
                default:
                    break
            }
        } else {
            // 404
            throw new Exception("File not found")
        }
    }

    private def proxyUrlConnection(URLConnection connection, File file, String fileName, response) {
        response.setHeader "Content-Type", connection.getHeaderField("Content-Type")
        response.setHeader "Content-disposition", "attachment; filename=$fileName"
        if (file.size) {
            response.setHeader "Content-Length", "$file.size"
        }
        response.outputStream << connection.inputStream
        response.outputStream.flush()
    }
}
