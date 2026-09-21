package com.example.RadioAPI.service;

import com.example.RadioAPI.model.RadioAPImodel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FavoritoService {

    private final Map<String, RadioAPImodel> favoritos =
            new LinkedHashMap<>();

    public void adicionar(RadioAPImodel radio) {
        favoritos.put(radio.getStationuuid(), radio);
    }

    public void remover(String stationuuid) {
        favoritos.remove(stationuuid);
    }

    public List<RadioAPImodel> listar() {
        return new ArrayList<>(favoritos.values());
    }
    public boolean estaFavoritado(String stationuuid) {
        return favoritos.containsKey(stationuuid);
    }
    public Set<String> listarIds() {
        return new HashSet<>(favoritos.keySet());
    }
}
