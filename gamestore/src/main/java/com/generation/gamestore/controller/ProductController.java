package com.generation.gamestore.controller;

import com.generation.gamestore.model.Product;
import com.generation.gamestore.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id){
        return repository.findById(id)
                .map(res -> ResponseEntity.ok(res))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<Product>> getByName(@PathVariable String name){
        return ResponseEntity.ok(repository.findAllByNameContainingIgnoreCase(name));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(product));
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product product){
        return repository.findById(product.getId())
                .map(res -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(repository.save(product)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        Optional<Product> product = repository.findById(id);

        if (product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        repository.deleteById(id);
    }

}