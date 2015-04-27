package com.talkingandroid.hour19application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class PieDbAdapter {
    private static final String DATABASE_NAME = "FOOD_DATABASE.db";
    private static final String PIE_TABLE = "PIE_TABLE";
    private static final int DATABASE_VERSION =500;
    private final Context mCtx;
    public static String TAG = PieDbAdapter.class.getSimpleName();

    private DatabaseHelper mDbHelper;
    SQLiteDatabase mDb;

    public static final String KEY_ROWID = "_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String FAVORITE = "favorite";

    public static final String[] PIE_FIELDS = new String[] {
            KEY_ROWID,
            NAME,
            DESCRIPTION,
            PRICE,
            FAVORITE
    };

    private static final String CREATE_TABLE_PIE =
            "create table " + PIE_TABLE +"("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " not null UNIQUE,"
                    + DESCRIPTION +" text,"
                    + PRICE + " REAL,"
                    + FAVORITE + " INTEGER"
                    +");";



    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PIE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + PIE_TABLE );
            onCreate(db);
        }
    }


    public PieDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public PieDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        if(mDbHelper!=null){
            mDbHelper.close();
        }
    }
    public void upgrade() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx); //open
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(mDb, 1, 0);
    }
    public  long insertPie(ContentValues initialValues) {
        return mDb.insertWithOnConflict (PIE_TABLE, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean updatePie(int id, ContentValues newValues) {
        String[] selectionArgs = {String.valueOf(id)};
        return mDb.update(PIE_TABLE, newValues, KEY_ROWID + "=?", selectionArgs ) > 0;
    }

    public int updatePie(String selection, String[] selectionArgs, ContentValues newValues) {
        return mDb.update(PIE_TABLE, newValues, selection, selectionArgs );
    }


    public boolean deletePie(int id) {
        String[] selectionArgs = {String.valueOf(id)};
        return mDb.delete(PIE_TABLE,  KEY_ROWID + "=?",selectionArgs ) > 0;
    }


    public int deletePie(String selection, String[] selectionArgs) {
        return mDb.delete(PIE_TABLE,  selection, selectionArgs );
    }

    public Cursor getPies() {
        return mDb.query(PIE_TABLE, PIE_FIELDS, null, null, null, null, null);
    }

    public Cursor getPie(int id) {
        String[] selectionArgs = {String.valueOf(id)};
        return mDb.query(PIE_TABLE, PIE_FIELDS,  KEY_ROWID + "=?", selectionArgs, null, null, null);

    }


    public Cursor queryPies(String[] projection, String selection,
                            String[] selectionArgs, String sortOrder) {

        return mDb.query(PIE_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
    }


    public static Pie getPieFromCursor(Cursor cursor){
        Pie pie = new Pie();
        pie.mId = cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
        pie.mName = cursor.getString(cursor.getColumnIndex(NAME));
        pie.mDescription = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
        pie.mPrice = cursor.getDouble(cursor.getColumnIndex(PRICE));
        pie.mIsFavorite = (cursor.getInt(cursor.getColumnIndex(FAVORITE)) == 1);
        return(pie);
    }

}
