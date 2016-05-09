package filesutra

class File {

    String type
    String name
    String fileId // fileId of the corresponding storage service
    String localFileId
    Long size

    Access access

    Date dateCreated
    Date lastUpdated

    static constraints = {
        dateCreated nullable: true
        lastUpdated nullable: true
        size        nullable: true
    }
}
