package lesson01;

import lesson01.barrieres.Barrier;
import lesson01.barrieres.RunningTrack;
import lesson01.barrieres.Wall;
import lesson01.competitors.Cat;
import lesson01.competitors.Man;
import lesson01.competitors.Movable;
import lesson01.competitors.Robot;

public class CompetitionTest {

    public static void main(String[] args) {
        Movable[] movables = {
                new Cat("Cat1", 50, 2),
                new Man("Man1", 5000, 4),
                new Robot("Robot1", 200, 1),
                new Cat("Cat2", 150, 3),
                new Man("Man2", 50, 1),
                new Robot("Robot2", 10, 10),
                new Cat("Cat3", 500, 5),
                new Man("Man3", 1000, 2),
                new Robot("Robot3", 2000, 3)

        };

        Barrier[] barriers = {
                new RunningTrack(10), new Wall(1),
                new RunningTrack(100), new Wall(2),
                new RunningTrack(1000), new Wall(3)
        };

        test(movables, barriers);
    }

    private static void test(Movable[] movables, Barrier[] barriers) {
        for (Movable movable : movables) {
            boolean check = false;
            for (Barrier barrier : barriers) {
                check = barrier.isOvercomeBarrier(movable);
                if (!check) {
                    break;
                }
            }
            if (check) {
                System.out.println("Участник " + movable + " прошёл все препятствия!");
                System.out.println();

            }

        }
    }
}
