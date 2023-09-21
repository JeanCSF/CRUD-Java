var dataTable = $('#listTable').DataTable({
	language: {
		url: '//cdn.datatables.net/plug-ins/1.13.4/i18n/pt-BR.json',
	},
	responsive: true,
	stateSave: true,
	ajax: {
		"url": "./ServletAluno?cmd=listar",
		"dataSrc": ""
	},
	autoWidth: false,
	columnDefs: [
		{
			orderable: false,
			targets: [6]
		}

	],
	order: [
		[1, 'asc']
	],
	columns: [
		{ "data": "ra" },
		{ "data": "nome" },
		{ "data": "endereco" },
		{ "data": "email" },
		{ "data": "dataNascimento" },
		{ "data": "periodo" },
		{
			"data": null,
			"render": function (data, type, row, meta) {
				return `
					<span>
						<button title="Editar Aluno" data-bs-toggle="modal" data-bs-target="#includeModal" class="btn border-0 bg-transparent text-primary" onclick="fillModalAtualizar(${row.ra})"><i class="fa fa-pen"></i></button>
						<button title="Ecluir Aluno" data-bs-toggle="modal" data-bs-target="#deleteModal" class="btn border-0 bg-transparent text-danger" onclick="fillModalDelete(${row.ra}, '${row.nome}')"><i class="fa fa-trash"></i></button>
					</span>
				  `;
			}
		}
	],
	createdRow: function (row, data, dataIndex) {
		$(row).attr('id', `tr${data.ra}`);
	},
});

document.addEventListener("DOMContentLoaded", () => {
	deleteContent();
});

var msg = document.querySelector('.toast-body');
var mainForm = document.getElementById('mainForm');
const mainFormSubmit = document.querySelector('#btnSalvarAluno');
const toastElement = document.querySelector('.toast');
const mainTable = document.querySelector('#listTableBody');

document.querySelector("#themeToggleButton").addEventListener("click", () => {
	if (document.documentElement.getAttribute('data-bs-theme') == 'dark') {
		document.documentElement.setAttribute('data-bs-theme', 'light')
		localStorage.setItem('theme', 'light');
	}
	else {
		document.documentElement.setAttribute('data-bs-theme', 'dark');
		localStorage.setItem('theme', 'dark');
	}
});

function dataFormatadaParaYYYYMMDD(dataFormatada) {

	const meses = {
		'jan.': '01',
		'fev.': '02',
		'mar.': '03',
		'abr.': '04',
		'mai.': '05',
		'jun.': '06',
		'jul.': '07',
		'ago.': '08',
		'set.': '09',
		'out.': '10',
		'nov.': '11',
		'dez.': '12'
	};

	const partesData = dataFormatada.split(' ');

	const numeroMes = meses[partesData[0]];

	var dia = partesData[1].replace(',', '');
	const ano = partesData[2];

	if (parseInt(dia) < 10) {
		dia = '0' + dia;
	}

	const novaData = `${ano}-${numeroMes}-${dia}`;

	return novaData;
};

function dataMesAbreviado(dataFormatada) {
	const meses = {
		'01': 'jan.',
		'02': 'fev.',
		'03': 'mar.',
		'04': 'abr.',
		'05': 'mai.',
		'06': 'jun.',
		'07': 'jul.',
		'08': 'ago.',
		'09': 'set.',
		'10': 'out.',
		'11': 'nov.',
		'12': 'dez.'
	};

	const partesData = dataFormatada.split('-');

	const ano = partesData[0];
	const numeroMes = partesData[1];
	const dia = partesData[2];

	const mesAbreviado = meses[numeroMes];

	const dataFormatadaFinal = `${mesAbreviado} ${dia}, ${ano}`;

	return dataFormatadaFinal;
};

function limparCampos() {
	const elementos = Array.from(mainForm.elements);
	elementos.forEach((e) => {
		if (e.tagName === "INPUT" || e.tagName === "SELECT" || e.tagName === "TEXTAREA") {
			e.value = '';
		}
	});
};

function getFormData(form) {
	const dadosDoFormulario = {};
	for (const elemento of form.elements) {
		if (elemento.name) {
			dadosDoFormulario[elemento.name] = elemento.value;
		}
	}
	return dadosDoFormulario;
};

async function inserirAluno() {
	var toast = new bootstrap.Toast(toastElement);
	const formObj = getFormData(mainForm);

	const paramsObj = {
		ra: formObj.txtRa,
		nome: formObj.txtNome,
		email: formObj.txtEmail,
		endereco: formObj.txtEndereco,
		dataNascimento: formObj.txtData,
		periodo: formObj.cmbPeriodo,
	};
	try {
		const response = await fetch(`./ServletAluno?cmd=incluir`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(paramsObj),
		});
		const responseData = await response.json();
		if (!response.ok) {
			msg.textContent = responseData.message;
			throw new Error(`Erro na requisição: ${response.statusText}`);
		}
		if (responseData.sucesso != false) {
			msg.textContent = responseData.message;
			const newRowData = {
				ra: formObj.txtRa,
				nome: formObj.txtNome,
				email: formObj.txtEmail,
				endereco: formObj.txtEndereco,
				dataNascimento: dataMesAbreviado(formObj.txtData),
				periodo: formObj.cmbPeriodo,
			};

			dataTable.row.add(newRowData).draw();
			document.querySelector('#closeIncludeModal').click();
			toast.show();

		} else {
			msg.textContent = responseData.message;
			toast.show();
		}

	} catch (error) {
		console.error("Erro na requisição:", error);
	}
};

async function editarAluno(ra) {
	var toast = new bootstrap.Toast(toastElement);
	const formObj = getFormData(mainForm);

	const paramsObj = {
		ra: formObj.txtRa,
		nome: formObj.txtNome,
		email: formObj.txtEmail,
		endereco: formObj.txtEndereco,
		dataNascimento: formObj.txtData,
		periodo: formObj.cmbPeriodo,
	};

	try {
		const response = await fetch(`./ServletAluno?cmd=atualizar`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(paramsObj),
		});
		const responseData = await response.json();
		if (!response.ok) {
			msg.textContent = responseData.message;
			throw new Error(`Erro na requisição: ${response.statusText}`);
		}

		var $linha = $('#listTable tbody tr#' + `tr${ra}`);
		$linha.find('td:eq(1)').text(formObj.txtNome);
		$linha.find('td:eq(2)').text(formObj.txtEndereco);
		$linha.find('td:eq(3)').text(formObj.txtEmail);
		$linha.find('td:eq(4)').text(dataMesAbreviado(formObj.txtData));
		$linha.find('td:eq(5)').text(formObj.cmbPeriodo);

		msg.textContent = responseData.message;
		document.querySelector('#closeIncludeModal').click();
		toast.show();
	} catch (error) {
		console.error("Erro na requisição:", error);
	}
};

async function validarCampos() {
	const insertType = mainFormSubmit.getAttribute('data-insert-type');
	var toast = new bootstrap.Toast(toastElement);
	const ra = document.querySelector("#txtRa").value;
	const nome = document.querySelector("#txtNome").value;
	const email = document.querySelector("#txtEmail").value;
	const endereco = document.querySelector("#txtEndereco").value;
	const dataNasc = document.querySelector("#txtData").value;
	const cmbPeriodo = document.querySelector("#cmbPeriodo").value;

	if (!ra || !nome || !email || !endereco || !dataNasc || !cmbPeriodo) {
		msg.textContent = "Todos os campos são obrigatórios!";
		toast.show();
		return false;
	}
	if (insertType === 'insert') {
		const response = await fetch(`./ServletAluno?cmd=checkra&ra=${ra}`);
		const data = await response.json();

		if (data.sucesso == false) {
			msg.textContent = data.message;
			toast.show();
			return false;
		}
	}

	return true;
};

mainForm.addEventListener('submit', async (e) => {
	const insertType = mainFormSubmit.getAttribute('data-insert-type');
	const ra = document.querySelector("#txtRa").value;

	e.preventDefault();

	const camposValidos = await validarCampos();

	if (!camposValidos) {
		return;
	}

	if (insertType === 'insert') {
		inserirAluno();
	} else {
		editarAluno(ra);
	}

	limparCampos();
});

function deleteContent() {
	document.querySelector("#btnDeletar").addEventListener('click', async () => {
		var ra = document.getElementById("btnDeletar").getAttribute('data-delete');
		var toast = new bootstrap.Toast(toastElement);
		try {
			const response = await fetch(`./ServletAluno?cmd=excluir&ra=${ra}`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				}
			});

			const responseData = await response.json();
			if (!response.ok) {
				msg.textContent = responseData.message;
				throw new Error(`Erro na requisição: ${response.statusText}`);
			}

			msg.textContent = responseData.message;
			dataTable.ajax.reload();
			document.querySelector('#closeDeleteModal').click();
			toast.show();
		} catch (error) {
			console.error("Erro na requisição:", error);
		}
	});
};

function fillModalDelete(ra, aluno) {
	document.getElementById("nomeAluno").textContent = aluno;
	document.getElementById("btnDeletar").setAttribute('data-delete', ra);
};

function fillModalInsert() {
	document.querySelector('#txtRa').removeAttribute('readonly');
	limparCampos();
	document.querySelector('#btnSalvarAluno').setAttribute('data-insert-type', 'insert');
};

async function fillModalAtualizar(ra) {
	try {
		const response = await fetch(`./ServletAluno?cmd=con&ra=${ra}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			}
		});

		const responseData = await response.json();
		if (!response.ok) {
			throw new Error(`Erro na requisição: ${response.statusText}`);
		}
		var txtRa = document.querySelector('#txtRa');
		txtRa.setAttribute('readonly', true);
		txtRa.value = responseData.ra;

		document.querySelector('#txtNome').value = responseData.nome;
		document.querySelector('#txtEmail').value = responseData.email;
		document.querySelector('#txtEndereco').value = responseData.endereco;
		document.querySelector('#txtData').value = dataFormatadaParaYYYYMMDD(responseData.dataNascimento);
		document.querySelector('#cmbPeriodo').value = responseData.periodo;
		document.querySelector('#btnSalvarAluno').setAttribute('data-insert-type', 'update');
	} catch (error) {
		console.error("Erro ao analisar a string JSON: " + error);
	}
};
