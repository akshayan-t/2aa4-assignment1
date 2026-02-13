public abstract class Building {
    protected Player owner;
    protected Node location;

    public abstract int getPoints(); //Gets points
    public abstract int getResourceMult(); //Gets resource multiplier
    public Player getOwner() { //Gets owner
        return owner;
    } //Gets owner
    public Node getLocation() { //Gets location
        return location;
    }
}
