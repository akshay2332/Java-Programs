package Assignment2;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Gym implements Runnable {

    private static final int GYM_SIZE = 30;
    private static final int GYM_REGISTERED_CLIENTS = 10000;

    private Map<WeightPlateSize, Integer> noOfWeightPlates;
    private Set<Integer> clients;// for generating fresh client ids
    private ExecutorService executor;

    //private Map<Integer, Client> clientIdMapping;

    private Map<ApparatusType, Semaphore> apparatusTypeSemaphores;
    private Map<WeightPlateSize, Semaphore> weightPlateSizeSemaphores;

    private Semaphore updateApparatusMachineCount;

    private Random random = new Random();


    Gym() {

        this.clients = new HashSet<Integer>();
        this.executor = Executors.newFixedThreadPool(this.GYM_SIZE);

        this.apparatusTypeSemaphores = new HashMap<ApparatusType, Semaphore>();

        this.apparatusTypeSemaphores.put(ApparatusType.BARBELL, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.CABLECROSSOVERMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.HACKSQUATMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.LATPULLDOWNMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.LEGCURLMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.LEGEXTENSIONMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.LEGPRESSMACHINE, new Semaphore(0));
        this.apparatusTypeSemaphores.put(ApparatusType.PECDECKMACHINE, new Semaphore(0));

        this.weightPlateSizeSemaphores = new HashMap<WeightPlateSize, Semaphore>();

        this.weightPlateSizeSemaphores.put(WeightPlateSize.SMALL_3KG, new Semaphore(0));
        this.weightPlateSizeSemaphores.put(WeightPlateSize.MEDIUM_5KG, new Semaphore(0));
        this.weightPlateSizeSemaphores.put(WeightPlateSize.LARGE_10KG, new Semaphore(0));

        //this.clientIdMapping = new HashMap<Integer, Client>();

        /*for (int id = 1; id <= Gym.GYM_REGISTERED_CLIENTS; id++) {
            this.clients.add(id);
            this.clientIdMapping.put(id, Client.generateRandom(id));
        }
         */

        this.updateApparatusMachineCount = new Semaphore(1);
    }

    public void run() {

        while (this.clients.size() != Gym.GYM_REGISTERED_CLIENTS) {


            final int clientId = (random.nextInt(Gym.GYM_REGISTERED_CLIENTS) + Constants.MIN_CLIENT_ID);

            if (this.clients.contains(clientId)) {
                continue;
            }

            this.clients.add(clientId);

            final Client currentClient = Client.generateRandom(clientId);

            this.executor.execute(new Runnable() {
                public void run() {

                    List<Exercise> exercisesToPerform = currentClient.getRoutine();

                    for (Exercise exercise : exercisesToPerform) {
                        int smallPlates = 0;
                        int mediumPlates = 0;
                        int largePlates = 0;
                        ApparatusType apparatusType = null;
                        try {

                            Map<WeightPlateSize, Integer> weights = exercise.getWeight();
                            apparatusType = exercise.getAt();

                            if (!(apparatusType.getMachineAvailable() > 0)) {
                                System.out.println("ClientId | " + clientId + " | Waiting for Apparatus | " + apparatusType.name());

                                apparatusTypeSemaphores.get(apparatusType).acquire();
                            }

                            while (!(weights.get(WeightPlateSize.SMALL_3KG)
                                    <= WeightPlateSize.SMALL_3KG.getNumberOfPlates())) {
                                weightPlateSizeSemaphores.get(WeightPlateSize.SMALL_3KG).acquire();
                            }

                            while (!(weights.get(WeightPlateSize.MEDIUM_5KG)
                                    <= WeightPlateSize.MEDIUM_5KG.getNumberOfPlates())) {
                                weightPlateSizeSemaphores.get(WeightPlateSize.MEDIUM_5KG).acquire();
                            }

                            while (!(weights.get(WeightPlateSize.LARGE_10KG)
                                    <= WeightPlateSize.LARGE_10KG.getNumberOfPlates())) {
                                weightPlateSizeSemaphores.get(WeightPlateSize.LARGE_10KG).acquire();
                            }

                            updateApparatusMachineCount.acquire();

                            smallPlates = weights.get(WeightPlateSize.SMALL_3KG);
                            mediumPlates = weights.get(WeightPlateSize.MEDIUM_5KG);
                            largePlates = weights.get(WeightPlateSize.LARGE_10KG);

                            WeightPlateSize.SMALL_3KG.decrementPlates(smallPlates);
                            WeightPlateSize.MEDIUM_5KG.decrementPlates(mediumPlates);
                            WeightPlateSize.LARGE_10KG.decrementPlates(largePlates);

                            apparatusType.decrementMachineAvailable();

                            currentClient.printClientCurrentExercise(exercise);
                            updateApparatusMachineCount.release();

                            Thread.sleep(exercise.getDuration());
                            currentClient.printClientEndExercise(exercise);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            updateApparatusMachineCount.acquire();

                            WeightPlateSize.SMALL_3KG.incrementPlates(smallPlates);
                            weightPlateSizeSemaphores.get(WeightPlateSize.SMALL_3KG).release();

                            WeightPlateSize.MEDIUM_5KG.incrementPlates(mediumPlates);
                            weightPlateSizeSemaphores.get(WeightPlateSize.MEDIUM_5KG).release();

                            WeightPlateSize.LARGE_10KG.incrementPlates(largePlates);
                            weightPlateSizeSemaphores.get(WeightPlateSize.LARGE_10KG).release();

                            updateApparatusMachineCount.release();

                            apparatusTypeSemaphores.get(apparatusType).release();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }

                }
            });

        }
        this.executor.shutdown();
    }

}
