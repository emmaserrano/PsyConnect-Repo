package ni.edu.uam.psyconnect.data.moodjournal

import kotlinx.coroutines.flow.Flow

class MoodJournalRepository(
    private val dao: MoodJournalDao
) {
    // Exponemos el flujo de datos directamente desde el DAO
    val allEntries: Flow<List<MoodJournalEntry>> = dao.getAllEntries()

    suspend fun insert(entry: MoodJournalEntry) {
        dao.insert(entry)
    }

    suspend fun update(entry: MoodJournalEntry) {
        dao.update(entry)
    }

    suspend fun delete(entry: MoodJournalEntry) {
        dao.delete(entry)
    }
}
