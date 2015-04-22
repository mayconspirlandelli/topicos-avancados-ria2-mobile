package br.ufg.inf.dosador.entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Maycon on 07/04/2015.
 */
public class Consumo  {

    private long id;
    private String nomeAlimento;
    private int quantidade;
    private Double calorias;
    private Double gorduras;
    private Double carboidratos;
    private Double proteinas;
    private String tipoRefeicao;
    private String data;

    public Consumo(){}

    public Consumo(String nomeAlimento, int quantidade, Double calorias, Double gorduras, Double carboidratos, Double proteinas, String tipoRefeicao, String data) {
        this.nomeAlimento = nomeAlimento;
        this.quantidade = quantidade;
        this.calorias = calorias;
        this.gorduras = gorduras;
        this.carboidratos = carboidratos;
        this.proteinas = proteinas;
        this.tipoRefeicao = tipoRefeicao;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeAlimento() {
        return nomeAlimento;
    }

    public void setNomeAlimento(String nomeAlimento) {
        this.nomeAlimento = nomeAlimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getCalorias() {
        return calorias;
    }

    public void setCalorias(Double calorias) {
        this.calorias = calorias;
    }

    public Double getGorduras() {
        return gorduras;
    }

    public void setGorduras(Double gorduras) {
        this.gorduras = gorduras;
    }

    public Double getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(Double carboidratos) {
        this.carboidratos = carboidratos;
    }

    public Double getProteinas() {
        return proteinas;
    }

    public void setProteinas(Double proteinas) {
        this.proteinas = proteinas;
    }

    public String getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(String tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
