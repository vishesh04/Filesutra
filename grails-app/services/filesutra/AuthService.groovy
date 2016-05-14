package filesutra

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class AuthService {

    Access googleLogin(emailId, accessInfo) {
        // save google info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.GOOGLE)
        if (!access) {
            access = new Access(type: StorageType.GOOGLE, emailId: emailId)
        } else{
            // refresh token is returned only on first login
            accessInfo.refreshToken = JSON.parse(access.accessInfo).refreshToken
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }

    Access facebookLogin(emailId, accessInfo) {
        // save google info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.FACEBOOK)
        if (!access) {
            access = new Access(type: StorageType.FACEBOOK, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }

    Access dropboxLogin(emailId, accessInfo) {
        // save dropbox info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.DROPBOX)
        if (!access) {
            access = new Access(type: StorageType.DROPBOX, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }

    Access boxLogin(emailId, accessInfo) {
        // save box info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.BOX)
        if (!access) {
            access = new Access(type: StorageType.BOX, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }

    Access onedriveLogin(emailId, accessInfo) {
        // save onedrive info
        Access access = Access.findByEmailIdAndType(emailId, StorageType.ONEDRIVE)
        if (!access) {
            access = new Access(type: StorageType.ONEDRIVE, emailId: emailId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }

    Access amazonLogin(rootFolderId, accessInfo) {
        // save amazon login info
        Access access = Access.findByEmailIdAndType(rootFolderId, StorageType.ONEDRIVE)
        if (!access) {
            access = new Access(type: StorageType.AMAZON_CLOUD_DRIVE, emailId: rootFolderId)
        }
        access.accessInfo = Utils.jsonToString(accessInfo)
        access.save(flush: true, failOnError: true)
        return access
    }
}
