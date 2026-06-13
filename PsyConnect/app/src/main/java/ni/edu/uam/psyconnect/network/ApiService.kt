package ni.edu.uam.psyconnect.network

import ni.edu.uam.psyconnect.data.model.AuthResponse
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.data.model.Psychologist
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.data.model.TestResult

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("api/results")
    suspend fun saveResult(
        @Body result: TestResult
    ): Response<TestResult>

    @GET("api/results/{userId}")
    suspend fun getHistory(
        @Path("userId") userId: Long
    ): Response<List<TestResult>>

    @PUT("api/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body user: User
    ): Response<User>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("psychologists")
    suspend fun getPsychologists():
            Response<List<Psychologist>>
    
    @GET("api/psychologists/featured")
    suspend fun getFeaturedPsychologists():
            Response<List<Psychologist>>
}