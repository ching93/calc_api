package com.radn.wsdl_api;

import com.radn.wsdl_api.com.radn.entities.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Repository
public class OperationRepositoryImpl implements OperationRepository {
    @Autowired
    EntityManager manager;

    @Override
    public void newOperation(int A, int B, int result) {
        Operation op = new Operation(A, B, result);
        manager.persist(op);
    }

    @Override
    public void listOperations(LocalDateTime from, LocalDateTime to) {

    }
}
