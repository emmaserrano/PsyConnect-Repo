package ni.edu.uam.psyconnect.data.moodjournal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ni.edu.uam.psyconnect.data.model.TestResultEntity

@Database(
    entities = [MoodJournalEntry::class, TestResultEntity::class],
    version = 3,
    exportSchema = false
)
abstract class MoodJournalDatabase : RoomDatabase() {

    abstract fun moodJournalDao(): MoodJournalDao
    abstract fun testResultDao(): TestResultDao

    companion object {
        @Volatile
        private var INSTANCE: MoodJournalDatabase? = null

        fun getDatabase(context: Context): MoodJournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodJournalDatabase::class.java,
                    "mood_journal_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
