package com.radn.wsdl_api;

import com.radn.wsdl_api.com.radn.entities.Operation;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class MainController {
    @Autowired
    private SendReceiveWsdl wsdl;
    @Autowired
    private OperationRepository repository;

    private int operationCounter;
    @Value("${scheduler.maxOperations}")
    public int maxOperations;

    public int getMaxOperations() {
        return maxOperations;
    }

    public void setMaxOperations(int maxOperations) {
        this.maxOperations = maxOperations;
    }

    @Transactional
    @RequestMapping("/add")
    public Operation add(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        return createOperation('+', A,B);
    }
    @Transactional
    @RequestMapping("/subtract")
    public Operation subtract(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        return createOperation('-', A,B);
    }
    @Transactional
    @RequestMapping("/multiply")
    public Operation multiply(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        return createOperation('*', A,B);
    }
    @Transactional
    @RequestMapping("/divide")
    public Operation divide(@RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        return createOperation('/', A,B);
    }
    @Transactional
    @RequestMapping("/operation")
    public Operation operation(@RequestParam(value="type")char type, @RequestParam(value="A")int A, @RequestParam(value="B")int B) {
        // + - %2B, / - %2F, * - %2A
        if ("+-/*".indexOf((int)type) != -1)
            return createOperation(type, A,B);
        else
            return null;
    }
    /*@RequestMapping("/expression")
    public List<Operation> expression(@RequestParam(value="expr")String expr) {
        Matcher matches = Pattern.compile("(\\d+)\\s+([\\/\\+\\*\\-])\\s+(\\d+)").matcher(expr);
        while (matches.find()) {
            int first = Integer.parseInt(matches.group(0));
            char op = matches.group(1).charAt(0);
            int second = Integer.parseInt(matches.group(2));
        }
    }*/
    @Cacheable("operations")
    public Operation createOperation(char type, int A, int B) {
        if (operationCounter > maxOperations)
            return null;
        //System.out.println("Requesting result..");
        int result;
        switch (type) {
            case '+':
                AddResponse addresp = wsdl.getAdd(A, B);
                result = addresp.getAddResult();
                break;
            case '-':
                SubtractResponse subtrresp = wsdl.getSubtract(A, B);
                result = subtrresp.getSubtractResult();
                break;
            case '*':
                MultiplyResponse mulresp = wsdl.getMultiply(A, B);
                result = mulresp.getMultiplyResult();
                break;
            case '/':
                DivideResponse divresp = wsdl.getDivide(A, B);
                result = divresp.getDivideResult();
                break;
            default:
                return null;
        }
        Operation op = repository.newOperation(type,A, B, result);
        operationCounter++;
        return op;
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
