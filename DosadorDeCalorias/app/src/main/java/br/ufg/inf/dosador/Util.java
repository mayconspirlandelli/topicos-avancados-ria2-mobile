package br.ufg.inf.dosador;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 06/04/2015.
 */
public class Util {

    public static String obterDataAtual() {
        // set the format to sql date time
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    //TODO: implementar essa função.
    public static String obterMesAtual() {
        return null;
    }

    /**
     * Método responsável por obter os dados a partir da campo "Description" do Json retornoado pela API FatSecret.
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

}
