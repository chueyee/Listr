package com.example.listr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.listr.ItemDbSchema.ItemTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }

        return sItemLab;
    }

    private ItemLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemBaseHelper(mContext).getWritableDatabase();


    }

    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemTable.NAME, null, values);
    }

    public void deleteItem(Item item) {
        mDatabase.delete(ItemTable.NAME, ItemTable.Cols.UUID + " =?", new String[] {String.valueOf(item.getId())});
    }


    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();

        //NULL and NULL because we want everything on the list
        ItemCursorWrapper cursorWrapper = queryItems(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                items.add(cursorWrapper.getItem());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return items;
    }

    public Item getItem(UUID id) {
        ItemCursorWrapper cursorWrapper = queryItems(
                ItemTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getItem();
        } finally {
            cursorWrapper.close();
        }
    }

    public File getPhotoFile(Item item) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, item.getPhotoFilename());
    }

    public void updateItem(Item item) {
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);

        mDatabase.update(ItemTable.NAME, values, ItemTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new ItemCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemTable.Cols.TITLE, item.getTitle());
        values.put(ItemTable.Cols.DETAIL, item.getDetail());
        values.put(ItemTable.Cols.DATE, item.getDate().getTime());
        values.put(ItemTable.Cols.HAVE, item.isHave() ? 1 : 0);

        return values;
    }



}
