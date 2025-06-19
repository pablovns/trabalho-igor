package io.github.pablovns.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
        this();
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public void salvarOuAtualizarNoticia(Noticia noticia) {
        Optional<Noticia> noticiaOptional = obterNoticia(noticia.getId());

        // Se a notícia já existe na lista de notícias do usuário, apenas atualiza os atributos
        if (noticiaOptional.isPresent()) {
            Noticia n = noticiaOptional.get();
            n.setFavorita(noticia.isFavorita());
            n.setLida(noticia.isLida());
            n.setParaLerDepois(noticia.isParaLerDepois());
        } else {
            // Se a notícia não existe, adiciona à lista de notícias
            noticias.add(noticia);
        }
    }

    public boolean noticiaExiste(Long idNoticia) {
        return noticias.stream()
                .anyMatch(n -> n.getId().equals(idNoticia));
    }

    public Optional<Noticia> obterNoticia(Long idNoticia) {
        return noticias.stream()
                .filter(n -> n.getId().equals(idNoticia))
                .findFirst();
    }

    public List<Noticia> listarNoticiasFavoritas() {
        return filtrarNoticias(Noticia::isFavorita);
    }

    public List<Noticia> listarNoticiasLidas() {
        return filtrarNoticias(Noticia::isLida);
    }

    public List<Noticia> listarNoticiasParaLerDepois() {
        return filtrarNoticias(Noticia::isParaLerDepois);
    }

    private List<Noticia> filtrarNoticias(Predicate<Noticia> criterio) {
        return noticias.stream()
                .filter(criterio)
                .toList();
    }
}
