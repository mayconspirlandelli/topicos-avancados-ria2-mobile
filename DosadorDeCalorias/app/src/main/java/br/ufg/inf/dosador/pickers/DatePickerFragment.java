package br.ufg.inf.dosador.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;

/**
 * Created by Maycon on 05/05/2015.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DatePickerFragment.class.getSimpleName();


    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EditText editDataInicial = (EditText) getActivity().findViewById(R.id.editDataInicial);
        EditText editDataFinal = (EditText) getActivity().findViewById(R.id.editDataFinal);
        StringBuilder stringBuilder;

        //Eh necessario somar mais 1 ao mês, pois o mes de janeiro inicia com zero.
        if (editDataInicial.isFocusable()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(year);
            stringBuilder.append("-");
            stringBuilder.append(monthOfYear + 1);
            stringBuilder.append("-");
            stringBuilder.append(dayOfMonth);

            String dataStr = Util.convertDataFormatToFormatInString(stringBuilder.toString(), "yyyy-MM-dd", "dd/MM/yyyy");
            //Converte a data
            String dataStrFormat = Util.convertDataFormatToFormatInString(stringBuilder.toString(), "yyyy-MM-dd", "yyyy-MM-dd");

            editDataInicial.setText(dataStr);
            editDataInicial.setHint(dataStrFormat);
            editDataInicial.setFocusable(false);
        }
        if (editDataFinal.isFocusable()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(year);
            stringBuilder.append("-");
            stringBuilder.append(monthOfYear + 1);
            stringBuilder.append("-");
            stringBuilder.append(dayOfMonth);

            String dataStr = Util.convertDataFormatToFormatInString(stringBuilder.toString(), "yyyy-MM-dd", "dd/MM/yyyy");
            String dataStrFormat = Util.convertDataFormatToFormatInString(stringBuilder.toString(), "yyyy-MM-dd", "yyyy-MM-dd");

            editDataFinal.setText(dataStr);
            editDataFinal.setHint(dataStrFormat);
            editDataFinal.setFocusable(false);
        }
    }
}
