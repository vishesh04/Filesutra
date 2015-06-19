databaseChangeLog = {

	changeSet(author: "vishesh (generated)", id: "1434717181803-1") {
		addColumn(tableName: "file") {
			column(name: "size", type: "int8") {
			}
		}
	}
}
