package br.ufg.inf.dosador;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maycon on 06/04/2015.
 */
public class Util {

    public String getData() {
        // set the format to sql date time
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

}
