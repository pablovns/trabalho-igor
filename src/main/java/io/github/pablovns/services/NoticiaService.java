package io.github.pablovns.services;

import com.google.gson.*;
import io.github.pablovns.domain.Noticia;
import io.github.pablovns.domain.TipoNoticia;
import io.github.pablovns.utils.LocalDateTimeAdapter;
import io.github.pablovns.utils.TipoNoticiaDeserializer;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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

    private String validarEntrada(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            return null;
        }
        
        String sanitizada = entrada.trim();
        
        // Verifica comprimento máximo
        if (sanitizada.length() > 100) {
            return null;
        }

        // Permite apenas letras, números, espaços e alguns caracteres especiais comuns
        Pattern padraoPermitido = Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s\\-.,!?()]+$");
        if (!padraoPermitido.matcher(sanitizada).matches()) {
            return null;
        }
        
        return sanitizada;
    }

    public Optional<List<Noticia>> buscarPorTitulo(String titulo) {
        String tituloSanitizado = validarEntrada(titulo);
        if (tituloSanitizado == null) {
            return Optional.empty();
        }

        try {
            String url = URL_BASE + "/?busca=" + URLEncoder.encode(tituloSanitizado, StandardCharsets.UTF_8);
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
        String palavrasChaveSanitizadas = validarEntrada(palavrasChave);
        if (palavrasChaveSanitizadas == null) {
            return Optional.empty();
        }

        try {
            String url = URL_BASE + "/?palavraChave=" + URLEncoder.encode(palavrasChaveSanitizadas, StandardCharsets.UTF_8);
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

        // Validação adicional da data
        if (data.isAfter(LocalDate.now())) {
            return Optional.empty(); // Não permite datas futuras
        }

        try {
            String dataString = data.toString();
            String url = URL_BASE + "/?data=" + URLEncoder.encode(dataString, StandardCharsets.UTF_8);
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
        // Validação adicional da URL
        if (url == null || url.trim().isEmpty()) {
            throw new IOException("URL inválida");
        }
        
        // Verifica se a URL começa com a base esperada
        if (!url.startsWith(URL_BASE)) {
            throw new IOException("URL não autorizada");
        }
        
        // Verifica comprimento máximo da URL
        if (url.length() > 2048) {
            throw new IOException("URL muito longa");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(java.time.Duration.ofSeconds(30))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na requisição: " + response.statusCode());
        }

        // Validação do corpo da resposta
        String responseBody = response.body();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return Optional.empty();
        }

        JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
        if (responseJson == null) {
            return Optional.empty();
        }
        
        JsonArray items = responseJson.getAsJsonArray("items");
        if (items == null) {
            return Optional.empty();
        }
        
        List<Noticia> noticias = new ArrayList<>();

        for (JsonElement item : items) {
            try {
                Noticia noticia = gson.fromJson(item, Noticia.class);
                if (noticia != null && noticia.getId() != null) {
                    noticias.add(noticia);
                }
            } catch (Exception e) {
                System.err.println("Erro ao processar notícia: " + e.getMessage());
            }
        }

        return Optional.of(noticias);
    }
} 