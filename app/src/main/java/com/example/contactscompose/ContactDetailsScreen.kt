package com.example.contactscompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.ArrayList

@Composable
fun ContactDetailsScreen(contact: Contact){
    Column(modifier = Modifier.fillMaxSize()
        .wrapContentSize(Alignment.Center)) {
        if (contact.image != null && contact.image != "") {
            Image(bitmap = imagePathToBitmap(contact.image), contentDescription = "Contact Image")
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
            ) {
                Text(
                    text = contact.firstName.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

        }
            contact.firstName?.let { Text(text = it) }
            contact.lastName?.let { Text(text = it) }
            contact.homePhone?.let { Text(text = it) }
            contact.mobilePhone?.let { Text(text = it) }
            contact.workPhone?.let { Text(text = it) }
            contact.workEmail?.let { Text(text = it) }
            contact.homeEmail?.let { Text(text = it) }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun ContactDetailsScreenPreview() {
//    ContactDetailsScreen(Contact("dsfdfs",
//            "avi", "xahavi", null, "ccc",
//            "ddd", "eee", "fff", "ggg"
//        ))
//}