package com.junior0028.assesment3.network

import com.junior0028.assesment3.model.Menu
import com.junior0028.assesment3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://restoran-api.michael-kaiser.my.id/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface HewanApiService {
    @GET("menu")
    suspend fun getMenu(
        @Header("Authorization") token: String
    ): List<Menu>

    @Multipart
    @POST("menu")
    suspend fun postMenu(
        @Header("Authorization") token: String,
        @Part("nama") nama: RequestBody,
        @Part("namaLatin") namaLatin: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @DELETE("menu/{id_menu}")
    suspend fun deleteHewan(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): OpStatus
}

object MenuApi {
    val service: MenuApiService by lazy {
        retrofit.create(MenuApiService::class.java)
    }

    fun getImageUrl(id: Long): String {
        return "${BASE_URL}menu/image/$id"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED}