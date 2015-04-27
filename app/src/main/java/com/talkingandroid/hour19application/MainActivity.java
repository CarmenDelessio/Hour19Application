package com.talkingandroid.hour19application;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        ArrayList<Pie> pies = Pie.makePies();
        for (Pie pie: pies){
            System.out.println(" pie "+ pie.mName);
            ContentValues newValues = new ContentValues();
            newValues.put(PieDbAdapter.NAME, pie.mName);
            newValues.put(PieDbAdapter.DESCRIPTION, pie.mDescription);
            newValues.put(PieDbAdapter.PRICE, pie.mPrice);
            newValues.put(PieDbAdapter.FAVORITE, pie.mIsFavorite);
            try {
                getContentResolver().insert(MyContentProvider.CONTENT_URI, newValues);
            }catch (SQLException e){};
        }

        Cursor pieCursor = getContentResolver().query(MyContentProvider.CONTENT_URI, null,null, null,null);
        StringBuilder results = new StringBuilder();

                PieDbAdapter pieDbAdapter = new PieDbAdapter(this);
        pieDbAdapter.open();
        if (pieCursor.moveToFirst()){
            do{
                Pie pie =  pieDbAdapter.getPieFromCursor(pieCursor);
                results.append(pie.mId + " " + pie.mName + " " + pie.mPrice + " "
                        + pie.mDescription + " " + pie.mIsFavorite +"\n");
            } while (pieCursor.moveToNext());
        }
        pieCursor.close();
        pieDbAdapter.close();
        textView.setText(results.toString());
    }
}
