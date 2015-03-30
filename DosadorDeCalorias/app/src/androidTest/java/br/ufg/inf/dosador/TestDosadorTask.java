package br.com.fatsecret_api_json_rest.dosadordecalorias;

import android.test.AndroidTestCase;
import android.util.Log;

import java.net.URL;

import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.task.ListaAlimentoTask;
import br.ufg.inf.dosador.api.FatSecret;

/**
 * Created by Maycon on 25/03/2015.
 */
public class TestDosadorTask extends AndroidTestCase {
    static final String PESQUISA_EXPRESSAO = "arroz";
    static final String PESQUISA_ID = "35755";

//    public void testPesquisarAlimentoPorID() {
//        ListaAlimentoTask dt = new ListaAlimentoTask(getContext());
//        dt.execute(FatSecret.METHOD_FOODS_SEARCH, PESQUISA_EXPRESSAO);
//    }

    public void testVelocidadeJSON() {

        long tempoInicial = System.currentTimeMillis();
        //FatSecret antigo
        String requestString = br.ufg.inf.dosador.temp.FatSecret.getFood(PESQUISA_ID).toString();
        Log.d("TESTE", "Tempo: " +  (System.currentTimeMillis() - tempoInicial)+ " URL: " + requestString);


        tempoInicial = System.currentTimeMillis();
        br.ufg.inf.dosador.api.FatSecret api = new br.ufg.inf.dosador.api.FatSecret(mContext);
        URL url = FatSecret.pesquisarAlimentoPorID(PESQUISA_ID);
        Log.d("TESTE", "Tempo: " +  (System.currentTimeMillis() - tempoInicial)+ " URL: " + url.toString());

    }
}
