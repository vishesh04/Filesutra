databaseChangeLog = {
    changeSet(author: "vishesh (manual)", id: "1434960092-1") {
        dropNotNullConstraint(columnDataType: "int8", columnName: "size", tableName: "file")
    }
}
