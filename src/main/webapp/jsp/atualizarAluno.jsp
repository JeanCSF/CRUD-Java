<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema Acadêmico</title>
</head>
<body>
	<h2>Atualizar Aluno</h2>
	<jsp:useBean id="aluno" scope="session" class="br.edu.exemplo.model.Aluno" />
	<%
	SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
	%>
	<form action="ServletAluno?cmd=atualizar" method="post">
		<table border="1">
			<tr>
				<td>RA:</td>
				<td><input type="text" size="60" name="txtRa"
					value="<%=aluno.getRa()%>" readonly="readonly" /></td>
			</tr>
			<tr>
				<td>Nome:</td>
				<td><input type="text" size="60" name="txtNome"
					value="<%=aluno.getNome()%>"  /></td>
			</tr>
			<tr>
				<td>E-mail:</td>
				<td><input type="text" size="60" name="txtEmail"
					value="<%=aluno.getEmail()%>"  /></td>
			</tr>
			<tr>
				<td>Endereço:</td>
				<td><input type="text" size="60"	name="txtEndereco" value="<%=aluno.getEndereco()%>"
					 /></td>
			</tr>

			<tr>
				<td>Nascimento (dd/mm/aaaa):</td>
				<td><input type="date" size="60" name="txtData" value="<%=aluno.getDataNascimento()%>" /></td>
			</tr>
			<tr>
				<td>Período:</td>
				<td><select name="cmbPeriodo" size="1">
						<option><%=aluno.getPeriodo()%></option>
						<option>Manhã</option>
						<option>Tarde</option>
						<option>Noite</option>
				     </select>		
				</td>
				
			</tr>
			<tr>
				<th colspan="2"><input type="submit" value="Confirmar Atualização" /></th>
			</tr>

			<tr>
				<th colspan="2"><a href="index.html">Página Principal</a></th>
			</tr>
		</table>
	</form>
</body>
</html>
