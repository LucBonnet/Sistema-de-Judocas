package test.java.org.fpij.jitakyoei.model.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;

import main.java.org.fpij.jitakyoei.model.beans.Aluno;
import main.java.org.fpij.jitakyoei.model.beans.Endereco;
import main.java.org.fpij.jitakyoei.model.beans.Entidade;
import main.java.org.fpij.jitakyoei.model.dao.DAO;
import main.java.org.fpij.jitakyoei.model.dao.DAOImpl;
import main.java.org.fpij.jitakyoei.util.DatabaseManager;

public class EntidadeDaoTest {
  private static DAO<Entidade> entidadeDao;
  private static Endereco endereco;

  @BeforeClass
  public static void setUp() {
    DatabaseManager.setEnviroment(DatabaseManager.TEST);

    endereco = new Endereco();
    endereco.setBairro("Dirceu");
    endereco.setCep("64078-213");
    endereco.setCidade("Teresina");
    endereco.setEstado("PI");
    endereco.setRua("Rua Des. Berilo Mota");

    Entidade entidade = new Entidade();
    entidade.setNome("Academia 1");
    entidade.setTelefone1("(88) 2802-1826");
    entidade.setTelefone2("(88) 98516-2167");
    entidade.setCnpj("60.580.350/0001-04");
    entidade.setEndereco(endereco);

    entidadeDao = new DAOImpl<Entidade>(Entidade.class);
  }

  public static void clearDatabase() {
    List<Entidade> all = entidadeDao.list();
    for (Entidade each : all) {
      entidadeDao.delete(each);
    }
    assertEquals(0, entidadeDao.list().size());
  }
}
