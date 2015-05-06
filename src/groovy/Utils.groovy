package cloudfilespicker

import groovy.json.JsonBuilder

/**
 * Created by vishesh on 06/05/15.
 */
class Utils {

    static def jsonToString(json) {
        def builder = new JsonBuilder()
        builder(json)
        return builder.toString()
    }
}
