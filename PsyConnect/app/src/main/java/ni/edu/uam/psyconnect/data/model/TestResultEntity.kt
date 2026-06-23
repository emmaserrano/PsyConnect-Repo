package ni.edu.uam.psyconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val percentage: Int,
    val date: String,
    val insight: String,
    val userId: Long
)
