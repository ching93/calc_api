package com.radn.wsdl_api;

import com.radn.wsdl_api.com.radn.entities.Operation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WsdlApiApplicationTests {
    @Autowired
    private BeanFactory factory;
    @Autowired
    private MainController controller;
    @Value(value = "${scheduler.delay}")
    private int opLimitDelay;
    /***
     * Тестирование правильности выполения операций
     */
    @Test
    public void contextLoads() {
        int maxCount = 100;
        controller.setMaxOperations(maxCount);
        Operation op = null;
        String ops = "+-*/";
        for (int i=0; i < maxCount; i++) {
            int opCode = Math.round((float)Math.random()*3);
            int A = Math.round((float)Math.random()*1000);
            int B = Math.round((float)Math.random()*1000);
            int result = -1;
            switch (opCode) {
                case 0: result = A + B; break;
                case 1: result = A - B; break;
                case 2: result = A * B; break;
                case 3: result = Math.round(((float)A)/((float)B)); break;
            }
            System.out.println(String.format("Correct:%d%s%d=%d",A,ops.charAt(opCode),B,result));
            op = controller.createOperation(ops.charAt(opCode),A,B);
            System.out.println(String.format("Assert:%d%s%d=%d",op.getFirstOperand(),ops.charAt(opCode),op.getSecondOperand(),op.getResult()));
            assert op.getResult() == result;
        }
    }

    /**
     * Проверка на выполнение ограничения числа операций в период
     */
    @Test
    public void limitTest() {
        int maxCount = 250;
        Temporal beginTime = Clock.systemUTC().instant();
        controller.setMaxOperations(maxCount);
        Operation op = null;
        String ops = "+-*/";
        int i;
        for (i=0; i < maxCount+10; i++) {
            int opCode = Math.round((float)Math.random()*3);
            op = controller.createOperation(ops.charAt(opCode),i,2*i-i);
            if (op == null) {
                break;
            }
        }
        Duration d = Duration.between(beginTime,Clock.systemUTC().instant());
        System.out.println(String.format("operations made: %d, elapsed %ds",i,d.getSeconds()));
        System.out.println(String.format("elapsed: %d:%d, interval:%d",d.toMinutes(),d.getSeconds(),opLimitDelay));
        if (op == null)
            assert d.getSeconds()*60 <= opLimitDelay;
        else
            assert d.getSeconds()*60 >= opLimitDelay;
    }

    /**
     * Разбор выражений
     */
    @Test
    public void calculateExpression() {
        String[] expressions = new String[] {
                "25/4*34/22*34+6=312",
                "25*66-34*43*34-6=-48064",
                "25*665667+34/43*34+6=16641715",
                "25/44+40/43*34+6=41",
                "2048/2/2/2/2/2/2/2=16"
        };
        for (String expression: expressions) {
            String[] line = expression.split("\\=",2);
            String correct = line[1];
            String leftPart = line[0];
            MainController.ReplaceResult result = controller.expression(leftPart);
            System.out.println(expression+" -- "+result.expression);
            assert result.expression.equals(correct);
        }
    }
}
