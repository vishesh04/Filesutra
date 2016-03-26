package filesutra

/**
 * Created by vishesh on 25/03/16.
 */
enum AmazonCloudDriveAPIType {
    NODE, METADATA

    public String toString() {
        switch(this) {
            case NODE: return "contentUrl";
            case METADATA: return "metatdaUrl";
            default: throw new IllegalArgumentException();
        }
    }
}