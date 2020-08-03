package com.basil.thelattice.bluetoothmessagingapp;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class)
public class Messages extends BaseModel {

    @Column
    @PrimaryKey (autoincrement = true)
    int id;

    @Column
    String message;

    @Column
    String from;

    @Column
    String to;

    public void insertData( String message, String from, String to) {
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
