package test.java.org.fpij.jitakyoei.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import main.java.org.fpij.jitakyoei.model.beans.Aluno;
import main.java.org.fpij.jitakyoei.model.beans.Endereco;
import main.java.org.fpij.jitakyoei.model.beans.Entidade;
import main.java.org.fpij.jitakyoei.model.beans.Faixa;
import main.java.org.fpij.jitakyoei.model.beans.Filiado;
import main.java.org.fpij.jitakyoei.model.beans.Professor;
import main.java.org.fpij.jitakyoei.model.beans.Rg;
import main.java.org.fpij.jitakyoei.model.dao.DAO;
import main.java.org.fpij.jitakyoei.model.dao.DAOImpl;
import main.java.org.fpij.jitakyoei.util.CorFaixa;
import main.java.org.fpij.jitakyoei.util.DatabaseManager;

public class AlunoDaoTest {

	private static DAO<Aluno> alunoDao;
	private static Aluno aluno;
	private static Entidade entidade;
	private static Endereco endereco;
	private static Filiado f1;
	private static Filiado filiadoProf;
	private static Professor professor;

	@BeforeClass
	public static void setUp() {
		DatabaseManager.setEnviroment(DatabaseManager.TEST);

		Rg rgAluno = new Rg();
		rgAluno.setNumero("400994100");
		rgAluno.setOrgaoExpedidor("SSP-SP");

		f1 = new Filiado();
		f1.setNome("Aécio");
		f1.setCpf("036.464.453-27");
		f1.setDataNascimento(new Date());
		f1.setDataCadastro(new Date());
		f1.setId(1332L);
		f1.setRg(rgAluno);
		f1.setTelefone1("(34) 85947-2837");
		f1.setTelefone2("(34) 85947-2125");

		List<Faixa> faixas = new ArrayList<>();
		Faixa faixa1 = new Faixa(CorFaixa.BRANCA, new Date());
		faixas.add(faixa1);
		f1.setFaixas(faixas);

		endereco = new Endereco();
		endereco.setBairro("Dirceu");
		endereco.setCep("64078-213");
		endereco.setCidade("Teresina");
		endereco.setEstado("PI");
		endereco.setRua("Rua Des. Berilo Mota");

		Rg rgProfessor = new Rg();
		rgProfessor.setNumero("494126100");
		rgProfessor.setOrgaoExpedidor("SSP-SP");

		filiadoProf = new Filiado();
		filiadoProf.setNome("Professor");
		filiadoProf.setCpf("036.464.453-27");
		filiadoProf.setDataNascimento(new Date());
		filiadoProf.setDataCadastro(new Date());
		filiadoProf.setId(3332L);
		filiadoProf.setEndereco(endereco);
		filiadoProf.setRg(rgProfessor);
		filiadoProf.setTelefone1("(34) 57192-2837");
		filiadoProf.setTelefone2("(34) 96823-2125");
		filiadoProf.setRegistroCbj("2024/001234-SP");

		professor = new Professor();
		professor.setFiliado(filiadoProf);

		entidade = new Entidade();
		entidade.setEndereco(endereco);
		entidade.setNome("Academia 1");
		entidade.setTelefone1("(086)1234-5432");

		aluno = new Aluno();
		aluno.setFiliado(f1);
		aluno.setProfessor(professor);
		aluno.setEntidade(entidade);

		alunoDao = new DAOImpl<Aluno>(Aluno.class);
	}

	public static void clearDatabase() {
		List<Aluno> all = alunoDao.list();
		for (Aluno each : all) {
			alunoDao.delete(each);
		}
		assertEquals(0, alunoDao.list().size());
	}

	@Test
	public void salvarAlunoComAssociacoesTest() throws Exception {
		clearDatabase();

		assertTrue(alunoDao.save(aluno));
		assertEquals("036.464.453-27", alunoDao.get(aluno).getFiliado().getCpf());
		assertEquals("Aécio", alunoDao.get(aluno).getFiliado().getNome());
		assertEquals("Professor", alunoDao.get(aluno).getProfessor().getFiliado().getNome());
		assertEquals("Dirceu", alunoDao.get(aluno).getProfessor().getFiliado().getEndereco().getBairro());
		assertEquals("Academia 1", alunoDao.get(aluno).getEntidade().getNome());
		assertEquals("(086)1234-5432", alunoDao.get(aluno).getEntidade().getTelefone1());
	}

	@Test
	public void salvarAlunoComInformacoesInsuficientesTest() {
		clearDatabase();

		Aluno a = new Aluno();
		Filiado f = new Filiado();
		a.setFiliado(f);
		assertFalse(alunoDao.save(a));

		assertEquals(0, alunoDao.list().size());
	}

	@Test
	public void atualizarAlunoTest() throws Exception {
		clearDatabase();
		assertEquals(0, alunoDao.list().size());

		Aluno aluno1 = new Aluno();
		Filiado f2 = new Filiado();
		f2.copyProperties(f1);

		aluno1.setFiliado(f2);
		aluno.setProfessor(professor);
		aluno.setEntidade(entidade);

		alunoDao.save(aluno1);
		assertEquals(1, alunoDao.list().size());
		assertEquals("Aécio", aluno.getFiliado().getNome());

		Aluno a1 = alunoDao.get(aluno1);
		a1.getFiliado().setNome("TesteUpdate");
		alunoDao.save(a1);

		Aluno a2 = alunoDao.get(a1);
		assertEquals("TesteUpdate", a2.getFiliado().getNome());
		assertEquals(1, alunoDao.list().size());
	}

	@Test
	public void listarEAdicionarAlunosTest() {
		int qtd = alunoDao.list().size();

		Aluno aluno1 = new Aluno();
		aluno1.setFiliado(f1);
		aluno1.setProfessor(professor);
		aluno1.setEntidade(entidade);
		alunoDao.save(aluno1);
		assertEquals(qtd + 1, alunoDao.list().size());

		Aluno aluno2 = new Aluno();
		aluno2.setFiliado(f1);
		aluno2.setProfessor(professor);
		aluno2.setEntidade(entidade);
		alunoDao.save(aluno2);
		assertEquals(qtd + 2, alunoDao.list().size());

		Aluno aluno3 = new Aluno();
		aluno3.setFiliado(f1);
		aluno3.setProfessor(professor);
		aluno3.setEntidade(entidade);
		alunoDao.save(aluno3);
		assertEquals(qtd + 3, alunoDao.list().size());

		Aluno aluno4 = new Aluno();
		aluno4.setFiliado(f1);
		aluno4.setProfessor(professor);
		aluno4.setEntidade(entidade);
		alunoDao.save(aluno4);
		assertEquals(qtd + 4, alunoDao.list().size());

		clearDatabase();
		assertEquals(0, alunoDao.list().size());

		alunoDao.save(aluno1);
		assertEquals(1, alunoDao.list().size());
	}

	@Test
	public void buscarAlunoCadastradoTest() throws Exception {
		clearDatabase();
		alunoDao.save(aluno);

		Filiado f = new Filiado();
		f.setNome("Aécio");
		Aluno a = new Aluno();
		a.setFiliado(f);

		List<Aluno> result = alunoDao.search(a);
		assertEquals(1, result.size());
		assertEquals("036.464.453-27", result.get(0).getFiliado().getCpf());

		clearDatabase();
	}

	@Test
	public void buscarAlunoNaoCadastradoTest() throws Exception {
		clearDatabase();
		alunoDao.save(aluno);

		Filiado f = new Filiado();
		f.setNome("João");
		Aluno a = new Aluno();
		a.setFiliado(f);

		List<Aluno> result = alunoDao.search(a);
		assertEquals(0, result.size());

		clearDatabase();
	}

	@Test
	public void compararAlunosIguaisTest() {
		clearDatabase();

		alunoDao.save(aluno);

		Filiado f1 = new Filiado();
		f1.setNome("Aécio");
		Aluno a1 = new Aluno();
		a1.setFiliado(f1);

		List<Aluno> result1 = alunoDao.search(a1);
		assertTrue(result1.get(0).equals(aluno));

		Aluno aluno2 = new Aluno();
		aluno2.setFiliado(f1);
		aluno2.setProfessor(professor);
		aluno2.setEntidade(entidade);
		alunoDao.save(aluno2);

		assertFalse(aluno2.equals(aluno));
	}
}
