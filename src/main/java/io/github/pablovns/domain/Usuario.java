package io.github.pablovns.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um usuário do sistema.
 */
public class Usuario {
    private String nome;
    private final List<Noticia> noticias;

    // Construtor padrão necessário para o Gson
    public Usuario() {
        this.noticias = new ArrayList<>();
    }

    public Usuario(String nome) {
        this();  // Chama o construtor padrão para inicializar as listas
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public List<Noticia> listarNoticiasFavoritas() {
        if (noticias == null || noticias.isEmpty()) {
            return List.of();
        }

        return noticias.stream()
                .filter(Noticia::isFavorita)
                .toList();
    }

    public List<Noticia> listarNoticiasLidas() {
        if (noticias == null || noticias.isEmpty()) {
            return List.of();
        }

        return noticias.stream()
                .filter(Noticia::isLida)
                .toList();
    }

    public List<Noticia> listarNoticiasParaLerDepois() {
        if (noticias == null || noticias.isEmpty()) {
            return List.of();
        }

        return noticias.stream()
                .filter(Noticia::isParaLerDepois)
                .toList();
    }

}