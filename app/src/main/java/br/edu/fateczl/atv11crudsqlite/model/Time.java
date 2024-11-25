/*
*@author:<Matheus Augusto Marti>
*/

package br.edu.fateczl.atv11crudsqlite.model;

import androidx.annotation.NonNull;

public class Time {
    private int codigo;
    private String nome;
    private String cidade;

    public Time() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @NonNull
    @Override
    public String toString() {
        return codigo + " - " + cidade + " - " + nome;
    }
}
