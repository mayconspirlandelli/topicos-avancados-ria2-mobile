package br.ufg.inf.dosador.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.dao.ConsumoDAO;
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
    private ConsumoDAO consumoDAO;

    public DetalhesPesquisaFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        consumoDAO = new ConsumoDAO(getActivity());
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
        inflater.inflate(R.menu.menu_detalhes_pesquisa, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                exibirAlertaParaSalvar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preencheComponentesDeTela(Alimento ali){
        Alimento alimento = Util.obterDadosFromDescription(ali);
        mAlimento = alimento;
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

    private void mostrarMaisDetalhes() {
        int id = mAlimento.getFood_id();
        Intent intent = new Intent(getActivity(), DetalhesAlimentoActivity.class);
        intent.putExtra(Json.FOOD_ID, id);
        startActivity(intent);
    }


    //TODO: somente para teste.
    final private View.OnClickListener btnSalvarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

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

    private void salvarConsumo(){
        String data = Util.obterDataAtual();
        Consumo consumo = new Consumo();

        if(validarDados()) {

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
            //TODO: usar enum para representar os 12 meses do ano.

            consumoDAO.salvar(consumo);
            Log.d(LOG_CAT, "Dados salvo com sucesso!");

        } else {
            //TODO: emiti Alerta ao usuario.
            Toast.makeText(getActivity(),"Não foi possível salvar!", Toast.LENGTH_LONG);
        }
    }

    private boolean validarDados(){
        if (editDescricao.getText().toString().isEmpty()) {
            return false;
        }
        if (txtCalorias.getText().toString().isEmpty()) {
            return false;
        }
        if (txtGorduras.getText().toString().isEmpty()) {
            return false;
        }
        if (txtCarboidratos.getText().toString().isEmpty()) {
            return false;
        }
        if (txtProteinas.getText().toString().isEmpty()) {
            return false;
        }
        if (editQuantidade.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void exibirAlertaParaSalvar(){
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_titulo))
                .setMessage(getString(R.string.dialog_mensagem))
                .setPositiveButton(getString(R.string.dialog_opcao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //salvar os dados.
                        salvarConsumo();
                        fecharActivity();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_opcao_nao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancela operação.
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void fecharActivity(){
        getActivity().finish();
    }

}
