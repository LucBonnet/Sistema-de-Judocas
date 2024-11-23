package test.java.org.fpij.jitakyoei.model.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.org.fpij.jitakyoei.model.beans.Aluno;
import main.java.org.fpij.jitakyoei.model.beans.Endereco;
import main.java.org.fpij.jitakyoei.model.beans.Entidade;
import main.java.org.fpij.jitakyoei.model.beans.Filiado;
import main.java.org.fpij.jitakyoei.model.beans.Professor;
import main.java.org.fpij.jitakyoei.model.dao.DAO;
import main.java.org.fpij.jitakyoei.model.dao.DAOImpl;
import main.java.org.fpij.jitakyoei.util.DatabaseManager;

public class ProfessorDaoTest {
  private static DAO<Professor> professorDao;
  private static Professor professor;
  private static Filiado f1;
  private static List<Entidade> entidades;
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

    f1 = new Filiado();
    f1.setNome("Matheus");
    f1.setCpf("206.561.170-79");
    f1.setDataNascimento(new Date());
    f1.setDataCadastro(new Date());
    f1.setId(87131L);
    f1.setEndereco(endereco);

    entidades = new ArrayList<>();

    Entidade e1 = new Entidade();
    e1.setEndereco(endereco);
    e1.setNome("Academia 1");
    e1.setTelefone1("(11)11111-1111");
    entidades.add(e1);

    Entidade e2 = new Entidade();
    e2.setEndereco(endereco);
    e2.setNome("Academia 2");
    e2.setTelefone1("(22)22222-2222");
    entidades.add(e2);

    professor = new Professor();
    professor.setFiliado(f1);
    professor.setEntidades(entidades);

    professorDao = new DAOImpl<Professor>(Professor.class);
  }

  public static void clearDatabase() {
    List<Professor> all = professorDao.list();
    for (Professor each : all) {
      professorDao.delete(each);
    }
    assertEquals(0, professorDao.list().size());
  }

  @Test
  public void salvarProfessorTest() {
    clearDatabase();

    assertEquals(professorDao.list().size(), 0);

    professorDao.save(professor);

    assertEquals(professorDao.list().size(), 1);

    assertEquals("Matheus", professorDao.get(professor).getFiliado().getNome());
    assertEquals("206.561.170-79", professorDao.get(professor).getFiliado().getCpf());

    assertEquals("Dirceu", professorDao.get(professor).getFiliado().getEndereco().getBairro());
    assertEquals("64078-213", professorDao.get(professor).getFiliado().getEndereco().getCep());
    assertEquals("Teresina", professorDao.get(professor).getFiliado().getEndereco().getCidade());
    assertEquals("PI", professorDao.get(professor).getFiliado().getEndereco().getEstado());
    assertEquals("Rua Des. Berilo Mota", professorDao.get(professor).getFiliado().getEndereco().getRua());

    assertEquals(2, professorDao.get(professor).getEntidades().size());

    assertEquals("Academia 1", professorDao.get(professor).getEntidades().get(0).getNome());
    assertEquals("Academia 2", professorDao.get(professor).getEntidades().get(1).getNome());
  }
}
