package com.radn.wsdl_api;


import com.radn.domainClasses.*;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;


public class SendReceiveClient extends WebServiceGatewaySupport implements SendReceiveWsdl {
    private String namespace_uri = "http://tempuri.org/";

    @Override
    public AddResponse getAdd(int A, int B) {
        Add request = new Add();
        request.setIntA(A);
        request.setIntB(B);
        System.out.println(namespace_uri);
        return (AddResponse)getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback(namespace_uri+"Add"));
    }

    @Override
    public DivideResponse getDivide(int A, int B) {
        Divide request = new Divide();
        request.setIntA(A);
        request.setIntB(B);
        return (DivideResponse)getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback(namespace_uri+"Divide"));
    }

    @Override
    public MultiplyResponse getMultiply(int A, int B) {
        Multiply request = new Multiply();
        request.setIntA(A);
        request.setIntB(B);
        return (MultiplyResponse)getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback(namespace_uri+"Multiply"));
    }

    @Override
    public SubtractResponse getSubtract(int A, int B) {
        Subtract request = new Subtract();
        request.setIntA(A);
        request.setIntB(B);
        return (SubtractResponse)getWebServiceTemplate().marshalSendAndReceive(request, new SoapActionCallback(namespace_uri+"Subtract"));
    }
}
