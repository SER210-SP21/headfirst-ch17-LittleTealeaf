package edu.quinnipiac.ser210.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz";
    private static final int DB_VERSION = 2;

    StarbuzzDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase,0,DB_VERSION);
    }

    /*
    WHY
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        updateMyDatabase(sqLiteDatabase,oldVersion,newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(oldVersion < 1) {
            sqLiteDatabase.execSQL("CREATE TABLE DRINK (_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, IMAGE_RESOURCE_ID INTEGER);");
            insertDrink(sqLiteDatabase,"Latte","Espresso and steamed milk",R.drawable.latte);
            insertDrink(sqLiteDatabase,"Cappuccino","Espresso, hot milk and steamed-milk foam",R.drawable.cappuccino);
            insertDrink(sqLiteDatabase,"Filter","Our best drip coffee",R.drawable.filter);
        }

        if(oldVersion < 2) {{
            sqLiteDatabase.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }}
    }

    private static void insertDrink(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues drinkValues = new ContentValues();
        drinkValues.put("NAME",name);
        drinkValues.put("DESCRIPTION",description);
        drinkValues.put("IMAGE_RESOURCE_ID",resourceId);
        db.insert("DRINK",null,drinkValues);
    }
}