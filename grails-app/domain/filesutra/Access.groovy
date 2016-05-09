package filesutra

class Access {

    String emailId
    String type
    String accessInfo

    Date dateCreated
    Date lastUpdated

    static constraints = {
        dateCreated nullable: true
        lastUpdated nullable: true
    }

    static mapping = {
        accessInfo type: "text"
    }
}
