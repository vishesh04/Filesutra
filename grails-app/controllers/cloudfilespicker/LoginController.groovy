package cloudfilespicker

import grails.converters.JSON

class LoginController {

    def authService

    def google() {
        redirect(url: Google.getLoginUrl())
    }

    def dropbox() {

    }

    def box() {

    }

    def onedrive() {

    }

    def googleCallback(String code) {
        def accessInfo = Google.exchangeCode(code)
        def emailId = Google.getEmailId(accessInfo.accessToken)
        Access googleAccess = authService.googleLogin(emailId, accessInfo)
        if (googleAccess) {
            session.googleAccessId = googleAccess.id
        }
        redirect(uri: '/picker')
    }

    def dropboxCallback() {

    }

    def boxCallback() {

    }

    def onedriveCallback() {

    }
}
