package io.github.pablovns.services;

import com.google.gson.*;
import io.github.pablovns.domain.Noticia;
import io.github.pablovns.domain.TipoNoticia;
import io.github.pablovns.utils.LocalDateTimeAdapter;
import io.github.pablovns.utils.TipoNoticiaDeserializer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por buscar notícias da API do IBGE.
 */
public class NoticiaService {
    private static final String URL_BASE = "https://servicodados.ibge.gov.br/api/v3/noticias";
    private final HttpClient httpClient;
    private final Gson gson;

    public NoticiaService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(TipoNoticia.class, new TipoNoticiaDeserializer())
                .create();
    }

    public Optional<List<Noticia>> buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return Optional.empty();
        }

        try {
            String url = URL_BASE + "/?busca=" + titulo.replace(" ", "%20");
            return fazerRequisicao(url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restabelece o status da thread
            System.err.println("Busca por título interrompida.");
        } catch (IOException e) {
            System.err.println("Erro de I/O ao buscar notícias por título: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar notícias por título: " + e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<List<Noticia>> buscarPorPalavrasChave(String palavrasChave) {
        if (palavrasChave == null) {
            return Optional.empty();
        }

        try {
            String url = URL_BASE + "/?palavraChave=" + palavrasChave.replace(" ", "%20");
            return fazerRequisicao(url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Busca por palavras-chave interrompida.");
        } catch (IOException e) {
            System.err.println("Erro de I/O ao buscar notícias por palavras-chave: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar notícias por palavras-chave: " + e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<List<Noticia>> buscarPorData(LocalDate data) {
        if (data == null) {
            return Optional.empty();
        }

        try {
            String url = URL_BASE + "/?data=" + data;
            return fazerRequisicao(url);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Busca por data interrompida.");
        } catch (IOException e) {
            System.err.println("Erro de I/O ao buscar notícias por data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar notícias por data: " + e.getMessage());
        }

        return Optional.empty();
    }


    private Optional<List<Noticia>> fazerRequisicao(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na requisição: " + response.statusCode());
        }

        JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
        JsonArray items = responseJson.getAsJsonArray("items");
        List<Noticia> noticias = new ArrayList<>();

        for (JsonElement item : items) {
            try {
                Noticia noticia = gson.fromJson(item, Noticia.class);
                noticias.add(noticia);
            } catch (Exception e) {
                System.err.println("Erro ao processar notícia: " + e.getMessage());
            }
        }

        return Optional.of(noticias);
    }
} 