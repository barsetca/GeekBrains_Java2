package lesson01.competitors;

public class Robot implements Movable {

    private final String name;
    private final int runLength;
    private final int jumpHeight;

    public Robot(String name, int runLength, int jumpHeight) {
        this.name = name;
        this.runLength = runLength;
        this.jumpHeight = jumpHeight;
    }

    @Override
    public int run() {
        System.out.println("Участник " + this + " бежит!");
        return runLength;

    }

    @Override
    public int jump() {
        System.out.println("Участник " + this + " прыгает!");
        return jumpHeight;
    }

    @Override
    public String toString() {
        return "Robot model: " + name;
    }

}
