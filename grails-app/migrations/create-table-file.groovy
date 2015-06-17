databaseChangeLog = {

	changeSet(author: "vishesh (generated)", id: "1430979143110-1") {
		createTable(tableName: "file") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "filePK")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "access_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "file_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "varchar(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "vishesh (generated)", id: "1430979143110-2") {
		addForeignKeyConstraint(baseColumnNames: "access_id", baseTableName: "file", constraintName: "FK_feo087vbdqmnf1uw0l2e27rtt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "access", referencesUniqueColumn: "false")
	}
}
