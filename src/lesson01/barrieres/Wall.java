package lesson01.barrieres;

import lesson01.competitors.Movable;

public class Wall implements Barrier {

    private final int wallHeight;

    public Wall(int wallHeight) {
        this.wallHeight = wallHeight;
    }

    @Override
    public boolean isOvercomeBarrier(Movable object) {
        if (wallHeight > object.jump()) {
            System.out.println(object.toString() + " не смог перепрыгнуть через стену высотой " + wallHeight + System.lineSeparator());
        return false;
        }
        System.out.println(object.toString() + " перепрыгнул через стену высотой " + wallHeight);
        return true;
    }
}
