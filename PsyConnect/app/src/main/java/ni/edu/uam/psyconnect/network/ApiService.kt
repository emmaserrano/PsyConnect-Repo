package ni.edu.uam.psyconnect.network

import ni.edu.uam.psyconnect.data.model.AuthResponse
import ni.edu.uam.psyconnect.data.model.ChangeEmailRequest
import ni.edu.uam.psyconnect.data.model.ChangePasswordRequest
import ni.edu.uam.psyconnect.data.model.LoginRequest
import ni.edu.uam.psyconnect.data.model.Psychologist
import ni.edu.uam.psyconnect.data.model.RecoveryCodeRequest
import ni.edu.uam.psyconnect.data.model.ResetPasswordRequest
import ni.edu.uam.psyconnect.data.model.TestResult
import ni.edu.uam.psyconnect.data.model.User
import ni.edu.uam.psyconnect.data.model.VerifyCodeRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/users/register")
    suspend fun registerUser(@Body user: User): Response<User>

    @POST("api/users/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<User>

    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: User): Response<User>

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("api/results")
    suspend fun saveResult(@Body result: TestResult): Response<TestResult>

    @GET("api/results/{userId}")
    suspend fun getHistory(@Path("userId") userId: Long): Response<List<TestResult>>

    @GET("api/psychologists")
    suspend fun getPsychologists(): Response<List<Psychologist>>

    // Este endpoint funciona para enviar códigos (tanto registro como recuperación)
    @POST("api/verification/send")
    suspend fun sendVerificationCode(@Query("email") email: String): Response<ResponseBody>

    // Usaremos este endpoint estándar para validar cualquier código
    @POST("api/verification/validate")
    suspend fun validateCode(@Body request: VerifyCodeRequest): Response<Boolean>

    @POST("api/users/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResponseBody>

    // Mantenemos estos por si el backend los soporta, pero preferimos los de arriba
    @POST("api/verification/validate-recovery")
    suspend fun validateRecoveryCode(@Body request: RecoveryCodeRequest): Response<Boolean>

    @GET("api/users/exists-email/{email}")
    suspend fun existsEmail(
        @Path("email") email: String
    ): Response<Boolean>

    @POST("api/users/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequest
    ): Response<ResponseBody>

    @POST("api/users/change-email")
    suspend fun changeEmail(
        @Body request: ChangeEmailRequest
    ): Response<ResponseBody>

}
