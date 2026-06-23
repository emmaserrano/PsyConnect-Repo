package ni.edu.uam.psyconnect.data.moodjournal

import androidx.room.*

@Dao
interface MoodJournalDao {

    @Insert
    suspend fun insert(entry: MoodJournalEntry)

    @Update
    suspend fun update(entry: MoodJournalEntry)

    @Delete
    suspend fun delete(entry: MoodJournalEntry)

    @Query("SELECT * FROM mood_journal ORDER BY timestamp DESC")
    suspend fun getAll(): List<MoodJournalEntry>
}
