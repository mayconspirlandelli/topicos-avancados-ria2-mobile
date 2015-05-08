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

    private static final int DATABASE_VERSION = 7;

    static final String DATABASE_NAME = "dosador.db";


    public DosadorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_CONSUMO_TABLE = "CREATE TABLE " + ConsumoEntry.TABLE_NAME + " ( " +
                ConsumoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConsumoEntry.COLUMN_NOME + " TEXT NOT NULL, " +
                ConsumoEntry.COLUMN_QTD + " INTEGER NOT NULL, " +
                ConsumoEntry.COLUMN_CALORIAS + " REAL NOT NULL, " +
                ConsumoEntry.COLUMN_GORDURA + " REAL NOT NULL, " +
                ConsumoEntry.COLUMN_CARBOIDRATO + " REAL NOT NULL, " +
                ConsumoEntry.COLUMN_PROTEINA + " REAL NOT NULL, " +
                ConsumoEntry.COLUMN_TIPO_REFEICAO + " TEXT NOT NULL, " +
                ConsumoEntry.COLUMN_DATA + " DATE DEFAULT CURRENT_DATE NOT NULL, " +
                ConsumoEntry.COLUMN_PORCAO + " TEXT NOT NULL, " +
                ConsumoEntry.COLUMN_FOOD_ID + " INTEGER NOT NULL " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CONSUMO_TABLE);

        final String SQL_CREATE_USUARIO_TABLE = "CREATE TABLE " + DosadorContract.UsuarioEntry.TABLE_NAME + " ( " +
                DosadorContract.UsuarioEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DosadorContract.UsuarioEntry.COLUMN_NOME + " TEXT NOT NULL, " +
                DosadorContract.UsuarioEntry.COLUMN_SEXO + " TEXT NOT NULL, " +
                DosadorContract.UsuarioEntry.COLUMN_IDADE + " INTEGER, " +
                DosadorContract.UsuarioEntry.COLUMN_PESO + " REAL NOT NULL, " +
                DosadorContract.UsuarioEntry.COLUMN_ALTURA + " REAL NOT NULL " + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_USUARIO_TABLE);

        final String SQL_CREATE_TIPO_REFEICAO_TABLE = "CREATE TABLE " + DosadorContract.TipoRefeicaoEntry.TABLE_NAME + " ( " +
                DosadorContract.TipoRefeicaoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DosadorContract.TipoRefeicaoEntry.COLUMN_NOME + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_TIPO_REFEICAO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ConsumoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DosadorContract.UsuarioEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DosadorContract.TipoRefeicaoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
