package com.radn.wsdl_api;


import java.time.LocalDateTime;

public interface OperationRepository {
    void newOperation(int A, int B, int result);
    void listOperations(LocalDateTime from, LocalDateTime to);
}
