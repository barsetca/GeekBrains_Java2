package lesson02.util;

import lesson02.exceptions.MyArrayDataException;
import lesson02.exceptions.MyArraySizeException;

public class SumUtil {

    public static final int ARRAY_ROWS = 4;
    public static final int ARRAY_COLUMN = 4;

    public static int summarizeArraysElements(String[][] integers) throws MyArraySizeException, MyArrayDataException {
        if (integers == null || integers.length != ARRAY_ROWS || integers[0].length != ARRAY_COLUMN) {
            throw new MyArraySizeException(String.format("Ошибка! Передаваемый в метод массив должен быть String [%d][%d]",
                    ARRAY_ROWS, ARRAY_COLUMN ));
        }
        int sum = 0;
        for (int i = 0; i < integers.length; i++) {
            for (int j = 0; j < integers[i].length; j++) {
                try {
                    sum = sum + Integer.parseInt(integers[i][j]);
                } catch (NumberFormatException | NullPointerException e) {
                    throw new MyArrayDataException(String.format("Элемент массива String[%d][%d] - не является целым числом!",
                            i, j));
                }
            }
        }
        return sum;
    }
}
