package io.github.pablovns.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um usuário do sistema.
 */
public class Usuario {
    private String nome;
    private final List<Noticia> noticiasFavoritas;
    private final List<Noticia> noticiasLidas;
    private final List<Noticia> noticiasParaLerDepois;

    // Construtor padrão necessário para o Gson
    public Usuario() {
        this.noticiasFavoritas = new ArrayList<>();
        this.noticiasLidas = new ArrayList<>();
        this.noticiasParaLerDepois = new ArrayList<>();
    }

    public Usuario(String nome) {
        this();  // Chama o construtor padrão para inicializar as listas
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Noticia> getNoticiasFavoritas() {
        return noticiasFavoritas;
    }

    public List<Noticia> getNoticiasLidas() {
        return noticiasLidas;
    }

    public List<Noticia> getNoticiasParaLerDepois() {
        return noticiasParaLerDepois;
    }

    public void adicionarNoticiaFavorita(Noticia noticia) {
        if (!noticiasFavoritas.contains(noticia)) {
            noticiasFavoritas.add(noticia);
            noticia.setFavorita(true);
        }
    }

    public void removerNoticiaFavorita(Noticia noticia) {
        noticiasFavoritas.remove(noticia);
        noticia.setFavorita(false);
    }

    public void marcarComoLida(Noticia noticia) {
        if (!noticiasLidas.contains(noticia)) {
            noticiasLidas.add(noticia);
            noticia.setLida(true);
        }
    }

    public void adicionarParaLerDepois(Noticia noticia) {
        if (!noticiasParaLerDepois.contains(noticia)) {
            noticiasParaLerDepois.add(noticia);
            noticia.setParaLerDepois(true);
        }
    }

    public void removerParaLerDepois(Noticia noticia) {
        noticiasParaLerDepois.remove(noticia);
        noticia.setParaLerDepois(false);
    }
} 