package filesutra

class FileController {

    def googleService

    def downloadFile(Long id) {
        File file = File.get(id)
        switch ((StorageType) file.type) {
            case StorageType.GOOGLE:
                def connection = googleService.getDownloadUrlConnection(file.fileId, file.access)
                response.setHeader "Content-Type", connection.getHeaderField("Content-Type")
                response.setHeader "Content-disposition", "attachment; filename=$file.name"
                response.outputStream << connection.inputStream
                response.outputStream.flush()
                break
            case StorageType.BOX:
                break
            default:
                break
        }
    }
}
