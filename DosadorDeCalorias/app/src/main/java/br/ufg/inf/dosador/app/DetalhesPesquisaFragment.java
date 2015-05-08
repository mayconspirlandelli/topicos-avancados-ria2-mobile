package br.ufg.inf.dosador.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import br.ufg.inf.dosador.R;
import br.ufg.inf.dosador.Util;
import br.ufg.inf.dosador.api.Json;
import br.ufg.inf.dosador.dao.ConsumoDAO;
import br.ufg.inf.dosador.entidades.Alimento;
import br.ufg.inf.dosador.entidades.Consumo;

/**
 * Created by Maycon on 09/04/2015.
 */
public class DetalhesPesquisaFragment extends Fragment {

    private static final String LOG_CAT = DetalhesPesquisaFragment.class.getSimpleName();
    public static final String TAG_DETALHE = "tagDetalhe";
    public static final String EXTRA_ALIMENTO = "alimento";

    private TextView txtFoodId;
    private TextView txtConsumoId;
    private EditText editDescricao;
    private TextView txtCalorias;
    private TextView txtGorduras;
    private TextView txtCarboidratos;
    private TextView txtProteinas;
    private EditText editPorcao;
    private EditText editQuantidade;
    private Button btnMaisDetalhes;

    private TextView txtTipoRefeicao;

    private ConsumoDAO consumoDAO;

    public DetalhesPesquisaFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        consumoDAO = new ConsumoDAO(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle parametros = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_detalhes_pesquisa, container, false);

        txtFoodId = (TextView) rootView.findViewById(R.id.txt_food_id);
        txtConsumoId = (TextView) rootView.findViewById(R.id.txt_consumo_id);
        editDescricao = (EditText) rootView.findViewById(R.id.edit_descricao);
        txtCalorias = (TextView) rootView.findViewById(R.id.txt_calorias_valor);
        txtGorduras = (TextView) rootView.findViewById(R.id.txt_gorduras_valor);
        txtCarboidratos = (TextView) rootView.findViewById(R.id.txt_carboidratos_valor);
        txtProteinas = (TextView) rootView.findViewById(R.id.txt_proteinas_valor);
        editPorcao = (EditText) rootView.findViewById(R.id.edit_porcao);
        editQuantidade = (EditText) rootView.findViewById(R.id.edit_quantidade);
        btnMaisDetalhes = (Button) rootView.findViewById(R.id.btn_mais_detalhes);
        btnMaisDetalhes.setOnClickListener(btnMaisDetalhesOnClickListener);

        txtTipoRefeicao = (TextView) rootView.findViewById(R.id.txt_tipo_refeicao);

        Alimento alimento = null;
        Consumo consumo = null;

        if (parametros != null) {
            if (parametros.getSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO) instanceof Consumo) {
                //objeto é uma instancia da Classe Consumo.
                consumo = (Consumo) parametros.getSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO);
            } else if (parametros.getSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO) instanceof Alimento) {
                //objeto é uma instancia da Classe Alimento.
                alimento = (Alimento) parametros.getSerializable(DetalhesPesquisaFragment.EXTRA_ALIMENTO);
            }
            if (alimento != null) {
                preencheComponentesDeTelaAlimento(alimento);
            } else if (consumo != null) {
                preencheComponentesDeTelaConsumo(consumo);
            }
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
                // NavUtils.navigateUpFromSameTask(getActivity());
                fecharActivity();
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

    private void preencheComponentesDeTelaAlimento(Alimento ali) {
        Alimento alimento = null;

        if (ali != null) {
            if (ali.getFood_description() != null && !ali.getFood_description().isEmpty()) {
                alimento = Util.obterDadosFromDescription(ali);
            } else {
                alimento = ali;
            }
            editDescricao.setText(alimento.getFood_name());
            txtCalorias.setText(alimento.getCalories().toString());
            txtGorduras.setText(alimento.getFat().toString());
            txtCarboidratos.setText(alimento.getCarbohydrate().toString());
            txtProteinas.setText(alimento.getProtein().toString());
            editPorcao.setText(alimento.getServing_description().toString());
            txtFoodId.setText(String.valueOf(alimento.getFood_id()));
            txtTipoRefeicao.setText(obtemTipoRefeifeicaoSharedPref());
        }

    }

    private void preencheComponentesDeTelaConsumo(Consumo consumo) {
        if (consumo != null) {
            editDescricao.setText(consumo.getFood_name());
            txtCalorias.setText(consumo.getCalories().toString());
            txtGorduras.setText(consumo.getFat().toString());
            txtCarboidratos.setText(consumo.getCarbohydrate().toString());
            txtProteinas.setText(consumo.getProtein().toString());
            editQuantidade.setText(String.valueOf(consumo.getQuantidade()));
            editPorcao.setText(consumo.getServing_description().toString());
            txtConsumoId.setText(String.valueOf(consumo.getId()));
            txtFoodId.setText(String.valueOf(consumo.getFood_id())); //Para exibir mais detalhes do alimento, temos que salvar o FOOD_ID que é a identificação do alimento na API.
            txtTipoRefeicao.setText(consumo.getTipoRefeicao());
        }
    }

    private String obtemTipoRefeifeicaoSharedPref() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences("teste", Context.MODE_PRIVATE);
        return sharedPref.getString("teste", null);
    }

    final private View.OnClickListener btnMaisDetalhesOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mostrarMaisDetalhes();
        }
    };

    private void mostrarMaisDetalhes() {
        int id = Integer.valueOf(txtFoodId.getText().toString());
        Intent intent = new Intent(getActivity(), DetalhesAlimentoActivity.class);
        intent.putExtra(Json.FOOD_ID, id);
        startActivity(intent);
    }

    private void salvarConsumo() {
        String data = Util.obterDataAtualToString();
        Consumo consumo = new Consumo();

        if (validarDados()) {

            if (txtConsumoId.getText().toString().isEmpty()) {
                consumo.setId(0);
            } else {
                consumo.setId(Integer.valueOf(txtConsumoId.getText().toString()));
            }
            consumo.setFood_name(editDescricao.getText().toString());
            consumo.setCalories(Double.valueOf(txtCalorias.getText().toString()));
            consumo.setFat(Double.valueOf(txtGorduras.getText().toString()));
            consumo.setCarbohydrate(Double.valueOf(txtCarboidratos.getText().toString()));
            consumo.setProtein(Double.valueOf(txtProteinas.getText().toString()));
            consumo.setQuantidade(Integer.valueOf(editQuantidade.getText().toString()));
            consumo.setData(data);
            consumo.setServing_description(editPorcao.getText().toString());
            consumo.setFood_id(Integer.valueOf(txtFoodId.getText().toString()));
            consumo.setTipoRefeicao(txtTipoRefeicao.getText().toString());
            consumoDAO.salvar(consumo);
            Log.d(LOG_CAT, "Dados salvo com sucesso!");

            fecharActivity();
        }
    }

    private boolean validarDados() {
        if (editDescricao.getText().toString().isEmpty() || editDescricao.getText().toString().equals("")) {
            Util.campoObrigatorio(getActivity(), "Informe a descrição!");
            return false;
        }
        if (txtCalorias.getText().toString().isEmpty() || txtCalorias.getText().toString().equals("")) {
            Util.exibeAlerta(getActivity());
            return false;
        }
        if (txtGorduras.getText().toString().isEmpty() || txtGorduras.getText().toString().equals("")) {
            Util.exibeAlerta(getActivity());
            return false;
        }
        if (txtCarboidratos.getText().toString().isEmpty() || txtCarboidratos.getText().toString().equals("")) {
            Util.exibeAlerta(getActivity());
            return false;
        }
        if (txtProteinas.getText().toString().isEmpty() || txtProteinas.getText().toString().equals("")) {
            Util.exibeAlerta(getActivity());
            return false;
        }
        if (editQuantidade.getText().toString().isEmpty() || editQuantidade.getText().toString().equals("")) {
            Util.campoObrigatorio(getActivity(), "Informe a quantidade!");
            return false;
        }
        return true;
    }

    private void exibirAlertaParaSalvar() {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_titulo))
                .setMessage(getString(R.string.dialog_mensagem_salvar_dieta))
                .setPositiveButton(getString(R.string.dialog_opcao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //salvar os dados.
                        salvarConsumo();
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

    private void fecharActivity() {
        getActivity().finish();
    }

}
