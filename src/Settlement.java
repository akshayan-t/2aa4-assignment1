public class Settlement extends Building {

    public Settlement(Player owner) {
        this.owner = owner;
    }

    @Override
    public int getPoints() {
        return 1;
    }

    @Override
    public int getResourceMult() {
        return 1;
    }
}
