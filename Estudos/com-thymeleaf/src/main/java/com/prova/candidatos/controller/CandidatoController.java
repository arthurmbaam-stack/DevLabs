package com.prova.candidatos.controller;

import com.prova.candidatos.model.Candidato;
import com.prova.candidatos.service.CandidatoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller   // <- devolve o NOME de uma página (template), não JSON
public class CandidatoController {

    private final CandidatoService service;

    // Injeção de dependência pelo construtor
    public CandidatoController(CandidatoService service) {
        this.service = service;
    }

    // Atende "/" e "/candidatos". Exemplo: /candidatos?nome=ana&cargo=QA&experienciaMin=3
    @GetMapping({"/", "/candidatos"})
    public String listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Integer experienciaMin,
            Model model) {

        // 1) pega tudo do service e filtra com stream
        List<Candidato> filtrados = service.listarTodos().stream()
                .filter(c -> nome == null || nome.isBlank()
                        || c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .filter(c -> cargo == null || cargo.isBlank()
                        || c.getCargo().equalsIgnoreCase(cargo))
                .filter(c -> experienciaMin == null
                        || c.getExperiencia() >= experienciaMin)
                .toList();

        // 2) manda os dados para o HTML
        model.addAttribute("candidatos", filtrados);
        model.addAttribute("cargos", service.listarTodos().stream()
                .map(Candidato::getCargo).distinct().sorted().toList());

        // 3) devolve os valores digitados para o formulário não "esquecer" o filtro
        model.addAttribute("nome", nome);
        model.addAttribute("cargoSelecionado", cargo);
        model.addAttribute("experienciaMin", experienciaMin);

        // 4) nome do arquivo em src/main/resources/templates (sem o .html)
        return "candidatos";
    }
}
