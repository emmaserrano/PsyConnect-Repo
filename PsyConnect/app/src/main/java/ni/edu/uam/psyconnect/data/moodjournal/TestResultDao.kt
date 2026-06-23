package ni.edu.uam.psyconnect.data.moodjournal

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ni.edu.uam.psyconnect.data.model.TestResultEntity

@Dao
interface TestResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: TestResultEntity)

    @Query("SELECT * FROM test_results WHERE userId = :userId ORDER BY id DESC")
    fun getResultsByUser(userId: Long): Flow<List<TestResultEntity>>

    @Query("SELECT * FROM test_results WHERE userId = :userId AND category = :category ORDER BY id DESC")
    fun getResultsByCategory(userId: Long, category: String): Flow<List<TestResultEntity>>

    @Delete
    suspend fun delete(result: TestResultEntity)
}
