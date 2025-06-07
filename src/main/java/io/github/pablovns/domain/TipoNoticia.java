package io.github.pablovns.domain;

public enum TipoNoticia {
    NOTICIA("Notícia"),
    RELEASE("Release"),
    ;

    private final String descricao;

    TipoNoticia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
