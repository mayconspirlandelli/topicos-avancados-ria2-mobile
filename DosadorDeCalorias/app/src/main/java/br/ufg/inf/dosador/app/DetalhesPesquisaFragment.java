package br.ufg.inf.dosador.app;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.data.DosadorContract;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.entidades.Consumo;

/**
 * Created by Maycon on 09/04/2015.
 */
public class DetalhesPesquisaFragment extends Fragment {

    private static final String LOG_CAT = DetalhesPesquisaFragment.class.getSimpleName();
    public static final String TAG_DETALHE = "tagDetalhe";
    public static final String EXTRA_ALIMENTO = "alimento";

    private EditText editDescricao;
    private TextView txtCalorias;
    private TextView txtGorduras;
    private TextView txtCarboidratos;
    private TextView txtProteinas;
    private EditText editPorcao;
    private EditText editQuantidade;
    private Button btnMaisDetalhes;
    //TODO: somente para teste os dois botoes: salvar e consumodiario.
    private Button btnSalvar;
    private Button btnConsumoDiario;

    private Alimento mAlimento;

    public DetalhesPesquisaFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle parametros = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_detalhes_pesquisa, container, false);

        if (parametros != null) {
            mAlimento = (Alimento) parametros.getSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO);
        }

        if (mAlimento != null) {
            editDescricao = (EditText) rootView.findViewById(R.id.edit_descricao);
            txtCalorias = (TextView) rootView.findViewById(R.id.txt_calorias_valor);
            txtGorduras = (TextView) rootView.findViewById(R.id.txt_gorduras_valor);
            txtCarboidratos = (TextView) rootView.findViewById(R.id.txt_carboidratos_valor);
            txtProteinas = (TextView) rootView.findViewById(R.id.txt_proteinas_valor);
            editPorcao = (EditText) rootView.findViewById(R.id.edit_porcao);
            editQuantidade = (EditText) rootView.findViewById(R.id.edit_quantidade);
            btnMaisDetalhes = (Button) rootView.findViewById(R.id.btn_mais_detalhes);
            btnMaisDetalhes.setOnClickListener(btnMaisDetalhesOnClickListener);

            //TODO: botão salvar e consumo é somente para teste.
            btnSalvar = (Button) rootView.findViewById(R.id.btn_salvar);
            btnSalvar.setOnClickListener(btnSalvarOnClickListener);
            btnConsumoDiario = (Button) rootView.findViewById(R.id.btn_consumo_diario);
            btnConsumoDiario.setOnClickListener(btnConsumoDiarionClickListener);

            preencheComponentesDeTela(mAlimento);

        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detalhes_pesquisa, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void inicializaObjetosDeTela() {
//
//        editDescricao = (EditText) findViewById(R.id.edit_descricao);
//        txtCalorias = (TextView) findViewById(R.id.txt_calorias_valor);
//        txtGorduras = (TextView) findViewById(R.id.txt_gorduras_valor);
//        txtCarboidratos = (TextView) findViewById(R.id.txt_carboidratos_valor);
//        txtProteinas = (TextView) findViewById(R.id.txt_proteinas_valor);
//        editPorcao = (EditText) findViewById(R.id.edit_porcao);
//        editQuantidade = (EditText) findViewById(R.id.edit_quantidade);
//        btnMaisDetalhes = (Button) findViewById(R.id.btn_mais_detalhes);
//        btnMaisDetalhes.setOnClickListener(btnMaisDetalhesOnClickListener);
//
//        btnSalvar = (Button) findViewById(R.id.btn_salvar);
//        btnSalvar.setOnClickListener(btnSalvarOnClickListener);
//        btnConsumoDiario = (Button) findViewById(R.id.btn_consumo_diario);
//        btnConsumoDiario.setOnClickListener(btnConsumoDiarionClickListener);
//
//        Bundle valoresEntreActivity = getIntent().getExtras();
//        int id = valoresEntreActivity.getInt(Json.FOOD_ID);
//        String nome = valoresEntreActivity.getString(Json.FOOD_NAME);
//        String porcao = valoresEntreActivity.getString(Json.SERVING_DESCRIPTION);
//        Double calorias = valoresEntreActivity.getDouble(Json.CALORIES);
//        Double gorduras = valoresEntreActivity.getDouble(Json.FAT);
//        Double carbs = valoresEntreActivity.getDouble(Json.CARBOHYDRATE);
//        Double proteinas = valoresEntreActivity.getDouble(Json.PROTEIN);
//
//        editDescricao.setText(nome);
//        txtCalorias.setText(calorias.toString());
//        txtGorduras.setText(gorduras.toString());
//        txtCarboidratos.setText(carbs.toString());
//        txtProteinas.setText(proteinas.toString());
//        editPorcao.setText(porcao);
//
//    }

    private void preencheComponentesDeTela(Alimento ali){

        Alimento alimento = Util.obterDadosFromDescription(ali);

        editDescricao.setText(alimento.getFood_name());
        txtCalorias.setText(alimento.getCalories().toString());
        txtGorduras.setText(alimento.getFat().toString());
        txtCarboidratos.setText(alimento.getCarbohydrate().toString());
        txtProteinas.setText(alimento.getProtein().toString());
        editPorcao.setText(alimento.getServing_description().toString());


    }

    final private View.OnClickListener btnMaisDetalhesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mostrarMaisDetalhes();
        }
    };

    //TODO: corrigir depois que implementei DoisFragments temos  que pegar os dados dos Fragment e nao das Activity.
    private void mostrarMaisDetalhes() {
        Bundle valoresEntreActivity = getActivity().getIntent().getExtras();
        int id = valoresEntreActivity.getInt(Json.FOOD_ID);

        Intent intent = new Intent(getActivity(), DetalhesAlimentoActivity.class);
        intent.putExtra(Json.FOOD_ID, id);
        startActivity(intent);
    }

    //TODO: somente para teste.
    final private View.OnClickListener btnSalvarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            salvar();
        }
    };

    private void salvar() {
        String data = "2015-04-07";
        Consumo consumo = new Consumo();
        consumo.setNomeAlimento(editDescricao.getText().toString());
        consumo.setCalorias(Double.valueOf(txtCalorias.getText().toString()));
        consumo.setGorduras(Double.valueOf(txtGorduras.getText().toString()));
        consumo.setCarboidratos(Double.valueOf(txtCarboidratos.getText().toString()));
        consumo.setProteinas(Double.valueOf(txtProteinas.getText().toString()));
        consumo.setQuantidade(Integer.valueOf(editQuantidade.getText().toString()));
        consumo.setData(data);
        consumo.setTipoRefeicao("lanche");
        //TODO: faltou colocar tipo de refeição.
        //TODO: usar enum para representar o tipo de refeição.
        //TODO: usar enum para representar os 12 meses do alno.

        ContentValues values = DosadorContract.createContentValuesConsumo(consumo);
        getActivity().getContentResolver().insert(DosadorContract.ConsumoEntry.CONTENT_URI, values);

    }

    //TODO: somente para teste.
    final private View.OnClickListener btnConsumoDiarionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mostrarConsumoDiario();
        }
    };

    private void mostrarConsumoDiario() {
        Intent intent = new Intent(getActivity(), ConsumoDiarioActivity.class);
        startActivity(intent);
    }


}
