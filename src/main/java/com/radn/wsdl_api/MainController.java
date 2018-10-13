package com.radn.wsdl_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.radn.domainClasses.*;

@RestController
public class MainController {
    @Autowired
    SendReceiveWsdl wsdl;
    private int operationCounter;
    public static final int maxOperations = 10000;

    @RequestMapping("/add")
    @Cacheable("operations")
    public AddResponse add(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        return wsdl.getAdd(A, B);
    }
    @RequestMapping("/subtract")
    @Cacheable("operations")
    public SubtractResponse subtract(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        return wsdl.getSubtract(A, B);
    }
    @RequestMapping("/multiply")
    @Cacheable("operations")
    public MultiplyResponse multiply(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        return wsdl.getMultiply(A, B);
    }
    @RequestMapping("/divide")
    @Cacheable("operations")
    public DivideResponse divide(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        if (operationCounter > maxOperations)
            return null;
        return wsdl.getDivide(A, B);
    }
    @Scheduled(initialDelayString = "${scheduler.delay}", fixedDelayString = "${scheduler.fixedDelay}")
    public void checkOperations() {
        System.out.println("Checking operations...");
        operationCounter = 0;
    }
    @Scheduled(initialDelayString = "${scheduler.evict.delay}", fixedDelayString = "${scheduler.evict.delay}")
    @CacheEvict(value="operations", allEntries = true)
    public void evictCache() {

    }
}
