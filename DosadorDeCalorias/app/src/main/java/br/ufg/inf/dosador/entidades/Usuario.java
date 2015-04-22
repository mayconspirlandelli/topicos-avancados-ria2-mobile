package br.ufg.inf.dosador.entidades;

/**
 * Created by Renilson on 16/04/2015.
 */
public class Usuario {
    private int id;
    private String nome;
    private String sexo;
    private int idade;
    private Double peso;
    private Double altura;

    public Usuario(int id, String nome, String sexo, int idade, Double peso, Double altura){
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.peso = peso;
        this.altura = altura;
    }

    public Usuario(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    @Override
    public String toString(){
        return "Id: " + id + " Nome: " + nome + " Sexo: " + sexo + " Idade: " + idade;
    }
}
