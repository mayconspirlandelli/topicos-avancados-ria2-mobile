package br.ufg.inf.dosador.data;

/**
 * Created by Maycon on 01/04/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.ufg.inf.dosador.data.DosadorContract.ConsumoEntry;

/**
 *
 */
public class DosadorDbHelper extends SQLiteOpenHelper {

    private static final  int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "dosador.db";

    public DosadorDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       final String SQL_CREATE_CONSUMO_TABLE = "CREATE TABLE " + ConsumoEntry.TABLE_NAME + " ( " +
               ConsumoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
               ConsumoEntry.COLUMN_NOME + " TEXT UNIQUE NOT NULL, " +
               ConsumoEntry.COLUMN_QTD + " INTEGER NOT NULL, " +
               ConsumoEntry.COLUMN_CALORIAS + " REAL NOT NULL, " +
        ConsumoEntry.COLUMN_GORDURA + " REAL NOT NULL, " +
        ConsumoEntry.COLUMN_CARBOIDRATO + " REAL NOT NULL, " +
        ConsumoEntry.COLUMN_PROTEINA + " REAL NOT NULL, " +
        ConsumoEntry.COLUMN_TIPO_REFEICAO + " TEXT NOT NULL, " +
        ConsumoEntry.COLUMN_DATA + " DATE DEFAULT CURRENT_DATE NOT NULL " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CONSUMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ConsumoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
