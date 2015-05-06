package cloudfilespicker

import grails.transaction.Transactional

@Transactional
class AuthService {

    def googleLogin(emailId, accessInfo) {
        // save google info
        Access access = Access.findByEmailIdAndType(emailId, AccessType.GOOGLE)
        if (!access) {
            access = new Access(type: AccessType.GOOGLE, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
    }
}
