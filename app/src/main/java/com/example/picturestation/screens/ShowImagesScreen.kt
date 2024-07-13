package com.example.picturestation.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun ShowImagesScreen(db: ImageDirectoryDao, navController: NavHostController, imageDirectoryID: Int){
    Column(modifier = Modifier.background(greenGrey80)){
        Box(modifier = Modifier.weight(1.5f)){ HeaderUI() }
        Box(modifier = Modifier.weight(10f) ){ ShowImagesUI(db, imageDirectoryID) }
        Box(modifier = Modifier.weight(1f)  ){ FooterUI(navController = navController) }
    }
}

@Composable
private fun ShowImagesUI(db: ImageDirectoryDao, initialImageDirectoryID: Int) {
    var imageDirectory by remember { mutableStateOf(ImageDirectory(title = "null", uri = "null")) }
    var imageList by remember{ mutableStateOf<List<String>>(emptyList()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(initialImageDirectoryID) {
        coroutineScope.launch {
            db.get(initialImageDirectoryID).collect { image ->
                imageDirectory = image
                imageList = imageDirectory.imageList
//                println("${imageDirectory.title}")
            }
        }
    }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        coroutineScope.launch {
            imageUri?.let {
                val imagePath = saveImageToInternalStorage(context, it)
                imageDirectory.imageList += imagePath
                db.update(imageDirectory)
                imageList += imagePath
                println(imageDirectory)
            }
        }
    }

    ImageGrid(imageList = imageList)
    Box(Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd){
        FloatingActionButton(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
        }
    }
}

@Composable
private fun ImageGrid(imageList: List<String>){
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(imageList) { image ->
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(2.dp),
                contentScale = ContentScale.Crop
            )
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