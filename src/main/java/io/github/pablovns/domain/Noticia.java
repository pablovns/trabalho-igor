package io.github.pablovns.domain;

import java.time.LocalDateTime;

/**
 * Classe que representa uma notícia do IBGE.
 */
public class Noticia {
    private Long id;
    private String titulo;
    private String introducao;
    private LocalDateTime dataPublicacao;
    private String link;
    private String tipo;
    private boolean lida;
    private boolean favorita;
    private boolean paraLerDepois;

    public Noticia(Long id, String titulo, String introducao, LocalDateTime dataPublicacao, String link, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.introducao = introducao;
        this.dataPublicacao = dataPublicacao;
        this.link = link;
        this.tipo = tipo;
        this.lida = false;
        this.favorita = false;
        this.paraLerDepois = false;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIntroducao() {
        return introducao;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public String getLink() {
        return link;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }

    public boolean isParaLerDepois() {
        return paraLerDepois;
    }

    public void setParaLerDepois(boolean paraLerDepois) {
        this.paraLerDepois = paraLerDepois;
    }

    @Override
    public String toString() {
        return String.format("""
                Título: %s
                Introdução: %s
                Data de Publicação: %s
                Tipo: %s
                Link: %s
                Status: %s | %s | %s""",
                titulo,
                introducao,
                dataPublicacao,
                tipo,
                link,
                lida ? "Lida" : "Não lida",
                favorita ? "Favorita" : "Não favorita",
                paraLerDepois ? "Para ler depois" : "Não marcada para ler depois"
        );
    }
} 