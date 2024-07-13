package com.example.picturestation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.picturestation.databases.ImageDirectoryDao
import com.example.picturestation.databases.entities.ImageDirectory
import com.example.picturestation.screens.uis.FooterUI
import com.example.picturestation.screens.uis.HeaderUI
import com.example.picturestation.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun ShowImageDirectoryScreen(db: ImageDirectoryDao, navController: NavHostController, returnImageDirectoryID: (Int) -> Unit){
    Column(modifier = Modifier.background(greenGrey80)){
        Box(modifier = Modifier.weight(1.5f)){ HeaderUI() }
        Box(modifier = Modifier.weight(10f)) { ShowImageDirectoryUI(db, navController, returnImageDirectoryID) }
        Box(modifier = Modifier.weight(1f))  { FooterUI(navController) }
    }
}

@Composable
private fun ShowImageDirectoryUI(db: ImageDirectoryDao, navController: NavHostController, returnImageDirectoryID: (Int) -> Unit){
    var listImageDirectory by remember { mutableStateOf<List<ImageDirectory>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            db.getAll().collect { directories ->
                withContext(Dispatchers.Main) {
                    listImageDirectory = directories
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(10.dp)
    ){
        items(listImageDirectory){ imageDirectory ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
//                        println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                        returnImageDirectoryID(imageDirectory.id)
                        navController.navigate("com/example/picturestation/screens/ShowImagesScreen.kt")
                    }
                    .background(lightGreen40),
//                    .padding(10.dp)
                verticalArrangement = Arrangement.Center
            ){
                Row(
                    verticalAlignment =  Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = rememberAsyncImagePainter(File(imageDirectory.uri)),
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(70.dp)
                            .clip(CircleShape),
                    )
                    Text(imageDirectory.title, maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}