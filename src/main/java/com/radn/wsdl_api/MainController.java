package com.radn.wsdl_api;

import com.radn.wsdl_api.com.radn.entities.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.radn.domainClasses.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    private SendReceiveWsdl wsdl;
    @Autowired
    private OperationRepository repository;

    private int operationCounter;
    public static final int maxOperations = 10000;

    @RequestMapping("/add")
    @Cacheable("operations")
    @Transactional
    public Operation add(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        System.out.println("Requesting result..");
        AddResponse resp = wsdl.getAdd(A, B);
        Operation op = repository.newOperation('+',A,B,resp.getAddResult());
        return op;
    }
    @Transactional
    @RequestMapping("/subtract")
    @Cacheable("operations")
    public Operation subtract(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        System.out.println("Requesting result..");
        SubtractResponse resp = wsdl.getSubtract(A, B);
        return repository.newOperation('-',A,B,resp.getSubtractResult());
    }
    @Transactional
    @RequestMapping("/multiply")
    @Cacheable("operations")
    public Operation multiply(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        System.out.println("Requesting result..");
        MultiplyResponse resp = wsdl.getMultiply(A, B);
        return repository.newOperation('*',A,B,resp.getMultiplyResult());
    }
    @Transactional
    @RequestMapping("/divide")
    @Cacheable("operations")
    public Operation divide(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        System.out.println("Requesting result..");
        DivideResponse resp = wsdl.getDivide(A, B);

        return repository.newOperation('/',A,B,resp.getDivideResult());
    }
    @Transactional
    @RequestMapping("/list")
    public List<Operation> getOperations() {
        return repository.listDayOperations(LocalDateTime.now());
    }
    @Scheduled(initialDelayString = "#{${scheduler.delay}*60000}", fixedDelayString = "#{${scheduler.delay}*60000}")
    public void checkOperations() {
        System.out.println("Zeroing operations");
        operationCounter = 0;
    }
    @Scheduled(initialDelayString = "#{${scheduler.evict.delay}*60000}", fixedDelayString = "#{${scheduler.evict.delay}*60000}")
    @CacheEvict(value="operations", allEntries = true)
    public void evictCache() {
        System.out.println("Cache evicting");
    }

    public int getOperationCounter() {
        return operationCounter;
    }

    public void setOperationCounter(int operationCounter) {
        this.operationCounter = operationCounter;
    }
}
