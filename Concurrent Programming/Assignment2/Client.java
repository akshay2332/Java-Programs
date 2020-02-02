package Assignment2;

import java.util.*;

public class Client {

    private int id;
    private List<Exercise> routine;
    private Random random;
    ;

    private Client(int id) {
        this.id = id;
        this.routine = new LinkedList<Exercise>();
        random = new Random();
    }

    public void addExercise(Exercise e) {
        this.routine.add(e);

    }

    public static Client generateRandom(int id) {

        Client client = new Client(id);

        int exercisePerformed = client.random.nextInt(Constants.MAX_NUMBER_EXERCISES - Constants.MIN_NUMBER_EXERCISES)
                + Constants.MIN_NUMBER_EXERCISES;

        List<ApparatusType> apparatusTypes = new ArrayList<ApparatusType>();
        ApparatusType[] allValues = ApparatusType.values();

        while (apparatusTypes.size() != exercisePerformed) {
            apparatusTypes.add(allValues[client.random.nextInt(Constants.NUMBER_OF_EXERCISES)]);
        }

        for (ApparatusType apparatusType : apparatusTypes) {
            Map<WeightPlateSize, Integer> weights = new HashMap<WeightPlateSize, Integer>();


            for (WeightPlateSize plateSize : WeightPlateSize.values()) {

                int numberOfPlates = client.random.nextInt(Constants.MAX_NUMBER_WEIGHTS - Constants.MIN_NUMBER_WEIGHTS)
                        + Constants.MIN_NUMBER_WEIGHTS;

                weights.put(plateSize, numberOfPlates);
            }

            client.addExercise(Exercise.generateRandom(apparatusType, weights));
        }

        return client;
    }

    public int getId() {
        return id;
    }

    public List<Exercise> getRoutine() {
        return routine;
    }

    public void printClientCurrentExercise(Exercise exercise) {
        System.out.println("Start Exercise | clientId | " + this.id + " | Apparatus | " + exercise.getAt().name() +
                " | Weights | " + WeightPlateSize.SMALL_3KG + " | " + exercise.getWeight().get(WeightPlateSize.SMALL_3KG)
                + " | " + WeightPlateSize.MEDIUM_5KG + " | " + exercise.getWeight().get(WeightPlateSize.MEDIUM_5KG)
                + " | " + WeightPlateSize.LARGE_10KG + " | " + exercise.getWeight().get(WeightPlateSize.LARGE_10KG));
    }


    public void printClientEndExercise(Exercise exercise) {
        System.out.println("Finish Exercise | clientId | " + this.id + " | Apparatus | " + exercise.getAt().name() +
                " | Weights | " + WeightPlateSize.SMALL_3KG + " | " + exercise.getWeight().get(WeightPlateSize.SMALL_3KG)
                + " | " + WeightPlateSize.MEDIUM_5KG + " | " + exercise.getWeight().get(WeightPlateSize.MEDIUM_5KG)
                + " | " + WeightPlateSize.LARGE_10KG + " | " + exercise.getWeight().get(WeightPlateSize.LARGE_10KG)
                + " | Duration | " + exercise.getDuration() + "ms");
    }

}
