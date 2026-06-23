package ni.edu.uam.psyconnect.data.moodjournal

import kotlinx.coroutines.flow.Flow
import ni.edu.uam.psyconnect.data.model.TestResultEntity

class TestResultRepository(
    private val dao: TestResultDao
) {
    fun getResultsByUser(userId: Long): Flow<List<TestResultEntity>> {
        return dao.getResultsByUser(userId)
    }

    fun getResultsByCategory(userId: Long, category: String): Flow<List<TestResultEntity>> {
        return dao.getResultsByCategory(userId, category)
    }

    suspend fun insert(result: TestResultEntity) {
        dao.insert(result)
    }

    suspend fun delete(result: TestResultEntity) {
        dao.delete(result)
    }
}
