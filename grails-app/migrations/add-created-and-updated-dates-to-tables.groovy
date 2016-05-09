databaseChangeLog = {

    changeSet(author: "vishesh (generated)", id: "1462776278609-1") {
        addColumn(tableName: "access") {
            column(name: "date_created", type: "timestamp")
        }
    }

    changeSet(author: "vishesh (generated)", id: "1462776278609-2") {
        addColumn(tableName: "access") {
            column(name: "last_updated", type: "timestamp")
        }
    }

    changeSet(author: "vishesh (generated)", id: "1462776278609-3") {
        addColumn(tableName: "file") {
            column(name: "date_created", type: "timestamp")
        }
    }

    changeSet(author: "vishesh (generated)", id: "1462776278609-4") {
        addColumn(tableName: "file") {
            column(name: "last_updated", type: "timestamp")
        }
    }

    changeSet(author: "vishesh (generated)", id: "1462776278609-5") {
        addNotNullConstraint(columnDataType: "varchar(255)", columnName: "email_id", tableName: "access")
    }
}
