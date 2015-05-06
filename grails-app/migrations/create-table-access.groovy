databaseChangeLog = {

	changeSet(author: "vishesh (generated)", id: "1430918390761-1") {
		createTable(tableName: "access") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "accessPK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "access_info", type: "text") {
				constraints(nullable: "false")
			}

			column(name: "email_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "vishesh (generated)", id: "1430918390761-2") {
		createSequence(sequenceName: "hibernate_sequence")
	}
}
