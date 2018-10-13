package com.radn.wsdl_api;


import com.radn.wsdl_api.com.radn.entities.Operation;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationRepository {
    Operation newOperation(char type, int A, int B, int result);
    List<Operation> listOperations(LocalDateTime from, LocalDateTime to);
    List<Operation> listDayOperations(LocalDateTime day);
}
