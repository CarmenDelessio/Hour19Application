package com.talkingandroid.hour19application;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.database.SQLException;


public class MyContentProvider extends ContentProvider {
    PieDbAdapter mPieDbAdapter;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI("com.talkingandroid.hour19application.provider", "pie", 1);
        sUriMatcher.addURI("com.talkingandroid.hour19application.provider/#", "pie", 2);
    }

    public static final Uri CONTENT_URI = Uri.parse("content://com.talkingandroid.hour19application.provider/pie");


    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mPieDbAdapter = new PieDbAdapter(getContext());
        mPieDbAdapter.open();
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mPieDbAdapter.deletePie(selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case 1:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.talkingandroid.hour19application.Pie";
            case 2:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/com.talkingandroid.hour19application.Pie";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = mPieDbAdapter.insertPie(values);
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, id);
            return newUri;
        }
        else {
            throw new SQLException("Failed to insert row into " + uri);
        }


    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        int uriType = sUriMatcher.match(uri);
        switch (uriType) {
            case 1:
                cursor =  mPieDbAdapter.queryPies(projection, selection,
                        selectionArgs, sortOrder);
                break;
            case 2:
                cursor =  mPieDbAdapter.getPie(Integer.valueOf(uri.getLastPathSegment()));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int  result =  mPieDbAdapter.updatePie(selection, selectionArgs, values);
        return result;
    }
}
