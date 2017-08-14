package com.sebastian.clausing.quester.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper {
    private static final String DB_PATH = "/data/data/com.sebastian.clausing.quester/databases/";
    private static final String DB_NAME = "QuesterDB.db";

    public static void createDatabaseIfNotExists(Context context) throws IOException {
        boolean createDb = false;

        File dbDir = new File(DB_PATH);
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbDir.exists()) {
            dbDir.mkdir();
            createDb = true;
        }
        else if (!dbFile.exists()) {
            createDb = true;
        }
        else {
            boolean doUpgrade = true;
            Log.d("DBHelper", "Upgrade DB");

            if (doUpgrade) {
                dbFile.delete();
                createDb = true;
            }
        }

        if (createDb) {
            InputStream myInput = context.getAssets().open(DB_NAME);
            OutputStream myOutput = new FileOutputStream(dbFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    public static SQLiteDatabase getStaticDb() {
        return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }
}