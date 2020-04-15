package java3lesson07reflection.mytest;

import hw06java3.util.HwMethods;
import java3lesson07reflection.utilannotation.AfterSuite;
import java3lesson07reflection.utilannotation.BeforeSuite;
import java3lesson07reflection.utilannotation.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HwMethodsMyTest {
    private static HwMethods hwMethods;
    private static List<String> testsResult;
    private static List<Object[]> testData;

    @BeforeSuite
    public static void init() {
        hwMethods = new HwMethods();
        testsResult = new ArrayList<>();

        testData = Arrays.asList(new Object[][]{
                {new int[]{1, 1}, new int[]{1, 4, 1, 1}, true},
                {null, new int[]{1, 4, 4}, true},
                {null, new int[]{4, 4, 4}, false},
                {null, new int[]{1, 1, 1}, false}
        });
    }

    @AfterSuite
    private static void printResult() {
        System.out.println("Print result of testing");
        testsResult.forEach(System.out::println);
    }

    @Test(priority = 1)
    public void getAfterFourArrayTest() {
        for (int i = 0; i < testData.size() - 1; i++) {
            assertArrayEquals((int[]) testData.get(i)[0], hwMethods.getAfterFourArray((int[]) testData.get(i)[1]));
        }
    }

    @Test(priority = 2)
    private void getAfterFourArrayExceptionTest() {
        int[] exceptionArray = (int[]) testData.get(3)[1];
        try {
            Object object = hwMethods.getAfterFourArray(exceptionArray);
            testsResult.add("Test: getAfterFourArrayExceptionTest(" + Arrays.toString(exceptionArray) +
                    ") failed - expected: RuntimeException but actual:" +
                    object);
        } catch (RuntimeException e) {
            testsResult.add("Test: getAfterFourArrayExceptionTest(" + Arrays.toString(exceptionArray) +
                    ") is successful! expected: RuntimeException = actual: " + e.getClass());
        }
    }

    @Test(priority = 3)
    public void checkHaveOneOrFour() {
        for (Object[] testDatum : testData) {
            int[] checkingArray = (int[]) testDatum[1];
            assertEquals((boolean) testDatum[2], hwMethods.checkHaveOneOrFour(checkingArray), checkingArray);
        }
    }

    private static void assertArrayEquals(int[] expected, int[] actual) {
        String result = Arrays.equals(expected, actual) ? "Test: checkHaveOneOrFour() is successful! "
                + Arrays.toString(expected) + " = " + Arrays.toString(actual) + " is true" :
                "Test: checkHaveOneOrFour() failed " + Arrays.toString(expected) + " not equals " + Arrays.toString(actual);
        testsResult.add(result);
    }

    private void assertEquals(boolean expected, boolean actual, int[] checkingArray) {
        String result = (expected == actual) ? "Test: checkHaveOneOrFour(" + Arrays.toString(checkingArray) +
                ") is successful! " + expected + " = " + actual + " is true" :
                "Test: checkHaveOneOrFour(" + Arrays.toString(checkingArray) + ") failed " + expected + " not equals " + actual;
        testsResult.add(result);
    }
}