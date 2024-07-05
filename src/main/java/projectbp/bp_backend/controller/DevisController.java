package projectbp.bp_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectbp.bp_backend.bean.Devis;
import projectbp.bp_backend.dto.CRUD.DevisRequest;
import projectbp.bp_backend.service.DevisService;


import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DevisController {


    @GetMapping("/public/devis/numero/{numero}")
    public Optional<Devis> findByNumero(@PathVariable String numero) {
        return devisService.findByNumero(numero);
    }


    @GetMapping("/public/devis")
    public ResponseEntity<Object> getAllDevis() {
        return ResponseEntity.ok(devisService.findAll());
    }


    @PostMapping("/public/savedevis")
    public ResponseEntity<Object> createDevis(@RequestBody DevisRequest devisRequest) {
        return ResponseEntity.ok(devisService.createDevis(devisRequest));
    }

    private final DevisService devisService;
}
