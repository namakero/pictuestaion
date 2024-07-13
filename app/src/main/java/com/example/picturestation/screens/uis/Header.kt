package com.example.picturestation.screens.uis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.MailOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.picturestation.ui.theme.green80

@Composable
fun HeaderUI(){
    Row(
        modifier                = Modifier.fillMaxSize().background(green80),
        verticalAlignment       = Alignment.CenterVertically,
        horizontalArrangement   = Arrangement.Center
    ){
        Image(
            imageVector = Icons.Sharp.MailOutline,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "Picture Station",
            fontSize = 40.sp,
        )
    }
}