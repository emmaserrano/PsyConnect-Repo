package ni.edu.uam.psyconnect.data.moodjournal

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ni.edu.uam.psyconnect.data.model.AchievementEntity

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Query("SELECT * FROM achievements WHERE userId = :userId ORDER BY id DESC")
    fun getAchievementsByUser(userId: Long): Flow<List<AchievementEntity>>

    @Query("DELETE FROM achievements WHERE userId = :userId")
    suspend fun deleteByUser(userId: Long)
}
