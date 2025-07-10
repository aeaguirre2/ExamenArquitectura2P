package com.examen2p.sistema.repository;

import com.examen2p.sistema.model.TransaccionTurno;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransaccionTurnoRepository extends MongoRepository<TransaccionTurno, String> {
    List<TransaccionTurno> findByCodigoTurno(String codigoTurno);
} 