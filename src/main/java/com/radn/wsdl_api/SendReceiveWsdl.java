package com.radn.wsdl_api;

import com.radn.domainClasses.*;

public interface SendReceiveWsdl {
    AddResponse getAdd(int A, int B);
    DivideResponse getDivide(int A, int B);
    MultiplyResponse getMultiply(int A, int B);
    SubtractResponse getSubtract(int A, int B);
}
