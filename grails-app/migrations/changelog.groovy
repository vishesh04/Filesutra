databaseChangeLog = {

    include file: 'create-table-access.groovy'

	include file: 'create-table-file.groovy'

	include file: 'add-local-file-id-to-file.groovy'

	include file: 'add-size-to-file.groovy'

	include file: 'make-size-nullable-in-file.groovy'
}
