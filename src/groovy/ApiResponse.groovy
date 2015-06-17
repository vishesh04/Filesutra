package cloudfilespicker

/**
 * Created by vishesh on 17/06/15.
 */
class ApiResponse {
    static class Item {
        String name
        String type
        List<ApiResponse.Item> content
    }
}
