databaseChangeLog = {

	changeSet(author: "vishesh (generated)", id: "1434709985053-1") {
		addColumn(tableName: "file") {
			column(name: "local_file_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}
}
