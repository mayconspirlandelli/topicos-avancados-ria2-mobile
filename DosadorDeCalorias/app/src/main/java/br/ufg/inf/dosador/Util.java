package br.ufg.inf.dosador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 06/04/2015.
 */
public class Util {

    private static final String LOG_TAG = Util.class.getSimpleName();

    //TODO: Passar o formato da data como parametro.
    public static String obterDataAtualToString() {
        // set the format to sql date time
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = DATE_FORMAT.format(date);
        return dateStr;
    }

    //TODO: implementar essa função.
    public static String obterMesAtual() {
        return null;
    }



    public static Calendar obterDataAtual() {
        Calendar data = Calendar.getInstance();
        return  data;
    }

    /**
     * Método responsável por obter a data do dia anterior ou do dia posterior.
     * @param calendar
     * @param anterior, se true decrementa a data, se false incrementa a data.
     * @return
     */
    public static Calendar getDiaAnteriorPosterior(Calendar calendar, boolean anterior) {
        if (anterior) {
            calendar.add(Calendar.DATE, -1);
        } else {
            calendar.add(Calendar.DATE, +1);
        }
        return calendar;
    }

    /**
     * Converte a data do tipo Calendar para o tipo String.
     */
    public static String conveteDataFromCalendarToString(Calendar calendar, String formato){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(formato);
        String dateStr = DATE_FORMAT.format(calendar.getTime());
        return dateStr;
    }



    /**
     * Método responsável por obter os dados a partir da campo "Description" do Json retornoado pela API FatSecret.
     *
     * @param alimento
     * @return
     */
    public static Alimento obterDadosFromDescription(Alimento alimento) {
        String description = alimento.getFood_description();
        //Exemplo "Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g"
        if (!description.isEmpty()) {
            String[] temp = description.split("-");
            String[] temp2 = temp[1].split("\\|");

            String calories = temp2[0].replaceAll("[^0-9.]", "");
            String fat = temp2[1].replaceAll("[^0-9.]", "");
            String carbs = temp2[2].replaceAll("[^0-9.]", "");
            String protein = temp2[3].replaceAll("[^0-9.]", "");

            alimento.setServing_description(temp[0]);
            alimento.setCalories(Double.parseDouble(calories));
            alimento.setFat(Double.parseDouble(fat));
            alimento.setCarbohydrate(Double.parseDouble(carbs));
            alimento.setProtein(Double.parseDouble(protein));
        }
        return alimento;
    }


    public static void campoObrigatorio(Context context, String texto) {
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
    }

    public static void exibeAlerta(Context context) {
        Toast.makeText(context, "Não foi possível salvar!", Toast.LENGTH_LONG);
    }

    public static void exibeAlerta(Context context, String texto) {
        Toast.makeText(context, texto, Toast.LENGTH_LONG);
    }

    public static boolean verificaConexaoDeRede(Context contexto) {
        boolean conectado = false;
        try {
            ConnectivityManager gerenciadorConectividade = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (gerenciadorConectividade.getActiveNetworkInfo() != null
                    && gerenciadorConectividade.getActiveNetworkInfo().isAvailable()
                    && gerenciadorConectividade.getActiveNetworkInfo().isConnected()) {
                conectado = true;
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Android não tem permissão de acesso a rede. "
                    + "Contate seu administrador do aplicativo! Menssagem de erro: " + e.toString());
            conectado = false;
        }
        return conectado;
    }




}
