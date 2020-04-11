package hw06java3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HwMethodsTest {
    private HwMethods hwMethods;

    private int[] element;
    private int[] result1;
    private boolean result2;
    private Class<? extends Exception> exception;

    public HwMethodsTest(int[] element, int[] result1, boolean result2, Class<? extends Exception> exception) {

        this.element = element;
        this.result1 = result1;
        this.result2 = result2;
        this.exception = exception;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new int[]{1, 4, 1, 1}, new int[]{1, 1}, true, null},
                {new int[]{1, 4, 4}, null, true, null},
                {new int[]{4, 4, 4}, null, false, null},
                {new int[]{1, 1, 1}, null, false, RuntimeException.class},
        });
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        hwMethods = new HwMethods();
    }

    @Test
    public void getAfterFourArrayTest() {
        if (exception != null) {
            expectedException.expect(RuntimeException.class);
            expectedException.expectMessage("В массиве нет 4");
        }

        Assert.assertArrayEquals(result1, hwMethods.getAfterFourArray(element));
    }

    @Test
    public void checkHaveOneOrFour() {
        Assert.assertEquals(result2, hwMethods.checkHaveOneOrFour(element));
    }
}