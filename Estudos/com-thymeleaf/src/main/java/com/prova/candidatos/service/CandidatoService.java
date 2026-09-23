package com.prova.candidatos.service;

import com.prova.candidatos.model.Candidato;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Na prova este service já vem pronto. Você só INJETA ele no controller.
@Service
public class CandidatoService {

    private final List<Candidato> candidatos = List.of(
        new Candidato(1L, "Ana Souza",      "Desenvolvedor Java",  "Belo Horizonte", 5, "Java, Spring Boot, SQL"),
        new Candidato(2L, "Bruno Lima",     "Desenvolvedor Java",  "Betim",          2, "Java, JPA, Git"),
        new Candidato(3L, "Carla Mendes",   "Desenvolvedor Front", "Contagem",       3, "HTML, CSS, JavaScript"),
        new Candidato(4L, "Diego Ferreira", "Analista de Dados",   "Belo Horizonte", 4, "Python, SQL, Power BI"),
        new Candidato(5L, "Elisa Rocha",    "Desenvolvedor Front", "Betim",          1, "HTML, CSS, React"),
        new Candidato(6L, "Fábio Andrade",  "Desenvolvedor Java",  "Contagem",       8, "Java, Spring, Docker"),
        new Candidato(7L, "Giovana Alves",  "Analista de Dados",   "Betim",          6, "SQL, Python, Excel"),
        new Candidato(8L, "Henrique Costa", "QA",                  "Belo Horizonte", 3, "Selenium, Java, Postman")
    );

    public List<Candidato> listarTodos() {
        return candidatos;
    }

    public Optional<Candidato> buscarPorId(Long id) {
        return candidatos.stream().filter(c -> c.getId().equals(id)).findFirst();
    }
}
