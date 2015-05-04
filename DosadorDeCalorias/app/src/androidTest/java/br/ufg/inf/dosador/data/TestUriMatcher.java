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
    //private static final String TEST_TIPO_REFEICAO = TipoRefeicao.LANCHE.name();
    private static final String TEST_TIPO_REFEICAO = "lanche";



    private static final Uri TEST_DOSADOR_DIR = DosadorContract.ConsumoEntry.CONTENT_URI;
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_DIARIO_DIR = DosadorContract.ConsumoEntry.buildConsumoPorData(TEST_DATA);
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_PERIODO_DIR = DosadorContract.ConsumoEntry.buildConsumoPorPeriodo(TEST_DATA, TEST_DATA_FINAL);
    private static final Uri TEST_DOSADOR_WITH_CONSUMO_MENSAL_DIR = DosadorContract.ConsumoEntry.buildConsumoPorMes(TEST_MES);

    private static final Uri TEST_DOSADOR_WITH_CONSUMO_DIARIO_AND_TIPO_REFEICAO_DIR = DosadorContract.ConsumoEntry.buildConsumoPorDataAndTipoRefeicao(TEST_DATA, TEST_TIPO_REFEICAO);

    public void testUriMatcher() {
        UriMatcher testMatcher = DosadorProvider.buildUriMatcher();

        Log.d(LOG_CAT, "URI: " + TEST_DOSADOR_DIR.toString());
        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_DIR),
                DosadorProvider.CONSUMO);

        Log.d(LOG_CAT, "URI: " + TEST_DOSADOR_WITH_CONSUMO_DIARIO_DIR.toString());
        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_DIARIO_DIR),
                DosadorProvider.CONSUMO_DIARIO);

        Log.d(LOG_CAT, "URI: " + TEST_DOSADOR_WITH_CONSUMO_PERIODO_DIR.toString());
        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_PERIODO_DIR),
                DosadorProvider.CONSUMO_PERIODO);

        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_MENSAL_DIR),
                DosadorProvider.CONSUMO_MENSAL);

        assertEquals("Error",
                testMatcher.match(TEST_DOSADOR_WITH_CONSUMO_DIARIO_AND_TIPO_REFEICAO_DIR),
                DosadorProvider.CONSUMO_DIARIO_TIPO_REFEICAO);

        Log.d(LOG_CAT, "fsfsa");

    }

}
