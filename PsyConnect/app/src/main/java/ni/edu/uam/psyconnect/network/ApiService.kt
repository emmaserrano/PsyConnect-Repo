package ni.edu.uam.psyconnect.network

import ni.edu.uam.psyconnect.data.model.AuthResponse
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.data.model.User

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("api/users/register")
    suspend fun registerUser(
        @Body user: User
    ): Response<User>

    @POST("api/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<User>
}