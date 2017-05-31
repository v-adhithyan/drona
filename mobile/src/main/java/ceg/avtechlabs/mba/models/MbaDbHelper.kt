package ceg.avtechlabs.mba.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ceg.avtechlabs.mba.util.Logger

/**
 * Created by Adhithyan V on 30-05-2017.
 */

class MbaDbHelper(context: Context): SQLiteOpenHelper(context, MbaDbHelper.DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME ($COL_TITLE text, $COL_CATEGORY varchar(30))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }

    /*fun insertAll(titles: Array<String>) {
        val db = this.writableDatabase

        Runnable {
            for (title in titles) {
                val cv = ContentValues()
                cv.put(COL_TITLE, title)
                db.insert(TABLE_NAME, null, cv)
            }
        }
    }*/

    fun insert(title: String, category: String): Boolean {
        val db = this.writableDatabase

        if(sameTop(category, title)) { return false }

        truncate(category)
        val cv = ContentValues()
        cv.put(COL_TITLE, title)
        cv.put(COL_CATEGORY, category.toLowerCase())
        db.insert(TABLE_NAME, null, cv)
        db.close()
        return true
    }

    fun getTop(category: String): String? {
        val db = this.readableDatabase
        val result = db.rawQuery("select $COL_TITLE from $TABLE_NAME where $COL_CATEGORY = '${category.toLowerCase()}'", null)

        if(result.count > 0) {
            result.moveToFirst()
            return result.getString(0)
        } else {
            return null
        }
    }

    fun sameTop(category: String, title: String): Boolean {
        val current = getTop(category)
        return (current != null && current.toLowerCase().equals(title.toLowerCase()))
    }
    private fun truncate(category: String) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME where $COL_CATEGORY='${category.toLowerCase()}'")
    }

    companion object {
        val DB_NAME = "drona.db"
        val TABLE_NAME = "feeds"
        val COL_TITLE = "title"
        val COL_CATEGORY = "category"
    }
}