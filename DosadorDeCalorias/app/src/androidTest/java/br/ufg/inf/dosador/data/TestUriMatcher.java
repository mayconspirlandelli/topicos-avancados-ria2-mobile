package br.ufg.inf.dosador.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by Maycon on 06/04/2015.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String LOG_CAT = TestUriMatcher.class.getSimpleName();

    private static final String TEST_DATA = "2015-01-02";
    private static final String TEST_DATA_FINAL = "2015-02-03";
    private static final String TEST_MES = "02";
    private static final long TEST_LOCATION_ID = 10L;

    private static final Uri TEST_DOSADOR_DIR = DosadorContract.ConsumoEntry.CONTENT_URI;
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_DIARIO_DIR = DosadorContract.ConsumoEntry.buildConsumoPorData(TEST_DATA);
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_PERIODO_DIR = DosadorContract.ConsumoEntry.buildConsumoPorPeriodo(TEST_DATA, TEST_DATA_FINAL);
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_MENSAL_DIR = DosadorContract.ConsumoEntry.buildConsumoPorMes(TEST_MES);

    public void testUriMatcher() {
        UriMatcher testMatcher = DosadorProvider.buildUriMatcher();

        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_DIR),
                DosadorProvider.CONSUMO);

        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_DIARIO_DIR),
                DosadorProvider.CONSUMO_DIARIO);

        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_PERIODO_DIR),
                DosadorProvider.CONSUMO_PERIODO);

//        assertEquals("Error",
//                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_MENSAL_DIR),
//                DosadorProvider.CONSUMO_MENSAL);

        Log.d(LOG_CAT, "fsfsa");

    }

}
