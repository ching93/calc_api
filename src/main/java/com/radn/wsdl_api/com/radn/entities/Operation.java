package com.radn.wsdl_api.com.radn.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Operation {
    private Long id;
    private int version;
    private int firstOperand;
    private int secondOperand;
    private int result;
    private char type;
    @Temporal(value=TemporalType.TIMESTAMP)
    private LocalDateTime created;

    public Operation(char type, int firstOperand, int secondOperand, int result) {
        this();
        this.type = type;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.result = result;
    }

    public Operation() {
        created = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFirstOperand() {
        return firstOperand;
    }

    public void setFirstOperand(int firstOperand) {
        this.firstOperand = firstOperand;
    }

    public int getSecondOperand() {
        return secondOperand;
    }

    public void setSecondOperand(int secondOperand) {
        this.secondOperand = secondOperand;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return String.format("%d %s %d",firstOperand,type,secondOperand);
    }
}
