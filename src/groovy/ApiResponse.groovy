package filesutra

/**
 * Created by vishesh on 17/06/15.
 */
class ApiResponse {
    static class Item {
        def id
        String name
        String type
        Long size
        def iconurl
        String mimetype
        def thumbnail
        List<ApiResponse.Item> content
    }
}
