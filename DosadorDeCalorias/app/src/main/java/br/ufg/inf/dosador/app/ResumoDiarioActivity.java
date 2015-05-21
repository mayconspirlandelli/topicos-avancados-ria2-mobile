package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;

public class ResumoDiarioActivity extends ActionBarActivity {
    TextView caloriasConsumidas;
    TextView caloriasRestante;
    private Double iCaloriasConsumidas = 0.00;
    private Double iCaloriasRestante = 2000.00;

    private AnimationDrawable graficoAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_diario);
        this.setTitle("Resumo Diário");

        caloriasConsumidas = (TextView) findViewById(R.id.textViewCaloriasConsumidas);
        caloriasRestante = (TextView) findViewById(R.id.textViewCaloriasRestantes);

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setBackgroundResource(R.drawable.animation_list);
        graficoAnimation = (AnimationDrawable) imageView2.getBackground();

    }

    //https://github.com/luciofm/nextlevel/blob/931c03c19a70605e6139c8347ad3665031840a2b/app/src/main/res/layout/fragment_touch_feedback_code.xml

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (graficoAnimation.isRunning()) {
                graficoAnimation.stop();
            } else {
                graficoAnimation.start();
            }

            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaCaloriasConsumidas();
    }

    private void atualizaCaloriasConsumidas() {
        iCaloriasConsumidas = caloriasDiariasConsumidas();
        iCaloriasRestante = iCaloriasRestante - iCaloriasConsumidas;

        if (iCaloriasRestante <= 0) {
            iCaloriasRestante = 0.0;
        }

        //Formatando para duas casas decimais
        DecimalFormat fmt = new DecimalFormat("0.00");
        //iCaloriasRestante = Double.valueOf(fmt.format(iCaloriasRestante));

        caloriasConsumidas.setText("Calorias Consumidas: " + Double.toString(iCaloriasConsumidas));
        caloriasRestante.setText("Calorias Restante: " + Double.toString(iCaloriasRestante));

    }

    //TODO: criar grafico em forma de pizza
    //http://android-er.blogspot.com.br/2014/08/display-google-charts-pie-chart-on.html
    //https://google-developers.appspot.com/chart/interactive/docs/gallery/piechart


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumo_diario, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_usuario:
                dadosUsuarioMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void dadosUsuarioMenu() {
        Intent intencao = new Intent(this, UsuarioActivity.class);
        startActivity(intencao);
    }

    public void consumoDiario(View v) {
        Intent intencao = new Intent(this, ConsumoDiarioActivity.class);
        startActivity(intencao);
    }

    public void relatorio(View v) {
        Intent intencao = new Intent(this, RelatorioActivity.class);
        startActivity(intencao);
    }


    private Double caloriasDiariasConsumidas() {
        Double calorias = 0.00;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String data = df.format(new Date());
        Cursor cursor = this.getContentResolver().query(
                DosadorContract.ConsumoEntry.buildConsumoPorData(data),
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );
        try {
            if (cursor.moveToFirst()) {
                do {
                    calorias = calorias + (cursor.getDouble(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_CALORIAS) * cursor.getInt(cursor.getColumnIndex(DosadorContract.ConsumoEntry.COLUMN_QTD))));
                    if (calorias <= 0) {
                        calorias = 0.0;
                    }

                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return calorias;

    }

}
