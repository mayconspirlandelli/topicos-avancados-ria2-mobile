package br.ufg.inf.dosador.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Maycon on 06/04/2015.
 */
public class TestDosadorContract extends AndroidTestCase {

    //private static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    private static final String TEST_DATE = "2015-01-02";  // December 20th, 2014
    private static final String TEST_DATE_FINAL = "2015-02-02";
    private static final String TEST_DATE_MES = "02";
    private static final long TEST_ID = 22L;

    final String LOG_TAG = TestDosadorContract.class.getSimpleName();

    public void testBuildConsumoDiario() {
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoPorData(TEST_DATE);

        assertNotNull("Erro: Null Uri returned.", uri);

        //assertEquals("Erro:", String.valueOf(TEST_DATE), uri.getLastPathSegment());
        assertEquals("Erro:", TEST_DATE, uri.getLastPathSegment());

        //assertEquals("Erro:", uri.toString(), "content://br.ufg.inf.dosador.app/consumo/1419033600");
        assertEquals("Erro:", uri.toString(), "content://br.ufg.inf.dosador.app/consumo/2015-01-02");

        Log.d(LOG_TAG, uri.toString());
        }

    public void testBuildConsumoUri() {
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoUri(TEST_ID);
        assertNotNull("Erro: Null Uri returned.", uri);
        assertEquals("Erro:", String.valueOf(TEST_ID), uri.getLastPathSegment());
        assertEquals("Erro:", uri.toString(), "content://br.ufg.inf.dosador.app/consumo/22");
        Log.d(LOG_TAG, uri.toString());
    }

    public void testBuildConsumoPorData(){
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoPorData(TEST_DATE);
        String data = DosadorContract.ConsumoEntry.getDataDiariaFromUri(uri);

        assertNotNull("Erro: Id null.", data);
        assertEquals("Erro.", TEST_DATE, data);
        Log.d(LOG_TAG, uri.toString());

    }

    public void testBuildConsumoPorPeriodo(){
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoPorPeriodo(TEST_DATE, TEST_DATE_FINAL);
        String data = DosadorContract.ConsumoEntry.getDataInicialFromUri(uri);
        String dataFinal = DosadorContract.ConsumoEntry.getDataFinalFromUri(uri);

        assertNotNull("Erro: Id null.", data);
        assertNotNull("Erro: Id null.", dataFinal);
        assertEquals("Erro.", TEST_DATE, data);
        assertEquals("Erro.", TEST_DATE_FINAL, dataFinal);
        Log.d(LOG_TAG, uri.toString());

    }

    public void testBuildConsumoPorMes(){
        Uri uri = DosadorContract.ConsumoEntry.buildConsumoPorMes(TEST_DATE_MES);
        String mes = DosadorContract.ConsumoEntry.getMesFromUri(uri);

        assertNotNull("Erro: Id null.", mes);
        assertEquals("Erro.", TEST_DATE_MES, mes);
        Log.d(LOG_TAG, uri.toString());
    }

}
