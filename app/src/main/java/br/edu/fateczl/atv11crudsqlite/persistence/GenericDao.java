/*
 *@author:<Matheus Augusto Marti>
 */

package br.edu.fateczl.atv11crudsqlite.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "TIMES.DB";
    private static final int DATABASE_VER = 1;
    private static final String CREATE_TABLE_TIME =
            "CREATE TABLE time ( " +
                    "codigo INT NOT NULL PRIMARY KEY, " +
                    "nome TEXT NOT NULL, " +
                    "cidade TEXT NOT NULL);";
    private static final String CREATE_TABLE_JOGADOR =
            "CREATE TABLE jogador ( " +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "nome TEXT NOT NULL, " +
                    "data_nasc TEXT NOT NULL, " +
                    "altura REAL NOT NULL, " +
                    "peso REAL NOT NULL, " +
                    "codigo_time INT NOT NULL, " +
                    "FOREIGN KEY (codigo_time) REFERENCES time(codigo));";

    public GenericDao(Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TIME);
        db.execSQL(CREATE_TABLE_JOGADOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS time");
            db.execSQL("DROP TABLE IF EXISTS jogador");
            onCreate(db);
        }
    }
}
