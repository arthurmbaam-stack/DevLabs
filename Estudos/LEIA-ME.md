# Prova de amanhã: controller + HTML/CSS + filtro de candidatos

Dois projetos Spring Boot, mesma tela e mesmo filtro (nome, cargo, experiência mínima):

| Pasta | Como a página é feita |
|---|---|
| `com-thymeleaf/` | Controller devolve o nome do template. HTML em `templates/candidatos.html` |
| `sem-thymeleaf/` | Jeito 1: API JSON + `static/index.html` com `fetch`. Jeito 2: controller devolve o HTML como String |

Para rodar (Java 17+ e Maven): entre na pasta do projeto e execute `mvn spring-boot:run`.
- Com Thymeleaf: http://localhost:8080/
- Sem Thymeleaf: http://localhost:8080/ (jeito 1) e http://localhost:8080/candidatos (jeito 2)

---

## O que provavelmente cai: o controller

O service já vem pronto. Seu trabalho é este esqueleto:

```java
@Controller
public class CandidatoController {

    private final CandidatoService service;               // 1. injeta o service

    public CandidatoController(CandidatoService service) { // 2. pelo construtor
        this.service = service;
    }

    @GetMapping("/candidatos")                            // 3. rota
    public String listar(@RequestParam(required = false) String nome, Model model) {
        List<Candidato> lista = service.listarTodos();    // 4. busca / filtra
        model.addAttribute("candidatos", lista);          // 5. manda pro HTML
        return "candidatos";                              // 6. nome do template
    }
}
```

### Anotações para saber de cor
| Anotação | Para que serve |
|---|---|
| `@Controller` | Classe que atende requisições e devolve **página** (nome do template) |
| `@RestController` | `@Controller` + `@ResponseBody`: devolve **JSON/texto** |
| `@GetMapping("/x")` | Rota GET |
| `@RequestParam` | Lê `?nome=ana` da URL. `required = false` deixa opcional |
| `@PathVariable` | Lê `/candidatos/3`. Ex.: `@GetMapping("/candidatos/{id}")` |
| `Model` | Caixa onde você coloca dados para o HTML: `model.addAttribute("chave", valor)` |
| `redirect:/candidatos` | Retornar essa String redireciona para outra rota |

### Filtro com stream (padrão que se repete)
```java
.filter(c -> nome == null || nome.isBlank() || c.getNome().toLowerCase().contains(nome.toLowerCase()))
```
Lógica: "se o filtro não foi preenchido, deixa passar; se foi, compara".

---

## Thymeleaf em 6 linhas
- `xmlns:th="http://www.thymeleaf.org"` no `<html>`
- `th:text="${c.nome}"` escreve o valor (chama `getNome()`)
- `th:each="c : ${candidatos}"` é o `for`
- `th:if="${#lists.isEmpty(candidatos)}"` é o `if`
- `th:href="@{/css/style.css}"` e `th:action="@{/candidatos}"` montam URLs
- `th:value="${nome}"` mantém o campo preenchido depois de filtrar

Os templates ficam em `src/main/resources/templates/` e o CSS em `src/main/resources/static/css/`.

---

## Erros comuns (e o que olhar)
- **Whitelabel 404** ao abrir a rota: confira o `@GetMapping` e se a classe tem `@Controller`.
- **Aparece o texto "candidatos" na tela em vez da página**: você usou `@RestController` (ou `@ResponseBody`) onde precisava de `@Controller`.
- **Template not found**: o nome retornado tem que ser igual ao arquivo em `templates/`, sem `.html`.
- **CSS não carrega**: o arquivo precisa estar em `static/css/` e o link ser `/css/style.css`.
- **`Name for argument ... not specified`**: escreva o nome explícito: `@RequestParam("nome") String nome`.
- **Thymeleaf não funciona**: falta `spring-boot-starter-thymeleaf` no `pom.xml`.
- **Porta 8080 ocupada**: `server.port=8081` em `application.properties`.

## Como se preparar hoje à noite
1. Rode os dois projetos e veja funcionando.
2. Apague o conteúdo do `CandidatoController` e reescreva de memória.
3. Mude o filtro (ex.: filtrar por cidade) e adicione o campo no HTML.
4. Refaça o CSS do zero só com grid + cards, sem olhar.
