package edu.quinnipiac.ser210.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    public static final String EXTRA_DRINKID = "DrinkID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);

        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        try {
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK",new String[] {"NAME","DESCRIPTION","IMAGE_RESOURCE_ID","FAVORITE"},"_id = ?",
                                     new String[] {Integer.toString(drinkId)},null,null,null);
            if(cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                TextView description = findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    public void onFavoriteClicked(View view) {
        int drinkId = (Integer) getIntent().getExtras().get(EXTRA_DRINKID);
        new UpdateDrinkTask().execute(drinkId);
//        CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
//        ContentValues drinkValues = new ContentValues();
//        drinkValues.put("FAVORITE",favorite.isChecked());
//
//        SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
//        try {
//            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
//            db.update("DRINK",drinkValues,"_id = ?",new String[] {Integer.toString(drinkId)});
//            db.close();
//        } catch(SQLiteException e) {
//            Toast toast = Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    private class UpdateDrinkTask extends AsyncTask<Integer,Void,Boolean> {

        private ContentValues drinkValues;

        @Override
        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            drinkValues = new ContentValues();
            drinkValues.put("FAVORITE",favorite.isChecked());
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int drinkId = integers[0];
            SQLiteOpenHelper starbuzzDatabasehelper = new StarbuzzDatabaseHelper(DrinkActivity.this);

            try {
                SQLiteDatabase db = starbuzzDatabasehelper.getWritableDatabase();
                db.update("DRINK",drinkValues,"_id = ?",new String[] {Integer.toString(drinkId)});
                db.close();
                return true;
            } catch(Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean) {
                Toast toast = Toast.makeText(DrinkActivity.this,"Database unavailable",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}