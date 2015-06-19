package filesutra

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

    static def randomString(int length) {
        return org.apache.commons.lang.RandomStringUtils.random(length, true, true)
    }
}
