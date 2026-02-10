import java.util.ArrayList;

public class Road {
    private int start;
    private int end;
    private Player owner;

    public Road(int start, int end, Player owner) {
        this.start = start;
        this.end = end;
        this.owner = owner;
    }

    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }
    public Player getOwner() {
        return owner;
    }
    public void print() {
        System.out.println(start + ", " + end);
    }
}