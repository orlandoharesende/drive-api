function init() {
	console.log("Iniciando a aplicação JS.");
	let numeroProcesso = getRequestParameter("numeroProcesso");
	atualizarNavbar(numeroProcesso);
	atualizarListaArquivos(numeroProcesso);
	console.log("Fim do init.");
}

function getRequestParameter(name) {
	var queryString = location.search;
	let params = new URLSearchParams(queryString);
	return params.get(name);
}

function atualizarNavbar(numeroProcesso) {
	let novoTitulo = "Drive API - Processo: " + numeroProcesso + "";
	document.getElementById("main-navbar").innerHTML = novoTitulo;
}

function atualizarListaArquivos(numeroProcesso) {
	let url = "/" + numeroProcesso + "/arquivos";
	fetch(url)
		.then(response => response.json())
		.then(data => processarListaArquivos(data));
}

function processarListaArquivos(arquivos) {
	let listaArquivosHtml = document.getElementById("lista-arquivos");
	for(arquivo of arquivos){
		addItemListaArquivosHtml(listaArquivosHtml, arquivo);
	}
	
}

function addItemListaArquivosHtml(listaArquivosHtml, arquivo){
	let a = document.createElement("a"); 
	var link = document.createTextNode(arquivo.nome);
	a.appendChild(link); 
	a.title = arquivo.nome;
	a.href =  arquivo.download;
	a.target = "_blank";
	a.classList.add("list-group-item");
	a.classList.add("list-group-item-action");
	listaArquivosHtml.appendChild(a); 
}

