class UrlMappings {

    static mappings = {

        "/api/files/google"     (controller: "FilesAPI", action: "googleFiles")
        "/api/files/dropbox"    (controller: "FilesAPI", action: "dropboxFiles")
        "/api/files/box"        (controller: "FilesAPI", action: "boxFiles")
        "/api/files/onedrive"   (controller: "FilesAPI", action: "onedriveFiles")

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'Demo')
        "500"(view: '/error')
    }
}
