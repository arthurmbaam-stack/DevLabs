package com.example.RadioAPI.controller;

import com.example.RadioAPI.model.RadioAPImodel;
import com.example.RadioAPI.service.RadioAPIservice;
import com.example.RadioAPI.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class RadioAPicontroller {

    @Autowired
    private RadioAPIservice radioApiService;
    @Autowired
    private FavoritoService favoritoService;

    @GetMapping("Radio")
    public String listRadioAPImodel(Model model) {
        List<RadioAPImodel> radioStations = radioApiService.listRadioAPImodel();
        model.addAttribute("stations", radioStations);
        model.addAttribute("favoritosIds", favoritoService.listarIds());
        return "home";
    }

    @PostMapping("/favoritos/adicionar")
    public String adicionarFavorito(
    @RequestParam String stationuuid,
    @RequestParam  String name,
    @RequestParam  String url,
    @RequestParam  String favicon){
        if (favoritoService.estaFavoritado(stationuuid)) {
            favoritoService.remover(stationuuid);
        } else{
        RadioAPImodel radio = new RadioAPImodel();
        radio.setStationuuid(stationuuid);
        radio.setName(name);
        radio.setUrl(url);
        radio.setFavicon(favicon);
        favoritoService.adicionar(radio);
        }
        return "redirect:/Radio";
    }
    @GetMapping("/favoritos")
    public String listarFavoritos(Model model){
        model.addAttribute("favoritos", favoritoService.listar());
        return "favoritos";
    }
    @PostMapping("/favoritos/remover")
        public String removerFavorito(@RequestParam String stationuuid) {
            favoritoService.remover(stationuuid);
            return "redirect:/favoritos";
        }
    
}
