package io.github.pablovns.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um usuário do sistema.
 */
public class Usuario {
    private String nome;
    private List<Noticia> noticiasFavoritas;
    private List<Noticia> noticiasLidas;
    private List<Noticia> noticiasParaLerDepois;

    public Usuario(String nome) {
        this.nome = nome;
        this.noticiasFavoritas = new ArrayList<>();
        this.noticiasLidas = new ArrayList<>();
        this.noticiasParaLerDepois = new ArrayList<>();
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