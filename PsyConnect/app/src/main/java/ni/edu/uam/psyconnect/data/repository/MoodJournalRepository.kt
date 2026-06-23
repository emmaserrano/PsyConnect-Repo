package ni.edu.uam.psyconnect.data.moodjournal

class MoodJournalRepository(
    private val dao: MoodJournalDao
) {

    suspend fun insert(
        entry: MoodJournalEntry
    ) {
        dao.insert(entry)
    }

    suspend fun update(
        entry: MoodJournalEntry
    ) {
        dao.update(entry)
    }

    suspend fun delete(
        entry: MoodJournalEntry
    ) {
        dao.delete(entry)
    }

    suspend fun getAll():
            List<MoodJournalEntry> {

        return dao.getAll()
    }
}