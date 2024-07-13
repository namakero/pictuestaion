package com.example.picturestation.screens.uis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.picturestation.ui.theme.*

@Composable//complete
fun FooterUI(navController: NavHostController){
    val contents = listOf(
        listOf("show", "com/example/picturestation/screens/ShowImageDirectoryScreen.kt"),
        listOf("create", "com/example/picturestation/screens/CreateImageDirectoryScreen.kt")
    )

    Row(modifier = Modifier.fillMaxSize().background(green80),){
        for(content in contents){
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable{navController.navigate(content[1])},
                contentAlignment = Alignment.Center
            ){
                ButtonUI(content)
            }
        }
    }

}

@Composable
private fun ButtonUI(content: List<String>){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(imageVector = Icons.Default.Menu, contentDescription = null)
        Text(content[0])
    }
}
