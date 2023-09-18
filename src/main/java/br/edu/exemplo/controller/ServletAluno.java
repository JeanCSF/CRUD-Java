package br.edu.exemplo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.exemplo.dao.AlunoDAO;
import br.edu.exemplo.model.Aluno;

/**
 * Servlet implementation class ServletAluno
 */
@WebServlet("/ServletAluno")
public class ServletAluno extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// metodo para conversao de String para data
	private Date strToDate(String data) throws Exception {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date dataF = formato.parse(data);
		return dataF;
	}

	Aluno aluno = new Aluno();
	Gson gson = new Gson();
	AlunoDAO dao;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String cmd = request.getParameter("cmd");
		try {
			dao = new AlunoDAO();
			if (cmd.equalsIgnoreCase("incluir")) {
				request.setCharacterEncoding("UTF-8");
				Gson gson = new Gson();
				Aluno aluno = gson.fromJson(request.getReader(), Aluno.class);
				JsonObject jsonResponse = new JsonObject();
				try {
					dao.salvar(aluno);
					jsonResponse.addProperty("sucesso", true);
					jsonResponse.addProperty("message", "Aluno inserido com sucesso!");
				} catch (Exception e) {
					jsonResponse.addProperty("sucesso", false);
					jsonResponse.addProperty("message", "Erro ao inserir aluno: " + e.getMessage());
				}
				String json = gson.toJson(jsonResponse);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (cmd.equalsIgnoreCase("listar")) {

				List<Aluno> alunosList = dao.todosAlunos();
				String json = gson.toJson(alunosList);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);

			} else if (cmd.equalsIgnoreCase("atualizar")) {
				request.setCharacterEncoding("UTF-8");
				Gson gson = new Gson();
				Aluno aluno = gson.fromJson(request.getReader(), Aluno.class);
				JsonObject jsonResponse = new JsonObject();
				try {
					dao.atualizar(aluno);
					jsonResponse.addProperty("sucesso", true);
					jsonResponse.addProperty("message", "Aluno atualizado com sucesso!");
				} catch (Exception e) {
					jsonResponse.addProperty("sucesso", false);
					jsonResponse.addProperty("message", "Erro ao atualizar aluno: " + e.getMessage());
				}
				String json = gson.toJson(jsonResponse);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (cmd.equalsIgnoreCase("con")) {
				int raParam = Integer.parseInt(request.getParameter("ra"));
				Aluno aluno = dao.procurarAluno(raParam);
				String alunoJson = gson.toJson(aluno);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(alunoJson);

			} else if (cmd.equalsIgnoreCase("checkra")) {
				int raParam = Integer.parseInt(request.getParameter("ra"));
				Aluno aluno = dao.procurarAluno(raParam);
				JsonObject jsonResponse = new JsonObject();
				if (aluno != null) {
					jsonResponse.addProperty("sucesso", false);
					jsonResponse.addProperty("message", "Este RA já está cadastrado!");
				} else {
					jsonResponse.addProperty("sucesso", true);
					jsonResponse.addProperty("message", "OK");
				}
				String json = gson.toJson(jsonResponse);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);

			} else if (cmd.equalsIgnoreCase("excluir")) {

				int raParam = Integer.parseInt(request.getParameter("ra"));
				JsonObject jsonResponse = new JsonObject();
				try {
					if (raParam != 0) {
						dao.excluir(raParam);
						jsonResponse.addProperty("sucesso", true);
						jsonResponse.addProperty("message", "Aluno RA: " + raParam + " excluído com sucesso!");
					} else {
						jsonResponse.addProperty("sucesso", false);
						jsonResponse.addProperty("message", "Aluno RA: " + raParam + " não encontrado!");
					}
				} catch (Exception e) {
					jsonResponse.addProperty("sucesso", false);
					jsonResponse.addProperty("message", "Erro ao excluir aluno: " + e.getMessage());
				}
				String json = gson.toJson(jsonResponse);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);

			}

		} catch (Exception e) {
			System.out.println("Erro ao gravar");
			System.out.println(e.getMessage());

		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
