package com.example.listr;

public class ItemDbSchema {
    public static final class ItemTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DETAIL = "detail";
            public static final String DATE = "date";
            public static final String HAVE = "have";

        }
    }
}
