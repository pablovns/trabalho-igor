package io.github.pablovns.domain;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Classe que representa uma notícia do IBGE.
 */
public class Noticia {
    public static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss", Locale.of("pt", "BR"));

    private Long id;
    private String titulo;
    private String introducao;
    @SerializedName("data_publicacao")
    private LocalDateTime dataPublicacao;
    private String link;
    private TipoNoticia tipo;
    private boolean lida;
    private boolean favorita;
    private boolean paraLerDepois;

    // Construtor padrão necessário para o Gson
    public Noticia() {
        this.lida = false;
        this.favorita = false;
        this.paraLerDepois = false;
    }

    public Noticia(Long id, String titulo, String introducao, LocalDateTime dataPublicacao, String link, TipoNoticia tipo) {
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

    public TipoNoticia getTipo() {
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
                dataPublicacao.format(FORMATADOR),
                tipo.getDescricao(),
                link,
                lida ? "Lida" : "Não lida",
                favorita ? "Favorita" : "Não favorita",
                paraLerDepois ? "Para ler depois" : "Não marcada para ler depois"
        );
    }

    public void alterarFavorita() {
        favorita = !favorita; // só inverte o valor
    }

    public void alterarLida() {
        lida = !lida; // só inverte o valor
    }

    public void alterarLerDepois() {
        paraLerDepois = !paraLerDepois; // só inverte o valor
    }
}