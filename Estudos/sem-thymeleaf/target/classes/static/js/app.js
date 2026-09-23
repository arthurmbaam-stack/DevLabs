const form = document.getElementById("filtros");
const lista = document.getElementById("lista");
const resultado = document.getElementById("resultado");
const selectCargo = document.getElementById("cargo");

// Evita que texto vindo do servidor seja interpretado como HTML
function esc(texto) {
  const div = document.createElement("div");
  div.textContent = texto;
  return div.innerHTML;
}

function desenhar(candidatos) {
  resultado.textContent = candidatos.length + " candidato(s) encontrado(s)";

  if (candidatos.length === 0) {
    lista.innerHTML =
      '<div class="vazio">Nenhum candidato encontrado. Tente remover algum filtro.</div>';
    return;
  }

  lista.innerHTML = candidatos.map(c => `
    <article class="card">
      <div class="card-topo">
        <div class="avatar">${esc(c.nome.charAt(0))}</div>
        <h2>${esc(c.nome)}</h2>
      </div>
      <p class="cargo">${esc(c.cargo)}</p>
      <p class="meta">${esc(c.cidade)} · ${c.experiencia} ano(s) de experiência</p>
      <div class="tags">
        ${c.tecnologias.split(",").map(t => `<span class="tag">${esc(t.trim())}</span>`).join("")}
      </div>
    </article>
  `).join("");
}

async function carregar() {
  // Monta ?nome=...&cargo=...&experienciaMin=... a partir do formulário
  const params = new URLSearchParams(new FormData(form));
  const resposta = await fetch("/api/candidatos?" + params.toString());
  desenhar(await resposta.json());
}

async function iniciar() {
  // 1ª chamada sem filtro só para preencher o <select> de cargos
  const todos = await (await fetch("/api/candidatos")).json();
  const cargos = [...new Set(todos.map(c => c.cargo))].sort();
  cargos.forEach(c => selectCargo.add(new Option(c, c)));

  desenhar(todos);
}

form.addEventListener("submit", e => {
  e.preventDefault();      // não recarrega a página
  carregar();
});

// filtra enquanto digita / muda o select
form.addEventListener("input", carregar);

iniciar();
