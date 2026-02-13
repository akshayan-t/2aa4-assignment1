import java.util.ArrayList;

public class Road { //Road class
    private Node start; //Start and end nodes
    private Node end;
    private Player owner; //Road owner

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
    public void printRoad() { //Print method
        System.out.print("(" + getStart() + "," +  getEnd() + ") ");
    }
}