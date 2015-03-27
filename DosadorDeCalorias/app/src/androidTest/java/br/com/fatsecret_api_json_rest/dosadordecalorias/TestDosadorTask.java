package br.com.fatsecret_api_json_rest.dosadordecalorias;

import android.test.AndroidTestCase;

import br.ufg.inf.dosador.api.*;
import br.ufg.inf.dosador.app.DosadorTask;
import br.ufg.inf.dosador.api.FatSecret;

/**
 * Created by Maycon on 25/03/2015.
 */
public class TestDosadorTask extends AndroidTestCase {
    static final String PESQUISA_EXPRESSAO = "arroz";
    static final String PESQUISA_ID = "35755";

    public void testPesquisarAlimentoPorID() {

        DosadorTask dt = new DosadorTask(getContext());

        dt.execute(FatSecret.METHOD_FOODS_SEARCH, PESQUISA_EXPRESSAO);



    }

}
