package ceg.avtechlabs.mba.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import ceg.avtechlabs.mba.R.id.description
import java.util.*
import java.util.logging.Logger

/**
 * Created by Adhithyan V on 30-05-2017.
 */

class DronaDBHelper(context: Context): SQLiteOpenHelper(context, DronaDBHelper.DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_NAME ($COL_TITLE text, $COL_CATEGORY varchar(30))")
        db?.execSQL("create table $FEED_TABLE ($FEED_TITLE text, $FEED_DESC text, $FEED_CATEGORY text, $FEED_READ integer check(read in (0,1)));")
        db?.execSQL("create table $FAV_TABLE ($FAV_TITLE text, $FAV_CONTENT text, $FAV_DATE text, $FAV_IMAGE_URL text);")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        db?.execSQL("create table if not exists $FEED_TABLE ($FEED_TITLE text, $FEED_DESC text, $FEED_CATEGORY text, $FEED_READ integer check(read in (0,1)));")
        db?.execSQL("create table if not exists $FAV_TABLE ($FAV_TITLE text, $FAV_CONTENT text, $FAV_DATE text, $FAV_IMAGE_URL text);")
        //onCreate(db)
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

    fun insertToFeeds(title: String, description: String, category: String, read: Int): Boolean {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(FEED_TITLE, title.toLowerCase().trim().replace("'", ""))
        cv.put(FEED_DESC, description.toLowerCase().trim().replace("'", ""))
        cv.put(FEED_CATEGORY, category.toLowerCase().trim().replace("'", ""))
        cv.put(FEED_READ, read.toInt())
        db.insert(FEED_TABLE, null, cv)
        db.close()
        return true
    }

    fun insertToFeeds(title: String, description: String, read: Int): Boolean {
        return insertToFeeds(title, description, "", read)
    }

    private fun markReadStatus(title: String, description: String,  category: String, read: Int) {
        val db = this.writableDatabase

        val whereClauseArguments = arrayOf(title, description, category)
        val cv = ContentValues()
        cv.put(FEED_READ, read)

        db.update(FEED_TABLE, cv, "$FEED_TITLE = ? AND $FEED_DESC = ? AND $FEED_CATEGORY = ?", whereClauseArguments)
        db.close()
        //return true
    }

    fun markFeedAsRead(title: String, description: String,  category: String) {
        markReadStatus(title.toLowerCase().trim().replace("'", ""),
                description.toLowerCase().trim().replace("'", ""),
                category.toLowerCase().trim().replace("'", ""), 1);
    }

    fun markFeedAsRead(title: String, description: String) {
        markReadStatus(title.toLowerCase().trim().replace("'", ""),
                description.toLowerCase().trim().replace("'", ""), "",1)
    }

    fun isFeedRead(title: String, description: String, category: String): Boolean {
        val db = this.readableDatabase
        val query = "select $FEED_READ from $FEED_TABLE where " +
                "$FEED_CATEGORY = '${category.toLowerCase().trim().replace("'", "")}' and " +
                "$FEED_TITLE = '${title.toLowerCase().trim().replace("'", "")}' and " +
                "$FEED_DESC = '${description.toLowerCase().trim().replace("'", "")}'"

        val cursor = db.rawQuery(query, null)

        if(cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0).toInt() == 1
        } else {
            return false
        }
        db.close()
    }

    fun isFeedRead(title: String, description: String): Boolean {
        val db = this.readableDatabase
        val query = "select $FEED_READ from $FEED_TABLE where " +
                "$FEED_TITLE = '${title.toLowerCase().trim().replace("'", "")}' and " +
                "$FEED_DESC = '${description.toLowerCase().trim().replace("'", "")}'"

        val cursor = db.rawQuery(query, null)

        if(cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0).toInt() == 1
        } else {
            return false
        }
        db.close()
    }

    fun cleanAllReadFeeds(category: String) {
        val db = this.writableDatabase
        val query = "delete from $FEED_TABLE where $FEED_CATEGORY" +
                "= '${category.toLowerCase().trim().replace("'", "")}' and $FEED_READ = 1"
        //val cursor = db.rawQuery(query, null)
        db.execSQL(query)
        //val where = "$FEED_TITLE = ?  AND $FEED_DESC = ? AND $FEED_CATEGORY = ?"

        /*if(cursor.moveToFirst()) {
            do {
                Log.d(TAG, cursor.getString(0))
                val args = arrayOf(cursor.getString(0), cursor.getString(1), category)
                db.delete(FEED_TABLE, where, args)
            } while(cursor.moveToNext())
        }*/

        db.close()
    }

    fun feedExists(title: String, description: String, category: String):Boolean {
        val db = this.readableDatabase
        val query = "select *from $FEED_TABLE where $FEED_TITLE = '${title.toLowerCase().trim().replace("'", "")}' " +
                "AND $FEED_DESC = '${description.toLowerCase().trim().replace("'", "")}' " +
                "AND $FEED_CATEGORY = '${category.toLowerCase().trim().replace("'", "")}'"
        val cursor = db.rawQuery(query, null)
        if(cursor != null && cursor.moveToFirst()) {
            return true
        }

        return false
    }

    fun feedExists(title: String, description: String):Boolean {
        val db = this.readableDatabase
        val query = "select *from $FEED_TABLE where $FEED_TITLE = '${title.toLowerCase().trim().replace("'", "")}' " +
                "AND $FEED_DESC = '${description.toLowerCase().trim().replace("'", "")}'"
        val cursor = db.rawQuery(query, null)
        if(cursor != null && cursor.moveToFirst()) {
            return true
        }

        return false
    }

    fun getAllFeeds() {
        val db = this.writableDatabase
        val query = "select *from $FEED_TABLE"
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()) {
            do {
                Log.d(TAG, "${cursor.getString(0)} ${cursor.getString(1)} ${cursor.getString(2)} ${cursor.getString(3)}")
            } while(cursor.moveToNext())
        } else {
            Log.d(TAG, "none db found")
        }

        db.close()
    }

    fun getAllUnreadFeeds(): ArrayList<FeedObject> {
        val db = this.readableDatabase
        val query = "select *from $FEED_TABLE where $FEED_READ = 0"
        val cursor = db.rawQuery(query, null)

        val array = ArrayList<FeedObject>()
        if(cursor.moveToFirst()) {
            do {
                //Log.d(TAG, "${cursor.getString(0)} ${cursor.getString(1)} ${cursor.getString(2)} ${cursor.getString(3)}")
                array.add(FeedObject(cursor.getString(0), cursor.getString(1)))
            } while(cursor.moveToNext())
        } else {
            Log.d(TAG, "none db found")
        }

        db.close()
        return array
    }

    fun addToFavorites(title: String, content: String, date: String, imageUrl: String): Boolean {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(FAV_TITLE, title.replace("'", ""))
        cv.put(FAV_CONTENT, content.replace("'", ""))
        cv.put(FAV_DATE, date.replace("'", ""))
        cv.put(FAV_IMAGE_URL, imageUrl.replace("'", ""))

        db.insert(FAV_TABLE, null, cv)
        db.close()
        return true
    }

    fun getFavorites(): ArrayList<FavObject> {
        val db = this.readableDatabase
        val query = "select *from $FAV_TABLE limit 15"
        val cursor = db.rawQuery(query, null)

        val array = ArrayList<FavObject>()
        if(cursor.moveToFirst()) {
            do {
                //Log.d(TAG, "${cursor.getString(0)} ${cursor.getString(1)} ${cursor.getString(2)} ${cursor.getString(3)}")
                array.add(FavObject(cursor.getString(0), cursor.getString(1)
                ,cursor.getString(2), cursor.getString(3)))
            } while(cursor.moveToNext())
        } else {
            Log.d(TAG, "none db found")
        }

        db.close()
        return array
    }

    fun removeFromFavorites(title: String, content: String, date: String, imageUrl: String) {
        val db = this.writableDatabase
        val wherClause = "$FAV_TITLE = ? AND $FAV_DATE = ? AND $FAV_IMAGE_URL = ?"

        db.delete(FAV_TABLE, wherClause, arrayOf(title, date, imageUrl))

        db.close()
    }

    companion object {
        val TAG = "dronadbupdate"

        val DB_NAME = "drona.db"
        val TABLE_NAME = "feeds"
        val COL_TITLE = "title"
        val COL_CATEGORY = "category"

        val FEED_TABLE = "read_status_table"
        val FEED_TITLE = "title"
        val FEED_DESC = "description"
        val FEED_CATEGORY = "category"
        val FEED_READ = "read"

        val FAV_TABLE = "favorites"
        val FAV_TITLE = "fav_title"
        val FAV_DATE = "fav_date"
        val FAV_CONTENT = "fav_content"
        val FAV_IMAGE_URL = "image_url"
    }
}