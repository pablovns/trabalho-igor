package io.github.pablovns.utils;

import io.github.pablovns.domain.Noticia;

import java.util.Comparator;
import java.util.List;

public class OrdenadorNoticias {

    private OrdenadorNoticias() {
        throw new IllegalStateException("Classe utilitária não deve ser instanciada");
    }

    public static List<Noticia> ordenarPorTitulo(List<Noticia> noticias) {
        return noticias.stream()
                .sorted(Comparator.comparing(Noticia::getTitulo))
                .toList();
    }

    public static List<Noticia> ordenarPorData(List<Noticia> noticias) {
        return noticias.stream()
                .sorted(Comparator.comparing(Noticia::getDataPublicacao))
                .toList();
    }

    public static List<Noticia> ordenarPorTipo(List<Noticia> noticias) {
        return noticias.stream()
                .sorted(Comparator.comparing(Noticia::getTipo))
                .toList();
    }

    public static List<Noticia> ordenarPorId(List<Noticia> noticias) {
        return noticias.stream()
                .sorted(Comparator.comparing(Noticia::getId).reversed())
                .toList();
    }
}
