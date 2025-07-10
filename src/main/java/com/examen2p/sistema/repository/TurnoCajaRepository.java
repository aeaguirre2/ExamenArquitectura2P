package com.examen2p.sistema.repository;

import com.examen2p.sistema.model.TurnoCaja;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TurnoCajaRepository extends MongoRepository<TurnoCaja, String> {
    // Métodos personalizados si necesitas
} 