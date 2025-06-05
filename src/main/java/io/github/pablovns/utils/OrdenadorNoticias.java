package io.github.pablovns.utils;

import io.github.pablovns.domain.Noticia;

import java.util.Comparator;
import java.util.List;

/**
 * Classe utilitária para ordenação de notícias.
 */
public class OrdenadorNoticias {
    
    /**
     * Ordena a lista de notícias por título em ordem alfabética.
     *
     * @param noticias Lista de notícias a ser ordenada
     */
    public static void ordenarPorTitulo(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getTitulo));
    }

    /**
     * Ordena a lista de notícias por data de publicação.
     *
     * @param noticias Lista de notícias a ser ordenada
     */
    public static void ordenarPorData(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getDataPublicacao).reversed());
    }

    /**
     * Ordena a lista de notícias por tipo.
     *
     * @param noticias Lista de notícias a ser ordenada
     */
    public static void ordenarPorTipo(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getTipo));
    }
} 