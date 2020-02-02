package Assignment2;

import java.util.Map;
import java.util.Random;

public class Exercise {

    private ApparatusType at;
    private Map<WeightPlateSize, Integer> weight;
    private int duration;


    public Exercise(ApparatusType at, Map<WeightPlateSize, Integer> weight, int duration) {
        this.at = at;
        this.weight = weight;
        this.duration = duration;
    }


    public static Exercise generateRandom(ApparatusType apparatusType, Map<WeightPlateSize, Integer> weights) {
        Random random = new Random();


        int durationOfExercise = random.nextInt(Constants.MAX_DURATION - Constants.MIN_DURATION)
                + Constants.MIN_DURATION;



        return new Exercise(apparatusType, weights, durationOfExercise);
    }

    public ApparatusType getAt() {
        return at;
    }

    public int getDuration() {
        return duration;
    }

    public Map<WeightPlateSize, Integer> getWeight() {
        return weight;
    }
}
