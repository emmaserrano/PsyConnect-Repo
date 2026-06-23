package ni.edu.uam.psyconnect.data.moodjournal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MoodJournalEntry::class],
    version = 2,
    exportSchema = false
)
abstract class MoodJournalDatabase : RoomDatabase() {

    abstract fun moodJournalDao(): MoodJournalDao

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
                .fallbackToDestructiveMigration() // Para facilitar el desarrollo ante cambios de esquema
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
