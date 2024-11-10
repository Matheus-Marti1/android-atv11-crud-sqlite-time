/*
 *@author:<Matheus Augusto Marti>
 */

package br.edu.fateczl.atv11crudsqlite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import br.edu.fateczl.atv11crudsqlite.controller.JogadorController;
import br.edu.fateczl.atv11crudsqlite.controller.TimeController;
import br.edu.fateczl.atv11crudsqlite.model.Jogador;
import br.edu.fateczl.atv11crudsqlite.model.Time;
import br.edu.fateczl.atv11crudsqlite.persistence.JogadorDao;
import br.edu.fateczl.atv11crudsqlite.persistence.TimeDao;

public class JogadorFragment extends Fragment {

    private View view;
    private EditText etIdJogador, etNomeJogador, etDataNascJogador, etAlturaJogador, etPesoJogador;
    private Spinner spTimeJogador;
    private Button btnInserirJogador, btnModificarJogador, btnExcluirJogador, btnListarJogador, btnBuscarJogador;
    private TextView tvListarJogador;
    private JogadorController jCont;
    private TimeController tCont;
    private List<Time> times;

    public JogadorFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_jogador, container, false);
        etIdJogador = view.findViewById(R.id.etIdJogador);
        etNomeJogador = view.findViewById(R.id.etNomeJogador);
        etDataNascJogador = view.findViewById(R.id.etDataNascJogador);
        etAlturaJogador = view.findViewById(R.id.etAlturaJogador);
        etPesoJogador = view.findViewById(R.id.etPesoJogador);
        spTimeJogador = view.findViewById(R.id.spTimeJogador);
        btnInserirJogador = view.findViewById(R.id.btnInserirJogador);
        btnModificarJogador = view.findViewById(R.id.btnModificarJogador);
        btnExcluirJogador = view.findViewById(R.id.btnExcluirJogador);
        btnListarJogador = view.findViewById(R.id.btnListarJogador);
        btnBuscarJogador = view.findViewById(R.id.btnBuscarJogador);
        tvListarJogador = view.findViewById(R.id.tvListarJogador);

        jCont = new JogadorController(new JogadorDao(view.getContext()));
        tCont = new TimeController(new TimeDao(view.getContext()));
        preencheSpinner();

        btnInserirJogador.setOnClickListener(op -> acaoInserir());
        btnModificarJogador.setOnClickListener(op -> acaoModificar());
        btnExcluirJogador.setOnClickListener(op -> acaoExcluir());
        btnBuscarJogador.setOnClickListener(op -> acaoBuscar());
        btnListarJogador.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoInserir() {
        int spPos = spTimeJogador.getSelectedItemPosition();
        if (spPos > 0) {
            Jogador jogador = montaJogador();
            try {
                jCont.inserir(jogador);
                Toast.makeText(view.getContext(), "Jogador inserido com sucesso", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        } else {
            Toast.makeText(view.getContext(), "Selecione um time", Toast.LENGTH_LONG).show();
        }
    }

    private void acaoModificar() {
        int spPos = spTimeJogador.getSelectedItemPosition();
        if (spPos > 0) {
            Jogador jogador = montaJogador();
            try {
                jCont.modificar(jogador);
                Toast.makeText(view.getContext(), "Jogador atualizado com sucesso", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        } else {
            Toast.makeText(view.getContext(), "Selecione um time", Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Jogador jogador = montaJogador();
        try {
            jCont.deletar(jogador);
            Toast.makeText(view.getContext(), "Jogador excluído com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void acaoBuscar() {
        Jogador jogador = montaJogador();
        try {
            times = tCont.listar();
            jogador = jCont.buscar(jogador);
            if (jogador.getNome() != null) {
                preencheCampos(jogador);
            } else {
                Toast.makeText(view.getContext(), "Jogador não encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try {
            List<Jogador> jogadores = jCont.listar();
            StringBuffer buffer = new StringBuffer();
            for (Jogador j : jogadores) {
                buffer.append(j.toString() + "\n");
            }
            tvListarJogador.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void preencheSpinner() {
        Time t0 = new Time();
        t0.setCodigo(0);
        t0.setNome("Selecione um time");
        t0.setCidade("");
        try {
            times = tCont.listar();
            times.add(0, t0);
            ArrayAdapter ad = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, times);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spTimeJogador.setAdapter(ad);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Jogador montaJogador() {
        Jogador j = new Jogador();
        j.setId(Integer.parseInt(etIdJogador.getText().toString()));
        j.setNome(etNomeJogador.getText().toString());
        String dataTexto = etDataNascJogador.getText().toString();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate data = LocalDate.parse(dataTexto, formato);
        j.setDataNasc(data);
        j.setAltura(Float.parseFloat(etAlturaJogador.getText().toString()));
        j.setPeso(Float.parseFloat(etPesoJogador.getText().toString()));
        j.setTime((Time) spTimeJogador.getSelectedItem());
        return j;
    }

    private void preencheCampos(Jogador j) {
        etIdJogador.setText(String.valueOf(j.getId()));
        etNomeJogador.setText(j.getNome());
        etDataNascJogador.setText(String.valueOf(j.getDataNasc()));
        etAlturaJogador.setText((int) j.getAltura());
        etPesoJogador.setText((int) j.getPeso());

        int cont = 1;
        for (Time t : times) {
            if (t.getCodigo() == j.getTime().getCodigo()) {
                spTimeJogador.setSelection(cont);
            } else {
                cont++;
            }
        }
        if (cont > times.size()) {
            spTimeJogador.setSelection(0);
        }
    }

    private void limpaCampos() {
        etIdJogador.setText("");
        etNomeJogador.setText("");
        etDataNascJogador.setText("");
        etAlturaJogador.setText("");
        etPesoJogador.setText("");
        spTimeJogador.setSelection(0);
    }
}