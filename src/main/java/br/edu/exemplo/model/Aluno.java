package br.edu.exemplo.model;

import java.util.Date;

public class Aluno {
	
	/* Java Bean - É uma classe que contenha todos os atributos privados, possua getters e setters para seus atributos
	e usada para encapsular e abstrair uma entidade */
	
	// atributos
	private int ra;
	private String nome;
	private String email;
	private String endereco;
	private Date dataNascimento;
	private String periodo;
	// construtor
	public Aluno(int ra, String nome, String email, String endereco, Date dataNascimento, String periodo) {
		this.ra = ra;
		this.nome = nome;
		this.email = email;
		this.endereco = endereco;
		this.dataNascimento = dataNascimento;
		this.periodo = periodo;
	}
	// construtor vazio
	public Aluno() {
	}
	// getters e setters
	public int getRa() {
		return ra;
	}
	public void setRa(int ra) {
		this.ra = ra;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
}
