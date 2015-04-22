package br.ufg.inf.dosador.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.Usuario;

/**
 * Created by Renilson on 17/04/2015.
 */
public class UsuarioActivity extends ActionBarActivity {

    EditText editUsuario;
    EditText editSexo;
    EditText editIdade;
    EditText editPeso;
    EditText editAltura;
    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        //Título
        this.setTitle("Usuário");

        //Carregando o usuário cadastrado
        usuario = getUsuario();

        //Atribuição das variáveis
        editUsuario = (EditText) findViewById(R.id.editUsuario);
        editSexo = (EditText) findViewById(R.id.editSexo);
        editIdade = (EditText) findViewById(R.id.editIdade);
        editPeso = (EditText) findViewById(R.id.editPeso);
        editAltura = (EditText) findViewById(R.id.editAltura);

        editUsuario.setText(usuario.getNome());
        editSexo.setText(usuario.getSexo());
        editIdade.setText(Integer.toString(usuario.getIdade()));
        editPeso.setText(String.valueOf(usuario.getPeso()==null?"":usuario.getPeso()));
        editAltura.setText(String.valueOf(usuario.getAltura()==null?"":usuario.getAltura()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_post:
                salvar();
                return true;
            case R.id.action_delete:
                excluir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void salvar() {
        boolean validado = true;

        usuario.setNome(editUsuario.getText().toString());
        if (usuario.getNome().equals("")) {
            campoObrigatorio("Informe o nome do usuário!");
            validado = false;
        }

        usuario.setSexo(editSexo.getText().toString());
        if (usuario.getSexo().equals("")) {
            campoObrigatorio("Informe o sexo!");
            validado = false;
        }

        String idade = editIdade.getText().toString();
        if (idade.equals("")) {
            campoObrigatorio("Informe a Idade!");
            validado = false;
        } else {
            usuario.setIdade(Integer.valueOf(idade));
        }

        String peso = editPeso.getText().toString();
        if (peso.equals("")) {
            campoObrigatorio("Informe o Peso!");
            validado = false;
        } else {
            usuario.setPeso(Double.valueOf(peso));
        }

        String altura = editAltura.getText().toString();
        if (altura.equals("")) {
            campoObrigatorio("Informe a Altura!");
            validado = false;
        } else {
            usuario.setAltura(Double.valueOf(altura));
        }

        if (validado) {
            ContentValues values = DosadorContract.createContentValuesUsuario(usuario);
            if (usuario.getId() == 0)
                this.getContentResolver().insert(DosadorContract.UsuarioEntry.CONTENT_URI, values);
            else
                this.getContentResolver().update(DosadorContract.UsuarioEntry.CONTENT_URI, values, DosadorContract.UsuarioEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(usuario.getId())});

            Intent intencao = new Intent(this, ResumoDiarioActivity.class);
            startActivity(intencao);
        }

    }

    private void excluir(){
        this.getContentResolver().delete(DosadorContract.UsuarioEntry.CONTENT_URI, DosadorContract.UsuarioEntry.COLUMN_ID + " = ? ", new String[]{String.valueOf(usuario.getId())});

        //Intent intencao = new Intent(this,PesquisaActivity.class);
        Intent intencao = new Intent(this, LoginActivity.class);
        startActivity(intencao);
    }

    private Usuario getUsuario(){
        Usuario usuario = new Usuario();
        Cursor cursor = this.getContentResolver().query(
                DosadorContract.UsuarioEntry.CONTENT_URI,
                null,   // leaving "columns" null just returns all the columns.
                null,   // cols for "where" clause
                null,   // Values for the "where" clause
                null    // sort order
        );

        try {
            if (cursor.moveToFirst()) {
                do {

                    usuario.setId(cursor.getInt(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_ID)));
                    usuario.setNome(cursor.getString(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_NOME)));
                    usuario.setSexo(cursor.getString(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_SEXO)));
                    usuario.setIdade(cursor.getInt(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_IDADE)));
                    usuario.setPeso(cursor.getDouble(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_PESO)));
                    usuario.setAltura(cursor.getDouble(cursor.getColumnIndex(DosadorContract.UsuarioEntry.COLUMN_ALTURA)));

                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return usuario;
    }


    public void campoObrigatorio(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
