package br.ufg.inf.dosador.temp;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Maycon on 02/03/2015.
 */
public class FoodTypeAdapter /*implements JsonSerializer<Food>, JsonDeserializer<Food>*/{
   /* Gson gson = new Gson();

    public JsonElement serialize(Food food, Type typeOfT, JsonSerializationContext context) {
        JSONObject json = new JSONObject();

        for (Serving serving : food.getServings()){
            json.putOpt(serving.getServing_id(), gson.toJson(serving));
        }
    }*/
}
