package filesutra

import grails.converters.JSON

class PickerController {

    def importFiles() {
        def connectedApps = []
        if (session.googleAccessId) {
            connectedApps.push("Google")
        }
        if (session.boxAccessId) {
            connectedApps.push("Box")
        }
        if (session.dropboxAccessId) {
            connectedApps.push("Dropbox")
        }
        if (session.onedriveAccessId) {
            connectedApps.push("OneDrive")
        }
        if (session.amazonAccessId) {
            connectedApps.push("AmazonCloudDrive")
        }
        def appSettings = [
                connectedApps : connectedApps
        ]
        [appSettings: appSettings as JSON]
    }

}
