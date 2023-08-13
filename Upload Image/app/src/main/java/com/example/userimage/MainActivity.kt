package com.example.userimage

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.BlobHttpHeaders
import com.azure.storage.blob.options.*
import com.azure.storage.blob.specialized.*
import kotlinx.coroutines.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var selectedFileUri: Uri
    private lateinit var containerClient: BlobContainerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Azure Blob Storage client
        val connectionString = "Add Your connection string"
        val containerName = "Add your own name"
        containerClient = BlobServiceClientBuilder()
            .connectionString(connectionString)
            .buildClient()
            .getBlobContainerClient(containerName)
    }

    fun chooseFile(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        this.startActivityForResult(/* intent = */ intent, /* requestCode = */ 1)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun uploadFile(view: View) {
        try {
            val file = getRealPathFromUri(selectedFileUri)?.let { File(it) }
            val filepath = file?.path
            // Create a BlobClient with the desired blob name
            val blobName = file?.name
            val blobClient: BlobClient = this.containerClient.getBlobClient(blobName)
            val contentType = "image/jpeg"
            // Set the blob HTTP headers
            val httpHeaders = BlobHttpHeaders().setContentType(contentType)

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (file != null) {
                        blobClient.uploadFromFile(filepath,null,httpHeaders,null,null,null,null)
                    }
                    withContext(Dispatchers.Main) {
                        // File uploaded successfully
                        showToast("Your File has been uploaded")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        // Error uploading the file
                        showToast("Error uploading file: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("UploadFile", "Error during file upload", e)
        }
    }

     private fun getRealPathFromUri(uri: Uri): String? {
        var path: String? = null
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex("_data")
                    if (columnIndex != -1) {
                        path = it.getString(columnIndex)
                    } else {
                        Log.e("getRealPathFromUri", "Column _data not found in the Cursor.")
                    }
                } else {
                    Log.e("getRealPathFromUri", "Cursor is empty.")
                }
            }
        } catch (e: Exception) {
            Log.e("getRealPathFromUri", "Error retrieving file path from Uri: ${e.message}")
        } finally {
            cursor?.close()
        }
        return path
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.data!!
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}























