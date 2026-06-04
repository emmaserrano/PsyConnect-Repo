package ni.edu.uam.psyconnect.data.repository

import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.network.RetrofitClient
import retrofit2.Response

class AuthRepository {

    suspend fun registerUser(
        user: User
    ): Response<User> {

        return RetrofitClient
            .apiService
            .registerUser(user)
    }
}