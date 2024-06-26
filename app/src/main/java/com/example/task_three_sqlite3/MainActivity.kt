package com.example.task_three_sqlite3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task_three_sqlite3.ui.theme.Task_three_sqlite3Theme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dbHelper = ContactDatabaseHelper(this@MainActivity)
        setContent {
            Task_three_sqlite3Theme {
                ContactApp(dbHelper)
            }
        }
    }
}

@Composable
fun ContactApp(dbHelper: ContactDatabaseHelper) {
    var contacts by remember { mutableStateOf(dbHelper.getAllContacts()) }
    var newContactName by remember { mutableStateOf("") }
    var newContactPhoneNumber by remember { mutableStateOf("") }


    Scaffold(modifier = Modifier.padding(10.dp)) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = newContactName,
                onValueChange = { newContactName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newContactPhoneNumber,
                onValueChange = { newContactPhoneNumber = it },
                label = { Text(text = "Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val contactId = dbHelper.addContact(newContactName, newContactPhoneNumber)
                    if (contactId != -1L) {
                        contacts = dbHelper.getAllContacts()
                        newContactName = ""
                        newContactPhoneNumber = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Add Contact")
            }
            Spacer(modifier = Modifier.height(16.dp))
            ContactList(contacts = contacts, onDelete = { contactid ->
                dbHelper.deleteContacts(contactid)
                contacts = dbHelper.getAllContacts()
            })
        }
    }
}

@Composable
fun ContactList(contacts: List<Contact>, onDelete: (Long) -> Unit) {
    LazyColumn {
        items(contacts) { contact ->
            ContactItem(contact = contact, onDelete = onDelete)
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onDelete: (Long) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(start = 30.dp)
                .fillMaxWidth()
                .weight(1f)

        ) {
            Text(text = "Name: ${contact.name} ")
            Text(text = "Cellphone: ${contact.phoneNumber}")
        }
        IconButton(onClick = { onDelete(contact.id) }, modifier = Modifier.weight(0.09f)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}