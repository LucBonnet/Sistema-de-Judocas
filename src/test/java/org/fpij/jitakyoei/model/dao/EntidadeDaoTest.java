package test.java.org.fpij.jitakyoei.model.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.org.fpij.jitakyoei.model.beans.Aluno;
import main.java.org.fpij.jitakyoei.model.beans.Endereco;
import main.java.org.fpij.jitakyoei.model.beans.Entidade;
import main.java.org.fpij.jitakyoei.model.dao.DAO;
import main.java.org.fpij.jitakyoei.model.dao.DAOImpl;
import main.java.org.fpij.jitakyoei.util.DatabaseManager;

public class EntidadeDaoTest {
  private static DAO<Entidade> entidadeDao;
  private static Endereco endereco;
  private static Entidade entidade;

  @BeforeClass
  public static void setUp() {
    DatabaseManager.setEnviroment(DatabaseManager.TEST);

    endereco = new Endereco();
    endereco.setBairro("Dirceu");
    endereco.setCep("64078-213");
    endereco.setCidade("Teresina");
    endereco.setEstado("PI");
    endereco.setRua("Rua Des. Berilo Mota");

    entidade = new Entidade();
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

  @Test
  public void adicionarEntidadeTest() {
    clearDatabase();

    assertEquals(0, entidadeDao.list().size());

    entidadeDao.save(entidade);

    assertEquals(1, entidadeDao.list().size());
    assertEquals("Academia 1", entidadeDao.get(entidade).getNome());
    assertEquals("(88) 2802-1826", entidadeDao.get(entidade).getTelefone1());
    assertEquals("(88) 98516-2167", entidadeDao.get(entidade).getTelefone2());
    assertEquals("60.580.350/0001-04", entidadeDao.get(entidade).getCnpj());

    assertEquals("Dirceu", entidadeDao.get(entidade).getEndereco().getBairro());
    assertEquals("64078-213", entidadeDao.get(entidade).getEndereco().getCep());
    assertEquals("PI", entidadeDao.get(entidade).getEndereco().getEstado());
    assertEquals("Rua Des. Berilo Mota", entidadeDao.get(entidade).getEndereco().getRua());
  }
}
