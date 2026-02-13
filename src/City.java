public class City extends Building {
    public City(Player owner, Node node) {
        this.owner = owner;
        this.location = node;
    }

    @Override
    public int getPoints() {
        return 2;
    } //Returns 2 points

    @Override
    public int getResourceMult() {
        return 2;
    } //Returns 2 resources
}
