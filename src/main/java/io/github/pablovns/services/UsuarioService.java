package io.github.pablovns.services;

import com.google.gson.*;
import io.github.pablovns.domain.Usuario;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar a persistência dos dados do usuário.
 */
public class UsuarioService {
    private static final String DIRETORIO_DADOS = "dados";
    private static final String ARQUIVO_USUARIO = DIRETORIO_DADOS + "/usuario.json";
    private final Gson gson;

    public UsuarioService() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        criarDiretorioSeNaoExistir();
    }

    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }

    private void criarDiretorioSeNaoExistir() {
        try {
            Path path = Paths.get(DIRETORIO_DADOS);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    /**
     * Salva os dados do usuário em arquivo JSON.
     *
     * @param usuario O usuário a ser salvo
     */
    public void salvarUsuario(Usuario usuario) {
        try (FileWriter writer = new FileWriter(ARQUIVO_USUARIO)) {
            gson.toJson(usuario, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    /**
     * Carrega os dados do usuário do arquivo JSON.
     *
     * @return O usuário carregado ou Optional vazio se não existir
     */
    public Optional<Usuario> carregarUsuario() {
        File arquivo = new File(ARQUIVO_USUARIO);
        if (!arquivo.exists()) {
            return Optional.empty();
        }

        try (FileReader reader = new FileReader(arquivo)) {
            return Optional.ofNullable(gson.fromJson(reader, Usuario.class));
        } catch (IOException e) {
            System.err.println("Erro ao carregar usuário: " + e.getMessage());
            return Optional.empty();
        }
    }
} 