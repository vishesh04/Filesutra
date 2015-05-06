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
        authService.googleLogin(emailId, accessInfo)
        def resp = [
                success : true
        ]
        render resp as JSON
    }

    def dropboxCallback() {

    }

    def boxCallback() {

    }

    def onedriveCallback() {

    }
}
