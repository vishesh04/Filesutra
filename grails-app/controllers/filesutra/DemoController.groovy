package filesutra

import grails.converters.JSON

class DemoController {

    def index() {

    }

    // mix max integration
    def mixmaxEditor() {

    }

    def mixmaxResolver() {
        def input = JSON.parse(params.params)
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Origin", "https://compose.mixmax.com")
        println params.fileName
        def resp = [
                raw: true,
                body: "<a href='$input.downloadUrl'>$input.fileName</a>"
        ]
        render resp as JSON
    }
}
