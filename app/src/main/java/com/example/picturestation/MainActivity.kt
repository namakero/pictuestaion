package com.example.picturestation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.picturestation.databases.ImageDatabase
import com.example.picturestation.databases.ImageDirectoryDao
import com.example.picturestation.databases.entities.ImageDirectory
import com.example.picturestation.screens.CreateImageDirectoryScreen
import com.example.picturestation.screens.ShowImageDirectoryScreen
import com.example.picturestation.screens.ShowImagesScreen
import com.example.picturestation.ui.theme.PicturestationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val db = Room.databaseBuilder(applicationContext, ImageDatabase::class.java, "imageDirectory")
//                    .fallbackToDestructiveMigration() //use this code when db code is changed.
                .build().imageDirectoryDao()
            val navController: NavHostController = rememberNavController()

            PicturestationTheme{
                MainScreen(db = db, navController = navController)
            }
        }
    }
}

@Composable
private fun MainScreen(db: ImageDirectoryDao, navController: NavHostController){
    var _id by remember{ mutableStateOf<Int?>(null)}

    NavHost(navController = navController, startDestination = "com/example/picturestation/screens/ShowImageDirectoryScreen.kt"){
        composable("com/example/picturestation/screens/ShowImageDirectoryScreen.kt") {
            ShowImageDirectoryScreen(db = db, navController = navController, returnImageDirectoryID = { id -> _id = id})
        }
        composable("com/example/picturestation/screens/CreateImageDirectoryScreen.kt"){
            CreateImageDirectoryScreen(db = db, navController = navController)
        }
        composable("com/example/picturestation/screens/ShowImagesScreen.kt"){
            ShowImagesScreen(db = db, navController = navController, imageDirectoryID = _id!!)
        }
    }
}