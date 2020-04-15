package java3lesson07reflection.mytest;

import hw06java3.util.Calculator;
import java3lesson07reflection.utilannotation.AfterSuite;
import java3lesson07reflection.utilannotation.BeforeSuite;
import java3lesson07reflection.utilannotation.Test;

import java.util.ArrayList;
import java.util.List;


public class CalculatorTest {

    private static Calculator calculator;
    private static List<String> testsResult;

    @BeforeSuite
    public static void init() {
        calculator = new Calculator();
        testsResult = new ArrayList<>();
        System.out.println("Static object initialized");
    }

    @AfterSuite
    public static void printResult() {
        System.out.println("Print result of testing");
        testsResult.forEach(System.out::println);
    }


    @Test(priority = 1)
    public void testAdd() {

        assertEquals(4, calculator.add(2, 2), "testAdd");
    }

    @Test
    private void testSub() {

        assertEquals(3, calculator.sub(5, 2), "testSub");
    }

    @Test
    public void testMul() {

        assertEquals(9, calculator.mul(3, 3), "testMul");
    }

    @Test(priority = 10)
    private void testDiv() {
                assertEquals(1, calculator.div(2, 2), "testDiv");
    }


    private void assertEquals(int expected, int actual, String methodName) {
        String result = (expected == actual) ? "Test: " + methodName + " is successful! " + expected + " = " + actual + " is true" :
                "Test: " + methodName + " failed "+ expected + " not equals " + actual;
        testsResult.add(result);

    }
}

