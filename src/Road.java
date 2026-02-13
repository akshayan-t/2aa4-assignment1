import java.util.ArrayList;

public class Road {
    private Node start;
    private Node end;
    private Player owner;

    public Road(Node start, Node end, Player owner) { //Constructor
        this.start = start;
        this.end = end;
        this.owner = owner;
    }

    public Node getStart() { //Gets start
        return start;
    } //Gets start
    public Node getEnd() {
        return end;
    } //Gets end
    public Player getOwner() {
        return owner;
    } //Gets owner
    public void printRoad() {
        System.out.print("(" + getStart() + "," +  getEnd() + ") ");
    }
}