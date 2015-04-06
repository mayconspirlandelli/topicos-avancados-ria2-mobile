package br.ufg.inf.dosador.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by Maycon on 01/04/2015.
 */
public class TestDosadorDbHelper extends AndroidTestCase {

    public static final String LOG_CAT = TestDosadorDbHelper.class.getSimpleName();

    void deletarBancoDeDados(){
        mContext.deleteDatabase(DosadorDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deletarBancoDeDados();
    }

    public void testCriacaoDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(DosadorContract.ConsumoEntry.TABLE_NAME);

        mContext.deleteDatabase(DosadorDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DosadorDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        //Verifica se o banco de dados foi criado.
        assertTrue("Erro: O banco de dados não foi criado corretamente!", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        //Verifica se as tabelas do banco foram criadas.
        assertTrue("Erro: Seu banco de dados foi criado sem as tabelas!", tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + DosadorContract.ConsumoEntry.TABLE_NAME + ")", null);

        //Verifica se as colunas de cada tabela foram criadas corretamente.
        assertTrue("Erro: This means that we were unable to query the database for table information.", c.moveToFirst());

        final HashSet<String> consumoColumnHashSet = new HashSet<String>();
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry._ID);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_NOME);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_QTD);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_CALORIAS);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_GORDURA);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_PROTEINA);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_TIPO_REFEICAO);
        consumoColumnHashSet.add(DosadorContract.ConsumoEntry.COLUMN_DATA);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            consumoColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required CONSUMO entry columns", consumoColumnHashSet.isEmpty());
        db.close();
    }

    public void testConsumoTabela() {

        long consumoRowId;

        DosadorDbHelper dbHelper = new DosadorDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues testValues = TestUtilities.createContentValuesConsumo();

        consumoRowId = db.insert(DosadorContract.ConsumoEntry.TABLE_NAME, null, testValues);

        assertTrue(consumoRowId != -1);

        Cursor cursor = db.query(DosadorContract.ConsumoEntry.TABLE_NAME, null, null, null, null, null, null);

        assertTrue("Error: No Records returned from weather query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed.", cursor, testValues);
        assertFalse("Error: More than one record returned form weather query.", cursor.moveToNext());

        cursor.close();
        db.close();
    }
}
