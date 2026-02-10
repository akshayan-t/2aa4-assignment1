public class City extends Building {
    public City(Player owner) {
        this.owner = owner;
    }

    @Override
    public int getPoints() {
        return 2;
    }

    @Override
    public int getResourceMult() {
        return 2;
    }
}
