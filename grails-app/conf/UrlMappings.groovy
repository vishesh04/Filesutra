class UrlMappings {

    static mappings = {

        "/api/files/google" (controller: "FilesAPI", action: "googleFiles")

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'Demo')
        "500"(view: '/error')
    }
}
