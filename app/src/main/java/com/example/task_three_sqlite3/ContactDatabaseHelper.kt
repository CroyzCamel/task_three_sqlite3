package com.example.task_three_sqlite3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "contact.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE_NUMBER = "phone_number"

        private const val TABLE_CREATE =
            "CREATE TABLE $TABLE_CONTACTS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_PHONE_NUMBER TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS ")
        onCreate(db)
    }

    fun addContact(name: String, phoneNumber: String) : Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_PHONE_NUMBER, phoneNumber)
        }
        val id = db.insert(TABLE_CONTACTS, null, values)
        db.close()
        return id
    }

    fun getAllContacts() : List<Contact> {
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)
        val contacts = mutableListOf<Contact>()
        while (cursor.moveToNext()) {
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val contactName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val contactPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER))
            contacts.add(Contact(contactId, contactName, contactPhoneNumber))
        }
        cursor.close()
        db.close()
        return contacts
    }

    fun deleteContacts(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_CONTACTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }


}
