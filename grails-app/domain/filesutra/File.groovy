package filesutra

class File {

    String type
    String name
    String fileId // fileId of the corresponding storage service
    String localFileId
    Long size

    Access access

    static constraints = {
        size    nullable: true
    }
}
