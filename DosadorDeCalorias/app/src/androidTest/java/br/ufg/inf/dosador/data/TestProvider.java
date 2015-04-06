package br.ufg.inf.dosador.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import br.ufg.inf.dosador.data.DosadorContract.ConsumoEntry;

/**
 * Created by Maycon on 06/04/2015.
 */
public class TestProvider extends AndroidTestCase {

    private static final String LOG_CAT = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider(){
        mContext.getContentResolver().delete(
                ConsumoEntry.CONTENT_URI,
                null, null);

        Cursor cursor = mContext.getContentResolver().query(
                ConsumoEntry.CONTENT_URI,
                null, null, null, null);

        assertEquals("Error: Records not deleted from Weather table during delete",
                0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB(){
        DosadorDbHelper dbHelper = new DosadorDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DosadorContract.ConsumoEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords(){
        deleteAllRecordsFromDB();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DosadorProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: DosadorProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + DosadorContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DosadorContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

      /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
      */
    public void testGetType(){
        //content://br.ufg.inf.dosador.app/consumo/
        // vnd.android.cursor.dir/br.ufg.inf.dosador.app/consumo/
        String type = mContext.getContentResolver().getType(ConsumoEntry.CONTENT_URI);
        assertEquals("Error", ConsumoEntry.CONTENT_TYPE, type);

        //content://br.ufg.inf.dosador.app/consumo/2015-01-02
        // vnd.android.cursor.item/br.ufg.inf.dosador.app/consumo/2015-01-02
        String data = "2015-01-02";
        type = mContext.getContentResolver().getType(ConsumoEntry.buildConsumoPorData(data));
        assertEquals("Error", ConsumoEntry.CONTENT_ITEM_TYPE, type);

        //content://br.ufg.inf.dosador.app/consumo/2015-01-02/2015-02-02
        // vnd.android.cursor.item/br.ufg.inf.dosador.app/consumo/2015-01-02/2015-02-02
        data = "2015-01-02";
        String dataFinal = "2015-02-02";
        type = mContext.getContentResolver().getType(ConsumoEntry.buildConsumoPorPeriodo(data, dataFinal));
        assertEquals("Error", ConsumoEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testBasicConsumoQuery() {
        // insert our test records into the database
        DosadorDbHelper dbHelper = new DosadorDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createContentValuesConsumo();
        long consumoRowId = db.insert(ConsumoEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert WeatherEntry into the Database", consumoRowId != -1);
        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                ConsumoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicConsumoQuery", cursor, testValues);
    }



}
