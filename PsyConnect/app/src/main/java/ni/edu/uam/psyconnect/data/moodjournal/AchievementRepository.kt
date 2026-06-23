package ni.edu.uam.psyconnect.data.moodjournal

import kotlinx.coroutines.flow.Flow
import ni.edu.uam.psyconnect.data.model.AchievementEntity
import ni.edu.uam.psyconnect.network.RetrofitClient

class AchievementRepository(
    private val dao: AchievementDao
) {
    fun getAchievementsLocal(userId: Long): Flow<List<AchievementEntity>> {
        return dao.getAchievementsByUser(userId)
    }

    suspend fun refreshAchievements(userId: Long) {
        try {
            val response = RetrofitClient.apiService.getAchievements(userId)
            if (response.isSuccessful) {
                val remoteAchievements = response.body() ?: emptyList()
                val entities = remoteAchievements.map { remote ->
                    AchievementEntity(
                        userId = userId,
                        title = remote.title,
                        description = remote.description,
                        unlockedAt = remote.unlockedAt,
                        serverId = remote.id
                    )
                }
                // Limpiar antiguos y guardar nuevos para este usuario
                dao.deleteByUser(userId)
                dao.insertAll(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
