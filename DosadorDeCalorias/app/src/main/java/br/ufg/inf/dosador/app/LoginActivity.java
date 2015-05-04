package br.ufg.inf.dosador.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.TipoRefeicao;

public class LoginActivity extends ActionBarActivity {

    Button buttonEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPrimeiraTipoRefeicao();
                verificarPrimeiroAcesso();
            }
        });

        this.setTitle("Dosador de Calorias");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void verificarPrimeiroAcesso(){

        Cursor cursor = this.getContentResolver().query(
                DosadorContract.UsuarioEntry.CONTENT_URI,
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );
        if (cursor.moveToFirst()) {
            //Usuário já consta no cadastro, chama a tela de Resumo Diário de calorias.
            Intent intencao = new Intent(this,ResumoDiarioActivity.class);
            startActivity(intencao);
        } else {
            //Usuário não consta no cadastro, chama tela de cadastro de usuário
            Intent intencao = new Intent(this, UsuarioActivity.class);
            startActivity(intencao);
        }

    }

    public void verificarPrimeiraTipoRefeicao(){
        Cursor cursor = this.getContentResolver().query(
                DosadorContract.TipoRefeicaoEntry.CONTENT_URI,
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );
        if (cursor.moveToFirst()) {
            //Tipo de refeição já foi inserido na base de dados.

        } else {
            //Tipo de refeição não consta na base de dados.
            inserirTipoRefeicao();
        }
    }
    private void inserirTipoRefeicao() {
        Uri uri = this.getContentResolver().insert(DosadorContract.TipoRefeicaoEntry.CONTENT_URI, DosadorContract.createContentValuesTipoRefeicao(TipoRefeicao.CAFE_DA_MANHA.name()));
        uri = this.getContentResolver().insert(DosadorContract.TipoRefeicaoEntry.CONTENT_URI, DosadorContract.createContentValuesTipoRefeicao(TipoRefeicao.ALMOCO.name()));
        uri = this.getContentResolver().insert(DosadorContract.TipoRefeicaoEntry.CONTENT_URI, DosadorContract.createContentValuesTipoRefeicao(TipoRefeicao.LANCHE.name()));
        uri = this.getContentResolver().insert(DosadorContract.TipoRefeicaoEntry.CONTENT_URI, DosadorContract.createContentValuesTipoRefeicao(TipoRefeicao.JANTAR.name()));

    }


}
