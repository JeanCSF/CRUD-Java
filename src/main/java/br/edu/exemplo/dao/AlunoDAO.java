package br.edu.exemplo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.exemplo.model.Aluno;
import br.edu.exemplo.util.ConnectionFactory;

public class AlunoDAO {
	// classes de banco de dados
	private Connection conn;   // abre a conexao do banco de dados
	private PreparedStatement ps;  // permite que scripts SQL sejam executados a partir do Java
	private ResultSet rs;  // representa as tabelas
	// classe JavaBean
	private Aluno aluno;

	public AlunoDAO() throws Exception {
		// chama a classe ConnectionFactory e estabele uma conexao
		try {
			this.conn = ConnectionFactory.getConnection();
		} catch (Exception e) {
			throw new Exception("erro: \n" + e.getMessage());
		}
	}

	// metodo de salvar

	public void salvar(Aluno aluno) throws Exception {
		if (aluno == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "INSERT INTO alunos (ra, nome, email, endereco, datanascimento, periodo) values "
					+ "(?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, aluno.getRa());
			ps.setString(2, aluno.getNome());
			ps.setString(3, aluno.getEmail());
			ps.setString(4, aluno.getEndereco());
			ps.setDate(5, new java.sql.Date(aluno.getDataNascimento().getTime()));
			ps.setString(6, aluno.getPeriodo());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao inserir dados " + sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps);
		}
	}

	// metodo de atualizar

	public void atualizar(Aluno aluno) throws Exception {
		if (aluno == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "UPDATE alunos set nome=?, email=?, endereco=?, datanascimento=?, "
					+ "periodo=? WHERE ra=?";
			ps = conn.prepareStatement(SQL);
			ps.setString(1, aluno.getNome());
			ps.setString(2, aluno.getEmail());
			ps.setString(3, aluno.getEndereco());
			ps.setDate(4, new java.sql.Date(aluno.getDataNascimento().getTime()));
			ps.setString(5, aluno.getPeriodo());
			ps.setInt(6, aluno.getRa());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao alterar dados " + sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps);
		}
	}

	// metodo de excluir

	public void excluir(Aluno aluno) throws Exception {
		if (aluno == null)
			throw new Exception("O valor passado nao pode ser nulo");
		try {
			String SQL = "DELETE FROM alunos WHERE ra = ?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, aluno.getRa());
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new Exception("Erro ao excluir dados " + sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps);
		}
	}

	// Procurar Aluno

	public Aluno procurarAluno(int raAluno) throws Exception {

		try {
			String SQL = "SELECT  * FROM alunos WHERE ra=?";
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, raAluno);			
			rs = ps.executeQuery();
			if (rs.next()) {
				int ra = rs.getInt("ra");
				String nome = rs.getString("nome");
				String email = rs.getString("email");
				String endereco = rs.getString("endereco");
				Date dataNascimento = rs.getDate("dataNascimento");
				String periodo = rs.getString("periodo");
				
				aluno = new Aluno(ra, nome, email, endereco, dataNascimento, periodo);
			}
			return aluno;
		} catch (SQLException sqle) {
			throw new Exception(sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps, rs);
		}
	}
	
	// Listar todos os alunos

	public List<Aluno> todosAlunos() throws Exception {
		try {
			ps = conn.prepareStatement("SELECT * FROM alunos");
			rs = ps.executeQuery();
			List<Aluno> list = new ArrayList<Aluno>();
			while (rs.next()) {
				int ra = rs.getInt("ra");
				String nome = rs.getString("nome");
				String email = rs.getString("email");
				String endereco = rs.getString("endereco");
				Date dataNascimento = rs.getDate("dataNascimento");
				String periodo = rs.getString("periodo");
				list.add(new Aluno(ra, nome, email, endereco, dataNascimento, periodo));
			}
			return list;
		} catch (SQLException sqle) {
			throw new Exception(sqle);
		} finally {
			ConnectionFactory.closeConnection(conn, ps, rs);
		}
	}
}

