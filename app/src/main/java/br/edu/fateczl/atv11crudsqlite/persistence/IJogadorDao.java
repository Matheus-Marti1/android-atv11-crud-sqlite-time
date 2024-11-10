/*
 *@author:<Matheus Augusto Marti>
 */

package br.edu.fateczl.atv11crudsqlite.persistence;

import java.sql.SQLException;

public interface IJogadorDao {
    public JogadorDao open() throws SQLException;
    public void close();
}
