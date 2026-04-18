package pam.tugas5.romadhon.database

object DatabaseProvider {
    private var database: NotesDatabase? = null

    fun getDatabase(driverFactory: DatabaseDriverFactory): NotesDatabase {
        return database ?: NotesDatabase(driverFactory.createDriver()).also { database = it }
    }
}