package filesutra

class Access {

    String emailId
    String type
    String accessInfo

    static constraints = {
    }

    static mapping = {
        accessInfo type: "text"
    }
}
