public abstract class Building {
    protected Player owner;

    public abstract int getPoints();
    public abstract int getResourceMult();
    public Player getOwner() {
        return owner;
    }
}
