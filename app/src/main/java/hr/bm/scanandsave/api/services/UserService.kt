package hr.bm.scanandsave.api.services

import hr.bm.scanandsave.api.responses.UserResponse
import io.reactivex.Single
import retrofit2.http.GET

interface UserService {
    @GET("builds/ISBD_public/Zadatak_1.json")
    fun getUser(): Single<UserResponse>
}