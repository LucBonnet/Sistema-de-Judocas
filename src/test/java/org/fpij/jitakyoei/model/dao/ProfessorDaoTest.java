package test.java.org.fpij.jitakyoei.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.org.fpij.jitakyoei.model.beans.Endereco;
import main.java.org.fpij.jitakyoei.model.beans.Entidade;
import main.java.org.fpij.jitakyoei.model.beans.Filiado;
import main.java.org.fpij.jitakyoei.model.beans.Professor;
import main.java.org.fpij.jitakyoei.model.dao.DAO;
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
  }
}
