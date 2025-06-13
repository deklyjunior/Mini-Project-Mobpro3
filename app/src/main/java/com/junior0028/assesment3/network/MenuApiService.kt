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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://restoran-api.michael-kaiser.my.id/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MenuApiService {
    @GET("menu")
    suspend fun getMenu(
        @Header("Authorization") token: String
    ): List<Menu>

    @Multipart
    @POST("menu")
    suspend fun postMenu(
        @Header("Authorization") token: String,
        @Part("judul") judul: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("asal") asal: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("menu/{id_menu}")
    suspend fun updateMenu(
        @Header("Authorization") token: String,
        @Path("id_menu") idMenu: Long,
        @Part("_method") method: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("asal") asal: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): OpStatus

    @DELETE("menu/{id_menu}")
    suspend fun deleteMenu(
        @Header("Authorization") token: String,
        @Path("id_menu") idMenu: Long
    ): OpStatus

    @FormUrlEncoded
    @POST("register")
    suspend fun registerAkun(
        @Field("name") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): OpStatus
}

object MenuApi {
    val service: MenuApiService by lazy {
        retrofit.create(MenuApiService::class.java)
    }

    fun getImageUrl(id: Long): String {
        return "${BASE_URL}menu/image/$id?timestamp=${System.currentTimeMillis()}"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED}