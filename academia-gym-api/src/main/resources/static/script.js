const API_BASE = '';

let alunos = [];
let modalidades = [];
let professores = [];
let planos = [];
let matriculas = [];
let pagamentos = [];
let inadimplenciasPendentes = [];

document.addEventListener('DOMContentLoaded', () => {
  configurarAbas();
  configurarFormularios();
  carregarTudo();
});

function configurarAbas() {
  document.querySelectorAll('.aba').forEach((botao) => {
    botao.addEventListener('click', () => {
      document.querySelectorAll('.aba').forEach((b) => b.classList.remove('ativa'));
      document.querySelectorAll('.secao').forEach((s) => s.classList.remove('ativa'));
      botao.classList.add('ativa');
      document.getElementById(botao.dataset.alvo).classList.add('ativa');
    });
  });
}

function configurarFormularios() {
  document.getElementById('formAluno').addEventListener('submit', salvarAluno);
  document.getElementById('formModalidade').addEventListener('submit', salvarModalidade);
  document.getElementById('formProfessor').addEventListener('submit', salvarProfessor);
  document.getElementById('formPlano').addEventListener('submit', salvarPlano);
  document.getElementById('formMatricula').addEventListener('submit', salvarMatricula);
  document.getElementById('formPagamento').addEventListener('submit', salvarPagamento);
}

async function carregarTudo() {
  await Promise.allSettled([
    carregarAlunos(),
    carregarModalidades(),
    carregarPlanos(),
    carregarProfessores(),
    carregarMatriculas(),
    carregarPagamentos(),
    carregarInadimplenciasPendentes(),
    carregarRelatorios()
  ]);
  atualizarResumo();
}

async function apiRequest(caminho, opcoes = {}) {
  const resposta = await fetch(`${API_BASE}${caminho}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(opcoes.headers || {})
    },
    ...opcoes
  });

  if (!resposta.ok) {
    let mensagem = `Erro ${resposta.status}`;
    try {
      const erro = await resposta.json();
      mensagem = erro.mensagem || erro.message || JSON.stringify(erro);
    } catch (_) {
      mensagem = await resposta.text();
    }
    throw new Error(mensagem || 'Erro ao comunicar com a API');
  }

  atualizarStatusApi(true);

  if (resposta.status === 204) {
    return null;
  }

  return resposta.json();
}

function atualizarStatusApi(ok) {
  const bolinha = document.getElementById('statusApiBolinha');
  const texto = document.getElementById('statusApiTexto');
  bolinha.classList.remove('ok', 'erro');
  bolinha.classList.add(ok ? 'ok' : 'erro');
  texto.textContent = ok ? 'API conectada' : 'API indisponível';
}

function mostrarMensagem(texto, tipo = 'sucesso') {
  const mensagem = document.getElementById('mensagem');
  mensagem.textContent = texto;
  mensagem.className = `mensagem ${tipo === 'erro' ? 'erro' : ''}`;
  setTimeout(() => mensagem.classList.add('oculto'), 5000);
}

function tratarErro(erro) {
  atualizarStatusApi(false);
  mostrarMensagem(erro.message || 'Ocorreu um erro inesperado.', 'erro');
}

function dadosDoFormulario(formId) {
  const form = document.getElementById(formId);
  const dados = Object.fromEntries(new FormData(form).entries());

  Object.keys(dados).forEach((chave) => {
    if (dados[chave] === '') {
      delete dados[chave];
    }
  });

  return dados;
}

function limparFormulario(formId) {
  const form = document.getElementById(formId);
  form.reset();
  const id = form.querySelector('[name="id"]');
  if (id) id.value = '';

  const botaoSalvar = form.querySelector('button[type="submit"]');
  if (botaoSalvar) {
    botaoSalvar.textContent = botaoSalvar.textContent.replace('Atualizar', 'Salvar');
  }
}

function atualizarResumo() {
  document.getElementById('totalAlunos').textContent = alunos.length;
  document.getElementById('totalPlanos').textContent = planos.length;
  document.getElementById('totalMatriculas').textContent = matriculas.length;
  document.getElementById('totalInadimplencias').textContent = inadimplenciasPendentes.length;
}

function formatarData(valor) {
  if (!valor) return '-';
  const [ano, mes, dia] = valor.split('-');
  if (!ano || !mes || !dia) return valor;
  return `${dia}/${mes}/${ano}`;
}

function formatarDinheiro(valor) {
  if (valor === null || valor === undefined || valor === '') return '-';
  return Number(valor).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function textoSimNao(valor) {
  return valor ? 'Sim' : 'Não';
}

function statusBadge(status) {
  if (!status) return '-';
  return `<span class="status ${String(status).toLowerCase()}">${status}</span>`;
}

function criarTabela(containerId, colunas, dados, acoes) {
  const container = document.getElementById(containerId);

  if (!dados || dados.length === 0) {
    container.innerHTML = '<div class="vazio">Nenhum registro encontrado.</div>';
    return;
  }

  const cabecalho = colunas.map((coluna) => `<th>${coluna.titulo}</th>`).join('');
  const possuiAcoes = typeof acoes === 'function';

  const linhas = dados.map((item) => {
    const tds = colunas.map((coluna) => {
      const valor = coluna.render ? coluna.render(item) : item[coluna.campo];
      return `<td>${valor ?? '-'}</td>`;
    }).join('');

    return `<tr>${tds}${possuiAcoes ? `<td>${acoes(item)}</td>` : ''}</tr>`;
  }).join('');

  container.innerHTML = `
    <table>
      <thead>
        <tr>${cabecalho}${possuiAcoes ? '<th>Ações</th>' : ''}</tr>
      </thead>
      <tbody>${linhas}</tbody>
    </table>
  `;
}

function preencherSelect(id, dados, textoFn, valorCampo = 'id', textoVazio = 'Selecione') {
  const select = document.getElementById(id);
  select.innerHTML = `<option value="">${textoVazio}</option>`;
  dados.forEach((item) => {
    const option = document.createElement('option');
    option.value = item[valorCampo];
    option.textContent = textoFn(item);
    select.appendChild(option);
  });
}

async function carregarAlunos() {
  try {
    alunos = await apiRequest('/alunos');
    criarTabela('tabelaAlunos', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Nome', campo: 'nome' },
      { titulo: 'CPF', campo: 'cpf' },
      { titulo: 'E-mail', campo: 'email' },
      { titulo: 'Telefone', campo: 'telefone' },
      { titulo: 'Nascimento', render: (a) => formatarData(a.dataNascimento) },
      { titulo: 'Ativo', render: (a) => textoSimNao(a.ativo) }
    ], alunos, (a) => `
      <button class="botao secundario" type="button" onclick="editarAluno(${a.id})">Editar</button>
      <button class="botao perigo" type="button" onclick="deletarAluno(${a.id})">Excluir</button>
    `);
    preencherSelect('selectAlunoMatricula', alunos, (a) => `${a.id} - ${a.nome}`);
    atualizarResumo();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function buscarAlunoPorNome() {
  const nome = document.getElementById('buscaAluno').value.trim();
  if (!nome) {
    mostrarMensagem('Digite um nome para buscar.', 'erro');
    return;
  }

  try {
    const aluno = await apiRequest(`/alunos/buscar?nome=${encodeURIComponent(nome)}`);
    criarTabela('tabelaAlunos', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Nome', campo: 'nome' },
      { titulo: 'CPF', campo: 'cpf' },
      { titulo: 'E-mail', campo: 'email' },
      { titulo: 'Telefone', campo: 'telefone' },
      { titulo: 'Nascimento', render: (a) => formatarData(a.dataNascimento) },
      { titulo: 'Ativo', render: (a) => textoSimNao(a.ativo) }
    ], [aluno], (a) => `
      <button class="botao secundario" type="button" onclick="editarAluno(${a.id})">Editar</button>
      <button class="botao perigo" type="button" onclick="deletarAluno(${a.id})">Excluir</button>
    `);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarAluno(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formAluno');
  const id = dados.id;
  delete dados.id;

  try {
    if (id) {
      await apiRequest(`/alunos/${id}`, { method: 'PUT', body: JSON.stringify(dados) });
      mostrarMensagem('Aluno atualizado com sucesso.');
    } else {
      await apiRequest('/alunos', { method: 'POST', body: JSON.stringify(dados) });
      mostrarMensagem('Aluno cadastrado com sucesso.');
    }
    limparFormulario('formAluno');
    await carregarAlunos();
  } catch (erro) {
    tratarErro(erro);
  }
}

function editarAluno(id) {
  const aluno = alunos.find((a) => a.id === id);
  if (!aluno) return;
  const form = document.getElementById('formAluno');
  form.elements.id.value = aluno.id;
  form.elements.nome.value = aluno.nome || '';
  form.elements.cpf.value = aluno.cpf || '';
  form.elements.email.value = aluno.email || '';
  form.elements.telefone.value = aluno.telefone || '';
  form.elements.dataNascimento.value = aluno.dataNascimento || '';
  form.elements.sexo.value = aluno.sexo || '';
  form.elements.endereco.value = aluno.endereco || '';
  form.querySelector('button[type="submit"]').textContent = 'Atualizar aluno';
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function deletarAluno(id) {
  if (!confirm('Deseja excluir este aluno?')) return;
  try {
    await apiRequest(`/alunos/${id}`, { method: 'DELETE' });
    mostrarMensagem('Aluno excluído com sucesso.');
    await carregarAlunos();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarModalidades() {
  try {
    modalidades = await apiRequest('/modalidades');
    criarTabela('tabelaModalidades', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Nome', campo: 'nome' },
      { titulo: 'Descrição', campo: 'descricao' },
      { titulo: 'Ativa', render: (m) => textoSimNao(m.ativa) }
    ], modalidades, (m) => `
      <button class="botao secundario" type="button" onclick="editarModalidade(${m.id})">Editar</button>
      <button class="botao perigo" type="button" onclick="deletarModalidade(${m.id})">Excluir</button>
    `);
    preencherSelect('selectModalidadeProfessor', modalidades, (m) => `${m.id} - ${m.nome}`);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarModalidade(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formModalidade');
  const id = dados.id;
  delete dados.id;

  try {
    if (id) {
      await apiRequest(`/modalidades/${id}`, { method: 'PUT', body: JSON.stringify(dados) });
      mostrarMensagem('Modalidade atualizada com sucesso.');
    } else {
      await apiRequest('/modalidades', { method: 'POST', body: JSON.stringify(dados) });
      mostrarMensagem('Modalidade cadastrada com sucesso.');
    }
    limparFormulario('formModalidade');
    await carregarModalidades();
  } catch (erro) {
    tratarErro(erro);
  }
}

function editarModalidade(id) {
  const modalidade = modalidades.find((m) => m.id === id);
  if (!modalidade) return;
  const form = document.getElementById('formModalidade');
  form.elements.id.value = modalidade.id;
  form.elements.nome.value = modalidade.nome || '';
  form.elements.descricao.value = modalidade.descricao || '';
  form.querySelector('button[type="submit"]').textContent = 'Atualizar modalidade';
}

async function deletarModalidade(id) {
  if (!confirm('Deseja excluir esta modalidade?')) return;
  try {
    await apiRequest(`/modalidades/${id}`, { method: 'DELETE' });
    mostrarMensagem('Modalidade excluída com sucesso.');
    await carregarModalidades();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarProfessores() {
  try {
    professores = await apiRequest('/professores');
    criarTabela('tabelaProfessores', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Nome', campo: 'nome' },
      { titulo: 'E-mail', campo: 'email' },
      { titulo: 'Telefone', campo: 'telefone' },
      { titulo: 'Modalidade', campo: 'modalidadeNome' },
      { titulo: 'Ativo', render: (p) => textoSimNao(p.ativo) }
    ], professores, (p) => `
      <button class="botao secundario" type="button" onclick="editarProfessor(${p.id})">Editar</button>
      <button class="botao perigo" type="button" onclick="deletarProfessor(${p.id})">Excluir</button>
    `);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarProfessor(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formProfessor');
  const id = dados.id;
  delete dados.id;
  dados.modalidadeId = Number(dados.modalidadeId);

  try {
    if (id) {
      await apiRequest(`/professores/${id}`, { method: 'PUT', body: JSON.stringify(dados) });
      mostrarMensagem('Professor atualizado com sucesso.');
    } else {
      await apiRequest('/professores', { method: 'POST', body: JSON.stringify(dados) });
      mostrarMensagem('Professor cadastrado com sucesso.');
    }
    limparFormulario('formProfessor');
    await carregarProfessores();
  } catch (erro) {
    tratarErro(erro);
  }
}

function editarProfessor(id) {
  const professor = professores.find((p) => p.id === id);
  if (!professor) return;
  const form = document.getElementById('formProfessor');
  form.elements.id.value = professor.id;
  form.elements.nome.value = professor.nome || '';
  form.elements.email.value = professor.email || '';
  form.elements.telefone.value = professor.telefone || '';
  form.elements.modalidadeId.value = professor.modalidadeId || '';
  form.querySelector('button[type="submit"]').textContent = 'Atualizar professor';
}

async function deletarProfessor(id) {
  if (!confirm('Deseja excluir este professor?')) return;
  try {
    await apiRequest(`/professores/${id}`, { method: 'DELETE' });
    mostrarMensagem('Professor excluído com sucesso.');
    await carregarProfessores();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarPlanos() {
  try {
    planos = await apiRequest('/planos');
    criarTabela('tabelaPlanos', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Nome', campo: 'nome' },
      { titulo: 'Duração', render: (p) => `${p.duracaoDias} dias` },
      { titulo: 'Valor', render: (p) => formatarDinheiro(p.valor) },
      { titulo: 'Ativo', render: (p) => textoSimNao(p.ativo) }
    ], planos, (p) => `
      <button class="botao secundario" type="button" onclick="editarPlano(${p.id})">Editar</button>
      <button class="botao perigo" type="button" onclick="deletarPlano(${p.id})">Excluir</button>
    `);
    preencherSelect('selectPlanoMatricula', planos, (p) => `${p.id} - ${p.nome} (${formatarDinheiro(p.valor)})`);
    atualizarResumo();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarPlano(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formPlano');
  const id = dados.id;
  delete dados.id;
  dados.duracaoDias = Number(dados.duracaoDias);
  dados.valor = Number(dados.valor);

  try {
    if (id) {
      await apiRequest(`/planos/${id}`, { method: 'PUT', body: JSON.stringify(dados) });
      mostrarMensagem('Plano atualizado com sucesso.');
    } else {
      await apiRequest('/planos', { method: 'POST', body: JSON.stringify(dados) });
      mostrarMensagem('Plano cadastrado com sucesso.');
    }
    limparFormulario('formPlano');
    await carregarPlanos();
  } catch (erro) {
    tratarErro(erro);
  }
}

function editarPlano(id) {
  const plano = planos.find((p) => p.id === id);
  if (!plano) return;
  const form = document.getElementById('formPlano');
  form.elements.id.value = plano.id;
  form.elements.nome.value = plano.nome || '';
  form.elements.duracaoDias.value = plano.duracaoDias || '';
  form.elements.valor.value = plano.valor || '';
  form.querySelector('button[type="submit"]').textContent = 'Atualizar plano';
}

async function deletarPlano(id) {
  if (!confirm('Deseja excluir este plano?')) return;
  try {
    await apiRequest(`/planos/${id}`, { method: 'DELETE' });
    mostrarMensagem('Plano excluído com sucesso.');
    await carregarPlanos();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarMatriculas() {
  try {
    const status = document.getElementById('filtroStatusMatricula').value;
    const caminho = status ? `/matriculas?status=${encodeURIComponent(status)}` : '/matriculas';
    matriculas = await apiRequest(caminho);
    criarTabela('tabelaMatriculas', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Aluno', campo: 'alunoNome' },
      { titulo: 'Plano', campo: 'planoNome' },
      { titulo: 'Início', render: (m) => formatarData(m.dataInicio) },
      { titulo: 'Fim', render: (m) => formatarData(m.dataFim) },
      { titulo: 'Status', render: (m) => statusBadge(m.status) }
    ], matriculas, (m) => m.status === 'ATIVA'
      ? `<button class="botao perigo" type="button" onclick="cancelarMatricula(${m.id})">Cancelar</button>`
      : '-'
    );
    preencherSelect('selectMatriculaPagamento', matriculas, (m) => `${m.id} - ${m.alunoNome} / ${m.planoNome}`);
    atualizarResumo();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarMatricula(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formMatricula');
  dados.alunoId = Number(dados.alunoId);
  dados.planoId = Number(dados.planoId);

  try {
    await apiRequest('/matriculas', { method: 'POST', body: JSON.stringify(dados) });
    mostrarMensagem('Matrícula criada com sucesso.');
    limparFormulario('formMatricula');
    await Promise.all([carregarMatriculas(), carregarPagamentos(), carregarRelatorios()]);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function cancelarMatricula(id) {
  if (!confirm('Deseja cancelar esta matrícula?')) return;
  try {
    await apiRequest(`/matriculas/${id}/cancelar`, { method: 'PUT' });
    mostrarMensagem('Matrícula cancelada com sucesso.');
    await carregarMatriculas();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function verificarMatriculasVencidas() {
  try {
    const resposta = await apiRequest('/matriculas/verificar-vencidas', { method: 'PUT' });
    mostrarMensagem(resposta.mensagem || 'Matrículas verificadas.');
    await Promise.all([carregarMatriculas(), carregarInadimplenciasPendentes(), carregarRelatorios()]);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarPagamentos() {
  try {
    pagamentos = await apiRequest('/pagamentos');
    criarTabela('tabelaPagamentos', [
      { titulo: 'ID', campo: 'id' },
      { titulo: 'Matrícula', campo: 'matriculaId' },
      { titulo: 'Valor', render: (p) => formatarDinheiro(p.valor) },
      { titulo: 'Data', render: (p) => formatarData(p.dataPagamento) },
      { titulo: 'Forma', campo: 'formaPagamento' },
      { titulo: 'Status', render: (p) => statusBadge(p.status) }
    ], pagamentos);
  } catch (erro) {
    tratarErro(erro);
  }
}

async function salvarPagamento(evento) {
  evento.preventDefault();
  const dados = dadosDoFormulario('formPagamento');
  dados.matriculaId = Number(dados.matriculaId);
  dados.valor = Number(dados.valor);

  try {
    await apiRequest('/pagamentos', { method: 'POST', body: JSON.stringify(dados) });
    mostrarMensagem('Pagamento registrado com sucesso.');
    limparFormulario('formPagamento');
    await carregarPagamentos();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function carregarInadimplenciasPendentes() {
  try {
    inadimplenciasPendentes = await apiRequest('/inadimplencias/pendentes');
    atualizarResumo();
  } catch (erro) {
    inadimplenciasPendentes = [];
    atualizarResumo();
  }
}

async function carregarRelatorios() {
  try {
    const [alunosAtivos, inadimplencias] = await Promise.all([
      apiRequest('/relatorios/alunos-ativos'),
      apiRequest('/relatorios/inadimplencias-pendentes')
    ]);

    criarTabela('tabelaRelatorioAlunosAtivos', [
      { titulo: 'Aluno', campo: 'alunoNome' },
      { titulo: 'CPF', campo: 'cpf' },
      { titulo: 'E-mail', campo: 'email' },
      { titulo: 'Telefone', campo: 'telefone' },
      { titulo: 'Plano', campo: 'planoNome' },
      { titulo: 'Vencimento', render: (a) => formatarData(a.dataFim) },
      { titulo: 'Dias restantes', campo: 'diasRestantes' }
    ], alunosAtivos);

    inadimplenciasPendentes = inadimplencias;
    criarTabela('tabelaRelatorioInadimplencias', [
      { titulo: 'ID', campo: 'inadimplenciaId' },
      { titulo: 'Aluno', campo: 'alunoNome' },
      { titulo: 'E-mail', campo: 'email' },
      { titulo: 'Telefone', campo: 'telefone' },
      { titulo: 'Plano', campo: 'planoNome' },
      { titulo: 'Valor', render: (i) => formatarDinheiro(i.valor) },
      { titulo: 'Dias atraso', campo: 'diasAtraso' },
      { titulo: 'Registro', render: (i) => formatarData(i.dataRegistro) }
    ], inadimplencias, (i) => `
      <button class="botao secundario" type="button" onclick="resolverInadimplencia(${i.inadimplenciaId})">Resolver</button>
    `);
    atualizarResumo();
  } catch (erro) {
    tratarErro(erro);
  }
}

async function resolverInadimplencia(id) {
  if (!confirm('Deseja marcar esta inadimplência como resolvida?')) return;
  try {
    await apiRequest(`/inadimplencias/${id}/resolver`, { method: 'PUT' });
    mostrarMensagem('Inadimplência resolvida com sucesso.');
    await Promise.all([carregarInadimplenciasPendentes(), carregarRelatorios()]);
  } catch (erro) {
    tratarErro(erro);
  }
}
