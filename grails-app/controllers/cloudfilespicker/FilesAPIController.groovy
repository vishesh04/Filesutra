package cloudfilespicker

import grails.converters.JSON

class FilesAPIController {

    def googleService

    def googleFiles() {
        render googleService.getContent("root", Access.get(2)) as JSON
    }
}
