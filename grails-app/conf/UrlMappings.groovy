class UrlMappings {

    static mappings = {

        "/api/files/google"     (controller: "FilesAPI", action: "googleFiles")
        "/api/files/dropbox"    (controller: "FilesAPI", action: "dropboxFiles")
        "/api/files/box"        (controller: "FilesAPI", action: "boxFiles")
        "/api/files/onedrive"   (controller: "FilesAPI", action: "onedriveFiles")

        "/api/import/google"        (controller: "FilesAPI", action: "importGoogleFile")
        "/api/import/box"           (controller: "FilesAPI", action: "importBoxFile")
        "/api/import/dropbox"       (controller: "FilesAPI", action: "importDropboxFile")
        "/api/import/onedrive"      (controller: "FilesAPI", action: "importOnedriveFile")

        "/download/$id"         (controller: "File", action: "downloadFile")

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'Demo')
        "500"(view: '/error')
    }
}
