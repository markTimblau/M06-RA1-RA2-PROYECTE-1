package com.ra12.projecte1.controller;

import com.ra12.projecte1.model.Champion;
import com.ra12.projecte1.service.ChampionService;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/champions")
public class ChampionController {

    private static final Logger log = LoggerFactory.getLogger(ChampionController.class);

    @Autowired
    private ChampionService championService;

    // GET - obtener todos los champions
    @GetMapping
    public ResponseEntity<List<Champion>> getAllChampions() {
        log.info("GET /champions - Obtener todos los champions");
        return ResponseEntity.ok(championService.findAll());
    }

    // GET - obtener champion por id
    @GetMapping("/{id}")
    public ResponseEntity<Champion> getChampionById(@PathVariable long id) {
        log.info("GET /champions/{} - Obtener champion", id);

        Champion champion = championService.findById(id);
        if (champion == null) {
            log.warn("Champion con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(champion);
    }

    // POST - crear nuevo champion
    @PostMapping
    public ResponseEntity<Void> createChampion(@RequestBody Champion champion) {
        log.info("POST /champions - Crear nuevo champion");
        championService.save(champion);
        return ResponseEntity.ok().build();
    }

    // [b2] PUT - update completo por id
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateChampion(
            @PathVariable long id,
            @RequestBody Champion champion) {

        log.info("PUT /champions/{} - Actualizar champion", id);

        boolean updated = championService.update(id, champion);
        if (!updated) {
            log.warn("No se pudo actualizar champion con id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // [a2] PUT - actualizar SOLO la imagen
    @PutMapping("/{id}/image")
    public ResponseEntity<Void> updateChampionImage(
            @PathVariable long id,
            @RequestParam String imageUrl) {

        log.info("PUT /champions/{}/image - Actualizar imagen", id);

        boolean updated = championService.updateImage(id, imageUrl);
        if (!updated) {
            log.warn("No se pudo actualizar la imagen del champion {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // [d2] DELETE - borrar por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChampionById(@PathVariable long id) {
        log.info("DELETE /champions/{} - Eliminar champion", id);

        boolean deleted = championService.deleteById(id);
        if (!deleted) {
            log.warn("No se pudo eliminar champion con id {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // [c2] DELETE - borrar TODOS
    @DeleteMapping
    public ResponseEntity<Void> deleteAllChampions() {
        log.info("DELETE /champions - Eliminar todos los champions");

        championService.deleteAll();
        return ResponseEntity.ok().build();
    }

    // POST - cargar champions desde CSV
    @PostMapping("/csv/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        log.info("POST /champions/csv/upload - Cargar champions desde CSV");

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        try {
            int count = championService.loadFromCsv(file);
            return ResponseEntity.ok("Se cargaron " + count + " champions correctamente");
        } catch (IOException e) {
            log.error("Error al leer el archivo CSV", e);
            return ResponseEntity.internalServerError().body("Error al leer el archivo: " + e.getMessage());
        } catch (CsvException e) {
            log.error("Error al procesar el CSV", e);
            return ResponseEntity.badRequest().body("Error al procesar el CSV: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}

