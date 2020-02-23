package lesson01.barrieres;

import lesson01.competitors.Movable;

public class RunningTrack implements Barrier {

    private final int trackLength;

    public RunningTrack(int trackLength) {
        this.trackLength = trackLength;
    }

    @Override

    public boolean isOvercomeBarrier(Movable object) {
        if (trackLength > object.run()) {
            System.out.println(object + " не смог пробежать дистанцию длиной " + trackLength + System.lineSeparator());
            return false;
        }
        System.out.println(object + " пробежал дистанцию длиной " + trackLength);
        return true;
    }
}

