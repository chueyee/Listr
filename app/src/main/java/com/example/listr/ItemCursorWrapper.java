package com.example.listr;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.listr.ItemDbSchema.ItemTable;

import java.util.Date;
import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {
    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemTable.Cols.UUID));
        String title = getString(getColumnIndex(ItemTable.Cols.TITLE));
        String detail = getString(getColumnIndex(ItemTable.Cols.DETAIL));
        long date = getLong(getColumnIndex(ItemTable.Cols.DATE));
        int isHave = getInt(getColumnIndex(ItemTable.Cols.HAVE));

        Item item = new Item(UUID.fromString(uuidString));
        item.setTitle(title);
        item.setDetail(detail);
        item.setDate(new Date(date));
        item.setHave(isHave != 0);


        return item;
    }
}
