package com.ra12.projecte1.service;

import com.ra12.projecte1.model.Champion;
import com.ra12.projecte1.repository.ChampionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChampionService {

    private static final Logger log = LoggerFactory.getLogger(ChampionService.class);

    @Autowired
    private ChampionRepository championRepository;

    // Obtener todos
    public List<Champion> findAll() {
        log.info("Service - Obtener todos los champions");
        return championRepository.findAll();
    }

    // Obtener por id
    public Champion findById(long id) {
        log.info("Service - Buscar champion con id {}", id);
        return championRepository.findById(id);
    }

    // Guardar nuevo champion
    public void save(Champion champion) {
        log.info("Service - Guardar nuevo champion: {}", champion.getName());
        championRepository.save(champion);
    }

    // [b2] Update completo por id
    public boolean update(long id, Champion updatedChampion) {
        log.info("Service - Actualizar champion con id {}", id);

        Champion existing = championRepository.findById(id);
        if (existing == null) {
            log.warn("Service - Champion con id {} no existe", id);
            return false;
        }

        // Aseguramos que se actualiza el correcto
        updatedChampion.setId(id);
        championRepository.update(updatedChampion);
        return true;
    }

    // [a2] Update solo de la imagen
    public boolean updateImage(long id, String imageUrl) {
        log.info("Service - Actualizar imagen del champion con id {}", id);

        Champion existing = championRepository.findById(id);
        if (existing == null) {
            log.warn("Service - Champion con id {} no existe", id);
            return false;
        }

        championRepository.updateImage(id, imageUrl);
        return true;
    }

    // [d2] Delete por id
    public boolean deleteById(long id) {
        log.info("Service - Eliminar champion con id {}", id);
        return championRepository.deleteById(id);
    }

    // [c2] Delete todos
    public void deleteAll() {
        log.info("Service - Eliminar TODOS los champions");
        championRepository.deleteAll();
    }
}

