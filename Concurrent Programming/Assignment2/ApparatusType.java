package Assignment2;

public enum ApparatusType {

    LEGPRESSMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    BARBELL(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    HACKSQUATMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    LEGEXTENSIONMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    LEGCURLMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    LATPULLDOWNMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    PECDECKMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE),
    CABLECROSSOVERMACHINE(Constants.NUMBER_OF_MACHINES_AVAILABLE);

    private int machineAvailable;

    ApparatusType(int machineAvailable) {
        this.machineAvailable = machineAvailable;
    }

    public int getMachineAvailable() {
        return this.machineAvailable;
    }

    public void decrementMachineAvailable() {
        this.machineAvailable--;
    }
}
