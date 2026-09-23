package com.prova.candidatos.model;

public class Candidato {

    private Long id;
    private String nome;
    private String cargo;
    private String cidade;
    private int experiencia;      // anos de experiência
    private String tecnologias;

    public Candidato(Long id, String nome, String cargo, String cidade,
                     int experiencia, String tecnologias) {
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.cidade = cidade;
        this.experiencia = experiencia;
        this.tecnologias = tecnologias;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public String getCidade() { return cidade; }
    public int getExperiencia() { return experiencia; }
    public String getTecnologias() { return tecnologias; }
}
