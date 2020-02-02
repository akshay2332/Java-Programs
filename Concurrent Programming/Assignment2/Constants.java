package Assignment2;

public class Constants {

    /*
    *   Coding as per the comments in assignment description
    *   There are various apparatus types, and for each type, various apparatus.
    *   More precisely, there are 5 of each of the following types of apparatus in the gym
    *   (for example there are 5 Leg Press machines, 5 Bar Bells, and so on)
    */
    public final static int NUMBER_OF_MACHINES_AVAILABLE = 5;

    /*
     *  Coding as per the comments in assignment description
     *  The total set of weight plates in the gym are: 75 of size 10kg, 90 of size 5kg and 110 of size 3kg;
     *  as mentioned, this information is recorded in the noOfWeightPlates variable of the class Gym.
     */
    public final static int PLATES_SMALL_3KG = 110;
    public final static int PLATES_SMALL_5KG = 90;
    public final static int PLATES_SMALL_10KG = 75;

    /*
    *   Coding as per the comments in assignment description
    *   Clients should be assigned unique ids and should have between 15 and 20 ex- ercises in their routines.
    */
    public final static int MAX_NUMBER_EXERCISES = 20;
    public final static int MIN_NUMBER_EXERCISES = 15;

    /*
     *  Coding as per the comments in assignment description
     *  Also, the number of plates for each exercise should be between 0 and 10 per weight size
     */
    public final static int MAX_NUMBER_WEIGHTS = 10;
    public final static int MIN_NUMBER_WEIGHTS = 0;

    // duration is within 1 -10 milliseconds
    /*
     *  Coding as per the comments in assignment description
     *  The duration of each exercise should be anything reasonable
     *  (in the sense that the simulation doesn’t slow down too much)
     * */
    public final static int MIN_DURATION = 1;
    public final static int MAX_DURATION = 10;

    public final static  int MIN_CLIENT_ID = 1;

    public final static int NUMBER_OF_EXERCISES = ApparatusType.values().length;

}
