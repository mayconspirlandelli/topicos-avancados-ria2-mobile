package br.ufg.inf.dosador.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import br.ufg.inf.dosador.utils.PollingCheck;

/**
 * Created by Maycon on 01/04/2015.
 */
public class TestUtilities extends AndroidTestCase {

    //static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    static final String TEST_DATE = "2015-01-01";
    static final String TEST_DATE_FINAL = "2015-02-02";
    static final String TEST_DATE_MES = "01";


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createContentValuesConsumo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_NOME, "feijao");
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_QTD, 1);
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CALORIAS, 200);
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_GORDURA, 2.3);
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_CARBOIDRATO, 3.45);
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_PROTEINA, 4.56);
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_TIPO_REFEICAO, "lanche");
        contentValues.put(DosadorContract.ConsumoEntry.COLUMN_DATA, TEST_DATE);

        return contentValues;

    }

    /*
        The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

}
