package java3lesson07reflection;

import java3lesson07reflection.mytest.CalculatorTest;
import java3lesson07reflection.mytest.HwMethodsMyTest;

public class Main {

    public static void main(String[] args) {
        TestClassesRunner.start(CalculatorTest.class);
        TestClassesRunner.start(HwMethodsMyTest.class);
    }
}
