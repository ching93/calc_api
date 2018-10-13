package com.radn.wsdl_api;

import com.radn.wsdl_api.com.radn.entities.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OperationRepositoryImpl implements OperationRepository {
    @Autowired
    EntityManager manager;

    @Override
    public Operation newOperation(char type, int A, int B, int result) {
        Operation op = new Operation(type, A, B, result);
        manager.persist(op);
        manager.refresh(op);
        return op;
    }

    @Override
    public List<Operation> listOperations(LocalDateTime from, LocalDateTime to) {
        Query query = manager.createQuery("Select o From Operation o Where o.created between :from and :to", Operation.class);
        query.setParameter("from", from);
        query.setParameter("to", to);
        return query.getResultList();
    }

    @Override
    public List<Operation> listDayOperations(LocalDateTime day) {
        LocalDateTime from = LocalDateTime.of(day.getYear(),day.getMonthValue(),day.getDayOfMonth(),0,0);
        return listOperations(from, day);
    }
}
