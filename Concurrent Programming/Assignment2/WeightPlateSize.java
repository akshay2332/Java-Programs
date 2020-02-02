package Assignment2;

public enum WeightPlateSize {

    SMALL_3KG(Constants.PLATES_SMALL_3KG), MEDIUM_5KG(Constants.PLATES_SMALL_5KG), LARGE_10KG(Constants.PLATES_SMALL_10KG);

    private int numberOfPlates;

    WeightPlateSize(int numberOfPlates) {
        this.numberOfPlates = numberOfPlates;
    }

    public int getNumberOfPlates() {
        return numberOfPlates;
    }

    public void decrementPlates(int quantity) {
        this.numberOfPlates = this.numberOfPlates - quantity;
    }

    public void incrementPlates(int quantity) {

        this.numberOfPlates = this.numberOfPlates + quantity;
    }

}
