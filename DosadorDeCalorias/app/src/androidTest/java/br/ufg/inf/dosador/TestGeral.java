package br.ufg.inf.dosador;

import android.test.AndroidTestCase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.entidades.Consumo;

/**
 * Created by Maycon on 30/04/2015.
 */
public class TestGeral extends AndroidTestCase {

    private static final String LOG_TAG = TestGeral.class.getSimpleName();

    public void testInstancia() {

        Alimento objeto = new Alimento();
        Consumo objeto2 = new Consumo();
        Alimento objeto3 = new Consumo();

        assertTrue("Error: não é instancia de " + Alimento.class.getSimpleName(), objeto instanceof  Alimento);
        assertTrue("Error: não é instancia de " + Alimento.class.getSimpleName(), objeto2 instanceof  Alimento);
        assertTrue("Error: não é instancia de " + Alimento.class.getSimpleName(), objeto3 instanceof  Alimento);
        assertTrue("Error: não é instancia de " + Consumo.class.getSimpleName(), objeto3 instanceof  Consumo);
        assertTrue("Error: não é instancia de " + Consumo.class.getSimpleName(), objeto2 instanceof  Consumo);
        assertFalse("Error: não é instancia de " + Consumo.class.getSimpleName(), objeto instanceof  Consumo);
    }

    public void testDataAnteriorEPosterior() {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

        Calendar data = Calendar.getInstance();
        String hojeStr = DATE_FORMAT.format(data.getTime());

        Calendar ontem = getDiaAnterior(data);
        String ontemStr = DATE_FORMAT.format(ontem.getTime());

        Calendar amanha = getDiaPosterior(data);
        String amanhaStr = DATE_FORMAT.format(amanha.getTime());

        Log.d(LOG_TAG, "Ontem: " + ontemStr);
        Log.d(LOG_TAG, "Hoje: " + hojeStr);
        Log.d(LOG_TAG, "Amanha: " + amanhaStr);
        assertEquals("Erro", "2015-05-03", ontemStr);
        assertEquals("Erro", "2015-05-04", hojeStr);
        assertEquals("Erro", "2015-05-05", amanhaStr);

        Date date = null;
        try {
            date = (Date) DATE_FORMAT.parse(amanhaStr);
        } catch (ParseException e ) {
            Log.e(LOG_TAG, e.toString());
        }
        Log.d(LOG_TAG, "tewste");

    }

    private Calendar getDiaAnterior(Calendar calendar) {
        calendar.add(Calendar.DATE, -1);
        return calendar;
    }
    private Calendar getDiaPosterior(Calendar calendar) {
        calendar.add(Calendar.DATE, +2);
        return calendar;
    }


}
