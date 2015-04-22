package br.ufg.inf.dosador.entidades;

/**
 * Created by Maycon on 22/04/2015.
 */
public enum  TipoRefeicao {
    CAFE_DA_MANHA(1),
    ALMOCO(2),
    JANTAR(3),
    LANCHE(4);

    private int valor;

    /**
     * Construtor privado da classe.
     *
     * @param value Valor inteiro associado a cada tipo de status v�lido.
     */
    private TipoRefeicao(int value) {
        this.valor = value;
    }

    /**
     * Recupera o valor inteiro associado � constante enum.
     *
     * @return O valor inteiro associado � constante enum.
     */
    public int getValor() {
        return this.valor;
    }

    }
