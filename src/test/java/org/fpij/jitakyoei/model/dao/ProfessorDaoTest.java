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
  private static DAO<Aluno> alunoDao;
  private static Aluno aluno;

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

    Filiado f2 = new Filiado();
    f2.setNome("Aécio");
    f2.setCpf("036.464.453-27");
    f2.setDataNascimento(new Date());
    f2.setDataCadastro(new Date());
    f2.setId(80921L);
    f2.setEndereco(endereco);

    professor = new Professor();
    professor.setFiliado(f1);
    professor.setEntidades(entidades);

    aluno = new Aluno();
    aluno.setFiliado(f2);
    aluno.setEntidade(e1);

    professorDao = new DAOImpl<Professor>(Professor.class);
    alunoDao = new DAOImpl<Aluno>(Aluno.class);
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

  @Test
  public void atualizarProfessorTest() {
    clearDatabase();

    professorDao.save(professor);

    assertEquals("Matheus", professor.getFiliado().getNome());
    assertEquals(1, professorDao.list().size());

    Filiado f2 = new Filiado();
    f2.copyProperties(f1);

    professor.setFiliado(f2);
    professor.getFiliado().setNome("Nome");
    professorDao.save(professor);

    assertEquals("Nome", professorDao.get(professor).getFiliado().getNome());
    assertEquals(1, professorDao.list().size());

    professor.setFiliado(f1);
  }

  @Test
  public void atualizarEntidadesDoProfessorTest() {
    clearDatabase();

    Professor professor1 = new Professor();
    professor1.setFiliado(f1);

    List<Entidade> entidades1 = new ArrayList<>();
    entidades1.add(entidades.get(0));
    entidades1.add(entidades.get(1));

    professor1.setEntidades(entidades1);
    professorDao.save(professor1);

    assertEquals(2, professorDao.get(professor1).getEntidades().size());
    assertEquals(1, professorDao.list().size());

    Entidade e1 = new Entidade();
    e1.setNome("Academia 3");
    e1.setTelefone1("(33)33333-3333");

    Professor p1 = professorDao.get(professor1);
    p1.getEntidades().add(e1);

    professorDao.save(p1);

    assertEquals(3, professorDao.get(professor1).getEntidades().size());
    assertEquals(1, professorDao.list().size());

    p1 = professorDao.get(professor1);
    p1.getEntidades().remove(0);
    professorDao.save(p1);

    assertEquals(2, professorDao.get(professor1).getEntidades().size());
    assertEquals("Academia 2", professorDao.get(professor1).getEntidades().get(0).getNome());
    assertEquals(1, professorDao.list().size());
  }

  @Test
  public void buscarProfessorTest() {
    clearDatabase();
    professorDao.save(professor);

    Filiado f = new Filiado();
    f.setNome("Matheus");
    Professor p = new Professor();
    p.setFiliado(f);

    List<Professor> result = professorDao.search(p);
    assertEquals(1, result.size());
    assertEquals("206.561.170-79", result.get(0).getFiliado().getCpf());

    f = new Filiado();
    f.setNome("Mathe");
    p = new Professor();
    p.setFiliado(f);
    result = professorDao.search(p);
    assertEquals(0, result.size());

    clearDatabase();
    assertEquals(0, professorDao.search(p).size());
  }

  @Test
  public void adicionarProfessorQueJaEraAlunoTest() {
    clearDatabase();

    alunoDao.save(aluno);
    Professor p1 = new Professor();
    p1.setFiliado(aluno.getFiliado());

    professorDao.save(p1);
    assertEquals("Aécio", professorDao.get(p1).getFiliado().getNome());
  }
}
