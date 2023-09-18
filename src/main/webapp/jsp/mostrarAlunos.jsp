<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.ArrayList, com.google.gson.Gson, br.edu.exemplo.model.Aluno, java.text.SimpleDateFormat" %>

<%
List<Aluno> lista = (ArrayList<Aluno>) request.getAttribute("alunosList");
List<List<Object>> data = new ArrayList<>();
for (Aluno aluno : lista) {
    List<Object> rowData = new ArrayList<>();
    rowData.add(aluno.getRa());
    rowData.add(aluno.getNome());
    rowData.add(aluno.getEndereco());
    rowData.add(aluno.getEmail());
    rowData.add(new SimpleDateFormat("dd/MM/yyyy").format(aluno.getDataNascimento()));
    rowData.add(aluno.getPeriodo());
    data.add(rowData);
}

Gson gson = new Gson();
String json = gson.toJson(data);
out.print(json);
%>