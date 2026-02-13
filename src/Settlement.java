public class Settlement extends Building {

    public Settlement(Player owner, Node node) { //Settlement method
        this.owner = owner;
        this.location = node;
    }

    @Override
    public int getPoints() { //Returns 1 point
        return 1;
    }

    @Override
    public int getResourceMult() {
        return 1;
    } //Returns 1 resource
}
