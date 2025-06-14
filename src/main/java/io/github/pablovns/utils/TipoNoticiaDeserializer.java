package io.github.pablovns.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.github.pablovns.domain.TipoNoticia;

import java.lang.reflect.Type;

public class TipoNoticiaDeserializer implements JsonDeserializer<TipoNoticia> {
    @Override
    public TipoNoticia deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String descricao = json.getAsString();
        for (TipoNoticia tipo : TipoNoticia.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new JsonParseException("Tipo de notícia desconhecido: " + descricao);
    }
}

