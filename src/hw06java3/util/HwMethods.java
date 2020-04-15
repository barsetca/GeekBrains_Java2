package hw06java3.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HwMethods {

    public int[] getAfterFourArray(int[] ints) {
        List<Integer> result = new ArrayList<>();
        boolean noFour = true;

        for (int i = ints.length - 1; i >= 0; i--) {
            int element = ints[i];
            if (element != 4) {
                result.add(0, element);
            } else {
                noFour = false;
                break;
            }
        }
        if (noFour) {
            throw new RuntimeException("В массиве нет 4");
        }
        if (result.isEmpty()) {
            return null;
        }
        int[] arrayAfterFour = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arrayAfterFour[i] = result.get(i);
        }
        return arrayAfterFour;
    }

    public boolean checkHaveOneOrFour(int[] ints) {
        List<Integer> sourceList = Arrays.stream(ints).boxed().collect(Collectors.toList());
        return sourceList.contains(4) && sourceList.contains(1);
    }
}

