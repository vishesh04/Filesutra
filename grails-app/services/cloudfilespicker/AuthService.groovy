package cloudfilespicker

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.util.WebUtils

@Transactional
class AuthService {

    Access googleLogin(emailId, accessInfo) {
        // save google info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.GOOGLE)
        if (!access) {
            access = new Access(type: StorageType.GOOGLE, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }
}
