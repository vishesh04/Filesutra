package filesutra

class FileController {

    def googleService
    def boxService
    def dropboxService
    def onedriveService

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
                    connection = googleService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, response)
                    break
                case StorageType.BOX:
                    connection = boxService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, response)
                    break
                case StorageType.DROPBOX:
                    connection = dropboxService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, response)
                    break
                case StorageType.ONEDRIVE:
                    connection = onedriveService.getDownloadUrlConnection(file.fileId, file.access)
                    proxyUrlConnection(connection, file, response)
                default:
                    break
            }
        } else {
            // 404
            throw new Exception("File not found")
        }
    }

    private def proxyUrlConnection(URLConnection connection, File file, response) {
        response.setHeader "Content-Type", connection.getHeaderField("Content-Type")
        response.setHeader "Content-disposition", "attachment; filename=$file.name"
        if (file.size) {
            response.setHeader "Content-Length", "$file.size"
        }
        response.outputStream << connection.inputStream
        response.outputStream.flush()
    }
}
