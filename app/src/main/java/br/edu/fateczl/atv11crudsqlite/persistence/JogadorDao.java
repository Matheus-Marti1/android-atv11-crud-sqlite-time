/*
 *@author:<Matheus Augusto Marti>
 */

package br.edu.fateczl.atv11crudsqlite.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.atv11crudsqlite.model.Jogador;
import br.edu.fateczl.atv11crudsqlite.model.Time;

public class JogadorDao implements IJogadorDao, ICRUDDao<Jogador> {
    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public JogadorDao(Context context){
        this.context = context;
    }
    @Override
    public JogadorDao open() throws SQLException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        database.insert("jogador", null, contentValues);
    }


    @Override
    public int update(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        return database.update("jogador", contentValues, "id = " + jogador.getId(), null);
    }

    @Override
    public void delete(Jogador jogador) throws SQLException {
        database.delete("jogador", "id = " + jogador.getId(), null);
    }

    @SuppressLint("Range")
    @Override
    public Jogador findOne(Jogador jogador) throws SQLException {
        String sql = "SELECT t.codigo AS cod_time, t.nome AS nome_time, t.cidade AS cid_time, " +
                "j.id, j.nome, j.data_nasc, j.altura, j.peso " +
                "FROM time t, jogador j " +
                "WHERE t.codigo = j.codigo_time " +
                "AND j.id = " + jogador.getId();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_time")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome_time")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cid_time")));

            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_nasc"))));
            jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
            jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
            jogador.setTime(time);
        }
        cursor.close();
        return jogador;
    }

    @SuppressLint("Range")
    @Override
    public List<Jogador> findAll() throws SQLException {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT t.codigo AS cod_time, t.nome AS nome_time, t.cidade AS cid_time, " +
                "j.id, j.nome, j.data_nasc, j.altura, j.peso " +
                "FROM time t, jogador j " +
                "WHERE t.codigo = j.codigo_time";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Jogador jogador = new Jogador();
            Time time = new Time();
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_time")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome_time")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cid_time")));

            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(LocalDate.parse(cursor.getString(cursor.getColumnIndex("data_nasc"))));
            jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
            jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));
            jogador.setTime(time);

            jogadores.add(jogador);
            cursor.moveToNext();
        }
        cursor.close();
        return jogadores;
    }

    private ContentValues getContentValues(Jogador jogador) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("id", jogador.getId());
        contentValue.put("nome", jogador.getNome());
        contentValue.put("data_nasc", String.valueOf(jogador.getDataNasc()));
        contentValue.put("altura", jogador.getAltura());
        contentValue.put("peso", jogador.getPeso());
        contentValue.put("codigo_time", jogador.getTime().getCodigo());
        return contentValue;
    }
}
