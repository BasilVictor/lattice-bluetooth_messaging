package com.basil.thelattice.bluetoothmessagingapp;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "BluetoothMessaging";

    public static final int VERSION = 1;
}
