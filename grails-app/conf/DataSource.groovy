dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = org.hibernate.dialect.PostgreSQL9Dialect
    url = "jdbc:postgresql://localhost:5432/cfilesdb"
    // Moved to external config file
    username = ''
    password = ''

    dbCreate = "" // one of 'create', 'create-drop', 'update', 'validate', ''
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    reload = false
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
        dataSource {
            //dbCreate = "" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:postgresql://localhost:5432/eltropydb"
        }
    }
    test {
        dataSource {
            //dbCreate = "" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:postgresql://localhost:5432/eltropydb"
        }
    }
    production {
        dataSource {
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }

            //dbCreate = "" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:postgresql://localhost:5432/eltropydb"
        }
    }
}