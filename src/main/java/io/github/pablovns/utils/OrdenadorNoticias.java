package io.github.pablovns.utils;

import io.github.pablovns.domain.Noticia;

import java.util.Comparator;
import java.util.List;

public class OrdenadorNoticias {

    private OrdenadorNoticias() {
        throw new IllegalStateException("Classe utilitária não deve ser instanciada");
    }

    public static void ordenarPorTitulo(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getTitulo));
    }

    public static void ordenarPorData(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getDataPublicacao));
    }

    public static void ordenarPorTipo(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getTipo));
    }

    public static void ordenarPorId(List<Noticia> noticias) {
        noticias.sort(Comparator.comparing(Noticia::getId).reversed());
    }
} 