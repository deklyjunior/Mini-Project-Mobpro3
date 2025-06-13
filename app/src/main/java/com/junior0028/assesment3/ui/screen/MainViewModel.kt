package com.junior0028.assesment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junior0028.assesment3.model.Menu
import com.junior0028.assesment3.network.ApiStatus
import com.junior0028.assesment3.network.MenuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel: ViewModel() {
    var data = mutableStateOf(emptyList<Menu>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set
    var deleteStatus = mutableStateOf<String?>(null)
        private set

    fun retrieveData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = MenuApi.service.getMenu(token)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(token: String, judul: String, kategori: String, asal: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MenuApi.service.postMenu(
                    token,
                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    kategori.toRequestBody("text/plain".toMediaTypeOrNull()),
                    asal.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(token)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(token: String, idMenu: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = MenuApi.service.deleteMenu(token, idMenu)
                if (result.status == "success") {
                    retrieveData(token)
                } else {
                    deleteStatus.value = result.message ?: "Gagal menghapus data"
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Delete failure: ${e.message}")
                deleteStatus.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun clearDeleteStatus() {
        deleteStatus.value = null
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}