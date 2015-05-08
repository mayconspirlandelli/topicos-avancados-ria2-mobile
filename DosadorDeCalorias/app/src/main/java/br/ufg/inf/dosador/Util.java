package br.ufg.inf.dosador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
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
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = DATE_FORMAT.format(date);
        return dateStr;
    }

    public static Calendar obterMesAtual() {
        Calendar data = Calendar.getInstance();
        data.get(Calendar.MONTH);
        //data.add(Calendar.MONTH, 0);
        return data;
    }

    /**
     * Método responsável por obter o Mês em String
     *
     * @param formato M -> 9
     *                MM -> 09
     *                MMM -> Sep
     *                MMMM -> September
     * @return
     */
    public static String obterMesAtualToString(String formato) {
        Calendar data = obterMesAtual();
        String dataSt = convertDataFromCalendarToString(data, formato);
        return dataSt;
    }

    /**
     * Método responsável por obter a data do dia anterior ou do dia posterior.
     *
     * @param calendar
     * @param anterior, se true decrementa a data, se false incrementa a data.
     * @return
     */
    public static Calendar getMesAnteriorPosterior(Calendar calendar, boolean anterior) {
        if (anterior) {
            calendar.add(Calendar.MONTH, -1);
        } else {
            calendar.add(Calendar.MONTH, +1);
        }
        return calendar;
    }

    public static Calendar obterDataAtual() {
        Calendar data = Calendar.getInstance();
        return data;
    }

    /**
     * Método responsável por obter a data do dia anterior ou do dia posterior.
     *
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
    public static String convertDataFromCalendarToString(Calendar calendar, String formato) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(formato);
        String dateStr = DATE_FORMAT.format(calendar.getTime());
        return dateStr;
    }

    /**
     * Converte a data do tipo String para o tipo Calendar.
     *
     * @param data    em String
     * @param formato exemplo yyyy-MM-dd ou dd/MM/yyyy
     * @return Calendar
     */
    public static Calendar convertDataFromStringToCalendar(String data, String formato) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(formato);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.YEAR, 0);
        try {
            calendar.setTime(DATE_FORMAT.parse(data));
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error: " + e.toString());
        }

        return calendar;
    }

    /**
     * Converte a data de um formato para outro, exemplo: yyyy-MM-dd para dd/MM/yyyy.
     *
     * @param data
     * @param formatoEntrada exemplo yyyy-MM-dd
     * @param formatoSaida   exemplo dd/MM/yyyy
     * @return Data formatad de acordo com o formatoSaida.
     */
    public static String convertDataFormatToFormatInString(String data, String formatoEntrada, String formatoSaida) {
        Calendar calendar = Util.convertDataFromStringToCalendar(data, formatoEntrada);
        String dataStr = Util.convertDataFromCalendarToString(calendar, formatoSaida);
        return dataStr;
    }


    public static boolean compararDatas(String dataInicial, String dataFinal, String formato) {
        Calendar calendarInicial = convertDataFromStringToCalendar(dataInicial, formato);
        Calendar calendarFinal = convertDataFromStringToCalendar(dataFinal, formato);

        if (calendarInicial.before(calendarFinal) || calendarInicial.equals(calendarFinal)) {
            return true;
        }
//
//        calendarInicial.set(Calendar.MONTH, 0);
//        calendarInicial.set(Calendar.DAY_OF_MONTH, 0);
//        calendarInicial.set(Calendar.YEAR, 0);
//
//        calendarFinal.set(Calendar.MONTH, 0);
//        calendarFinal.set(Calendar.DAY_OF_MONTH, 0);
//        calendarFinal.set(Calendar.YEAR, 0);

        return false;
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
        Log.d(LOG_TAG, "getFood_description: " + description);

        if (!description.isEmpty()) {
            String[] temp = description.split(" - ");
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

    public static String[] obterDadosFromDescription(String description) {
        String[] resultado = {"", ""};

        //Exemplo "Per 100g - Calories: 89kcal | Fat: 0.33g | Carbs: 22.84g | Protein: 1.09g"
        Log.d(LOG_TAG, "getFood_description: " + description);

        if (!description.isEmpty()) {
            String[] temp = description.split(" - ");
            String[] temp2 = temp[1].split("\\|");

            resultado[0] = temp[0];
            resultado[1] = temp2[0].replaceAll("[^0-9.]", "");

        }
        return resultado;
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
