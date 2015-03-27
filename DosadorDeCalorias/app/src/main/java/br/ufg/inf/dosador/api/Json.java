package br.ufg.inf.dosador.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.ufg.inf.dosador.entidades.Alimento;

/**
 * Created by Maycon on 26/03/2015.
 */
public class Json {

    private final static String LOG_CAT = Json.class.getSimpleName();

    private final static String FOODS = "foods";
    private final static String FOOD = "food";
    private final static String SERVINGS = "servings";
    private final static String SERVING = "serving";

    private final static String FOOD_ID = "food_id";
    private final static String FOOD_NAME = "food_name";
    private final static String FOOD_TYPE = "food_type";
    private final static String BRAND_NAME = "brand_name";
    private final static String FOOD_URL = "food_url";
    private final static String FOOD_DESCRIPTION = "food_description";

    private final static String SERVING_ID = "serving_id";
    private final static String SERVING_DESCRIPTION = "serving_description";
    private final static String SERVING_URL = "serving_url";
    private final static String METRIC_SERVING_AMOUNT = "metric_serving_amount";
    private final static String METRIC_SERVING_UNIT = "metric_serving_unit";
    private final static String NUMBER_OF_UNITS = "number_of_units";
    private final static String MEASUREMENT_DESCRIPTION = "measurement_description";
    private final static String CALORIES = "calories";
    private final static String CARBOHYDRATE = "carbohydrate";
    private final static String PROTEIN = "protein";
    private final static String FAT = "fat";
    private final static String SATURATED_FAT = "saturated_fat";
    private final static String POLYUNSATURATED_FAT = "polyunsaturated_fat";
    private final static String MONOUNSATURATED_FAT = "monounsaturated_fat";
    private final static String TRANS_FAT = "trans_fat";
    private final static String CHOLESTEROL = "cholesterol";
    private final static String SODIUM = "sodium";
    private final static String POTASSIUM = "potassium";
    private final static String FIBER = "fiber";
    private final static String SUGAR = "sugar";
    private final static String VITAMIN_A = "vitamin_a";
    private final static String VITAMIN_C = "vitamin_c";
    private final static String CALCIUM = "calcium";
    private final static String IRON = "iron";


    public ArrayList<Alimento> obterListaAlimentoFromJson(String foodJsonStr) throws JSONException {

        ArrayList<Alimento> listaDeAlimentos = new ArrayList<Alimento>();
        Alimento alimento = null;

        try {
            JSONObject foodsJson = new JSONObject(foodJsonStr);
            JSONObject foods = foodsJson.getJSONObject(FOODS);
            JSONArray foodArray = foods.getJSONArray(FOOD);

            if (foodArray.length() <= 0) {
                return listaDeAlimentos;
            }
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject food = foodArray.getJSONObject(i);
                alimento = new Alimento();
                alimento.setFood_id(food.getInt(FOOD_ID));
                alimento.setFood_name(food.getString(FOOD_NAME));
                alimento.setFood_description(food.getString(FOOD_DESCRIPTION));
                listaDeAlimentos.add(alimento);
            }

            Log.d(LOG_CAT, "LIsta: " + listaDeAlimentos.size());

        } catch (JSONException e) {
            Log.e(LOG_CAT, e.getMessage(), e);
            e.printStackTrace();
        }
        return listaDeAlimentos;
    }

    public Alimento obterAlimentoFromJson(String foodJsonStr) {

        Alimento alimento = null;

        try {
            JSONObject foodsJson = new JSONObject(foodJsonStr);
            JSONObject food = foodsJson.getJSONObject(FOOD);

            alimento = new Alimento();
            alimento.setFood_id(food.getInt(FOOD_ID));
            alimento.setFood_name(food.getString(FOOD_NAME));

            JSONObject servings = food.getJSONObject(SERVINGS);
            JSONArray servingArray = servings.getJSONArray(SERVING);

            if (servingArray.length() <= 0) {
                return null;
            }
            JSONObject serving = servingArray.getJSONObject(0);
            alimento.setCalories(serving.getDouble(CALORIES));
            alimento.setFat(serving.getDouble(FAT));
            alimento.setCarbohydrate(serving.getDouble(CARBOHYDRATE));
            alimento.setProtein(serving.getDouble(PROTEIN));

//calories - cals
//fat - gord
//carbohydrate - carbs
//protein - prot


            Log.d(LOG_CAT, "LIsta: " + "sxfvdsafsa");

        } catch (JSONException e) {
            Log.e(LOG_CAT, e.getMessage(), e);
            e.printStackTrace();
        }
        return alimento;
    }
}
