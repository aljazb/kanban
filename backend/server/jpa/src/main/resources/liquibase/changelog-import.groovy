package liquibase

databaseChangeLog {

    include(file: 'changelogs.groovy', relativeToChangelogFile: true)

    changeSet(id: 'sql-1', author: 'Targma') {
        sql('')
    }
}
