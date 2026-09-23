package com.prova.candidatos.controller;

import com.prova.candidatos.model.Candidato;
import com.prova.candidatos.service.CandidatoService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Sem Thymeleaf existem 2 jeitos. Este projeto tem os dois:
 *
 *  JEITO 1 (API + JavaScript):  GET /api/candidatos  -> devolve JSON
 *          a página static/index.html chama essa rota com fetch() e desenha os cards.
 *          Abra: http://localhost:8080/
 *
 *  JEITO 2 (HTML montado em Java): GET /candidatos   -> devolve uma String HTML
 *          Abra: http://localhost:8080/candidatos
 */
@Controller
public class CandidatoController {

    private final CandidatoService service;

    public CandidatoController(CandidatoService service) {
        this.service = service;
    }

    // ------------------------------------------------------------------
    // JEITO 1: JSON. @ResponseBody = "o retorno é o corpo da resposta"
    // (se a classe fosse @RestController, não precisaria do @ResponseBody)
    // ------------------------------------------------------------------
    @GetMapping("/api/candidatos")
    @ResponseBody
    public List<Candidato> api(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Integer experienciaMin) {

        return filtrar(nome, cargo, experienciaMin);
    }

    // ------------------------------------------------------------------
    // JEITO 2: HTML como String
    // ------------------------------------------------------------------
    @GetMapping(value = "/candidatos", produces = MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
    @ResponseBody
    public String pagina(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Integer experienciaMin) {

        List<Candidato> lista = filtrar(nome, cargo, experienciaMin);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1'>")
            .append("<title>Candidatos</title>")
            .append("<link rel='stylesheet' href='/css/style.css'></head><body>")
            .append("<header class='topo'><div class='container'>")
            .append("<h1>Candidatos</h1><p>Filtre por nome, cargo ou tempo de experiência.</p>")
            .append("</div></header><main class='container'>");

        // Formulário (mantém o que a pessoa digitou)
        html.append("<form class='filtros' method='get' action='/candidatos'>")
            .append("<div class='campo'><label for='nome'>Nome</label>")
            .append("<input type='text' id='nome' name='nome' value='")
            .append(nome == null ? "" : HtmlUtils.htmlEscape(nome)).append("'></div>")
            .append("<div class='campo'><label for='cargo'>Cargo</label>")
            .append("<select id='cargo' name='cargo'><option value=''>Todos os cargos</option>");

        for (String c : service.listarTodos().stream().map(Candidato::getCargo).distinct().sorted().toList()) {
            boolean marcado = c.equalsIgnoreCase(cargo == null ? "" : cargo);
            html.append("<option value='").append(HtmlUtils.htmlEscape(c)).append("'")
                .append(marcado ? " selected" : "").append(">")
                .append(HtmlUtils.htmlEscape(c)).append("</option>");
        }

        html.append("</select></div>")
            .append("<div class='campo'><label for='experienciaMin'>Experiência mínima (anos)</label>")
            .append("<input type='number' min='0' id='experienciaMin' name='experienciaMin' value='")
            .append(experienciaMin == null ? "" : experienciaMin).append("'></div>")
            .append("<div class='acoes'><button type='submit'>Filtrar</button>")
            .append("<a class='limpar' href='/candidatos'>Limpar filtros</a></div></form>");

        html.append("<p class='resultado'>").append(lista.size()).append(" candidato(s) encontrado(s)</p>")
            .append("<section class='grade'>");

        // O "for" do Java faz o papel do th:each
        for (Candidato c : lista) {
            html.append("<article class='card'><div class='card-topo'>")
                .append("<div class='avatar'>").append(c.getNome().charAt(0)).append("</div>")
                .append("<h2>").append(HtmlUtils.htmlEscape(c.getNome())).append("</h2></div>")
                .append("<p class='cargo'>").append(HtmlUtils.htmlEscape(c.getCargo())).append("</p>")
                .append("<p class='meta'>").append(HtmlUtils.htmlEscape(c.getCidade()))
                .append(" · ").append(c.getExperiencia()).append(" ano(s) de experiência</p>")
                .append("<div class='tags'>");
            for (String t : c.getTecnologias().split(",")) {
                html.append("<span class='tag'>").append(HtmlUtils.htmlEscape(t.trim())).append("</span>");
            }
            html.append("</div></article>");
        }

        // O "if" do Java faz o papel do th:if
        if (lista.isEmpty()) {
            html.append("<div class='vazio'>Nenhum candidato encontrado. Tente remover algum filtro.</div>");
        }

        html.append("</section></main></body></html>");
        return html.toString();
    }

    // ------------------------------------------------------------------
    // Filtro usado pelos dois jeitos
    // ------------------------------------------------------------------
    private List<Candidato> filtrar(String nome, String cargo, Integer experienciaMin) {
        return service.listarTodos().stream()
                .filter(c -> nome == null || nome.isBlank()
                        || c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(c -> cargo == null || cargo.isBlank()
                        || c.getCargo().equalsIgnoreCase(cargo))
                .filter(c -> experienciaMin == null
                        || c.getExperiencia() >= experienciaMin)
                .toList();
    }
}
