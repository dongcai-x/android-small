package com.dc.myapplication.db;

public class LifeDbSchema {
    public static final class LifeTable {

        public static final String NAME = "lives";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DESCRIPTION = "description";
            public static final String STAR = "star";
        }
    }


}