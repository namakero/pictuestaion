package com.example.picturestation.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.picturestation.databases.ImageDirectoryDao
import com.example.picturestation.databases.entities.ImageDirectory
import com.example.picturestation.screens.uis.FooterUI
import com.example.picturestation.screens.uis.HeaderUI
import com.example.picturestation.ui.theme.greenGrey80
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun CreateImageDirectoryScreen(db: ImageDirectoryDao, navController: NavHostController) {
    Column(modifier = Modifier.background(greenGrey80)){
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1.5f))   { HeaderUI() }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(10f))  { CreateImageDirectoryUI(db) }
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f))   { FooterUI(navController) }
    }
}

@Composable
private fun CreateImageDirectoryUI(db: ImageDirectoryDao) {
    var title by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(modifier = Modifier
            .weight(1f)
            .padding(10.dp)){
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(350.dp)
            )
        }
        Box(modifier = Modifier.weight(1f)){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.padding(10.dp))

                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Load Image")
                }
                Spacer(modifier = Modifier.padding(10.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        if(imageUri != null){
                            val imagePath = saveImageToInternalStorage(context, imageUri!!)
                            val imageDirectory = ImageDirectory(
                                title = title,
                                uri = imagePath
                            )
                            db.insert(imageDirectory)
                            title = ""
                            imageUri = null
                        }
                    }
                }) {
                    Text("Create")
                }
                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val listImageDirectory = db.getAll().first()
                            listImageDirectory.forEach { imageDirectory ->
                                deleteImageFromInternalStorage(imageDirectory.uri)
                                imageDirectory.imageList.forEach { imagePath ->
                                    deleteImageFromInternalStorage(imagePath)
                                }
                            }
                            db.deleteAll()
                        }
                    },
                ){
                    Text("delete")
                }

            }
        }
    }
}
private suspend fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    return withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val outputFile = File(context.filesDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(outputFile).use { output ->
                input.copyTo(output)
            }
        }

        outputFile.absolutePath
    }
}

private suspend fun deleteImageFromInternalStorage(filePath: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)
            file.delete()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
