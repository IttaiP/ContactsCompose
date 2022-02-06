package com.example.contactscompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactDetailsScreen(contact: Contact){
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .background(Color.Gray),
        verticalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        if (contact.image != null && contact.image != "") {
            Image(bitmap = imagePathToBitmap(contact.image),
                contentDescription = "Contact Image"
            ,modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally))
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.Cyan)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = contact.firstName?.get(0).toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 38.sp,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }

        }
        val textModifier:Modifier = Modifier.align(
            Alignment.CenterHorizontally
        )
        contact.firstName?.let { SingleDetailCard(type = "First Name", detail = it) }
        contact.lastName?.let { SingleDetailCard(type = "Last Name", detail = it) }
        contact.homePhone?.let { SingleDetailCard(type = "Home Phone Number", detail = it) }
        contact.mobilePhone?.let { SingleDetailCard(type = "Mobile Phone Number", detail = it) }
        contact.workPhone?.let { SingleDetailCard(type = "Work Phone Number", detail = it) }
        contact.workEmail?.let { SingleDetailCard(type = "Work Email", detail = it) }
        contact.homeEmail?.let { SingleDetailCard(type = "Home Email", detail = it) }


    }
}

@Preview(showBackground = true)
@Composable
fun ContactDetailsScreenPreview() {
    ContactDetailsScreen(Contact("dsfdfs",
            "avi", "xahavi", "", "angirfji@gmail.com",
            "aaaaaaji@gmail.com", "0549294929", "0495867483", "0384858493"
        ))
}

@Composable
fun SingleDetailCard(type: String, detail: String ){
    Card(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp))
        .padding(4.dp)){
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                //todo add background(color=)
                .padding(16.dp)
        ) {
            Text(text = type+": ")
            Text(detail)
        }
    }
}