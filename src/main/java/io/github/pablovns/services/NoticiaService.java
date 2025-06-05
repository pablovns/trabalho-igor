package io.github.pablovns.services;

import com.google.gson.*;
import io.github.pablovns.domain.Noticia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Serviço responsável por buscar notícias da API do IBGE.
 */
public class NoticiaService {
    private static final String BASE_URL = "https://servicodados.ibge.gov.br/api/v3/noticias";
    private final HttpClient httpClient;
    private final Gson gson;

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        };

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatters[0].format(src));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            String dateStr = json.getAsString();
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDateTime.parse(dateStr, formatter);
                } catch (DateTimeParseException ignored) {
                }
            }
            throw new JsonParseException("Não foi possível converter a data: " + dateStr);
        }
    }

    public NoticiaService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * Busca notícias por título.
     *
     * @param titulo O título a ser buscado
     * @return Lista de notícias encontradas
     */
    public List<Noticia> buscarPorTitulo(String titulo) {
        try {
            String url = BASE_URL + "/?busca=" + titulo.replace(" ", "%20");
            return fazerRequisicao(url);
        } catch (Exception e) {
            System.err.println("Erro ao buscar notícias por título: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca notícias por palavras-chave.
     *
     * @param palavrasChave As palavras-chave a serem buscadas
     * @return Lista de notícias encontradas
     */
    public List<Noticia> buscarPorPalavrasChave(String palavrasChave) {
        try {
            String url = BASE_URL + "/?palavraChave=" + palavrasChave.replace(" ", "%20");
            return fazerRequisicao(url);
        } catch (Exception e) {
            System.err.println("Erro ao buscar notícias por palavras-chave: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca notícias por data.
     *
     * @param data A data no formato YYYYMMDD
     * @return Lista de notícias encontradas
     */
    public List<Noticia> buscarPorData(String data) {
        try {
            String url = BASE_URL + "/?data=" + data;
            return fazerRequisicao(url);
        } catch (Exception e) {
            System.err.println("Erro ao buscar notícias por data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Noticia> fazerRequisicao(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na requisição: " + response.statusCode());
        }

        JsonArray items = JsonParser.parseString(response.body())
                .getAsJsonObject()
                .getAsJsonArray("items");

        List<Noticia> noticias = new ArrayList<>();
        for (JsonElement item : items) {
            JsonObject noticiaJson = item.getAsJsonObject();
            
            try {
                Noticia noticia = new Noticia(
                        noticiaJson.get("id").getAsLong(),
                        noticiaJson.get("titulo").getAsString(),
                        noticiaJson.get("introducao").getAsString(),
                        LocalDateTime.parse(noticiaJson.get("data_publicacao").getAsString(), 
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                        noticiaJson.get("link").getAsString(),
                        noticiaJson.get("tipo").getAsString()
                );
                noticias.add(noticia);
            } catch (Exception e) {
                System.err.println("Erro ao processar notícia: " + e.getMessage());
            }
        }

        return noticias;
    }
} 