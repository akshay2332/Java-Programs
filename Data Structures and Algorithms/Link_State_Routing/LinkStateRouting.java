import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class LinkStateRouting {

    private static final String FILE_NAME = "infile.dat";
    private int routerCount;
    private Map<String, Router> allRouterInfoList;
    private Map<String, Integer> mappingOfAllRouter;


    private static final String NETWORK_PATTERN =
            "\\d\\s\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";


    private static final String ROUTER_COST_PATTERN =
            "\\d|\\d\\s\\d{1,3}";


    private LinkStateRouting() {

        this.allRouterInfoList = new HashMap<String, Router>();
        this.mappingOfAllRouter = new HashMap<String, Integer>();
    }

    public static void main(String args[]) {

        LinkStateRouting linkStateRouting = new LinkStateRouting();
        linkStateRouting.readFile();
        System.out.println("File infile.dat has been read and routers have been initialized");

        String userInput;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("If you want to continue Enter: C");
            System.out.println("If you want to quit Enter: Q");
            System.out.println("If you want to print the routing table of a router, example for router 1 Enter: P 1");
            System.out.println("If you want to shut down a router,example for router 1 Enter: S 1");
            System.out.println("If you want to start up a router,example for router 1 Enter: T 1");
            userInput = scanner.nextLine();

            int userInputLength = userInput.length();
            if (userInputLength == 1) {

                if ("C".equalsIgnoreCase(userInput)) {
                    linkStateRouting.continuefxn();
                } else if ("Q".equalsIgnoreCase(userInput)) {
                    break;
                } else {
                    System.out.println("Please input the right format!");
                }
            } else if (userInputLength == 3) {
                String[] userInputSplit = userInput.split(" ");
                if ("P".equalsIgnoreCase(userInputSplit[0])) {
                    linkStateRouting.printRouterTable(userInputSplit[1]);
                } else if ("S".equalsIgnoreCase(userInputSplit[0])) {
                    linkStateRouting.shutdownRouter(userInputSplit[1]); //To stop the router
                } else if ("T".equalsIgnoreCase(userInputSplit[0])) {
                    linkStateRouting.start(userInputSplit[1]); //To start the router
                } else {
                    System.out.println("please input the right format!\n");
                }
            } else {
                System.out.println("Please input the right format!");
            }

        }
        while (!"Q".equalsIgnoreCase(userInput));

    }

    private void readFile() {
        File file = new File(FILE_NAME);
        Scanner in;

        try {

            in = new Scanner(file);
            String currentLine;
            String[] routerInfo;
            Router currentRouter = null;

            while (in.hasNext()) {
                currentLine = in.nextLine();
                currentLine = currentLine.trim();

                if (Pattern.compile(NETWORK_PATTERN).matcher(currentLine).find()) {
                    routerInfo = currentLine.split(" ");
                    currentRouter = allRouterInfoList.get(routerInfo[0]);

                    if (currentRouter == null) {
                        currentRouter = new Router(routerInfo[0], routerInfo[1]);
                        this.allRouterInfoList.put(routerInfo[0], currentRouter);
                        this.mappingOfAllRouter.put(currentRouter.getId(), currentRouter.getWorkId());
                    } else {
                        currentRouter.setNetwork(routerInfo[1]);
                        this.allRouterInfoList.put(routerInfo[0], currentRouter);
                    }
                    this.routerCount++;
                } else if (Pattern.compile(ROUTER_COST_PATTERN).matcher(currentLine).find()) {
                    String linkInfo[];
                    linkInfo = currentLine.split(" ");

                    Router neighbourRouter = this.allRouterInfoList.get(linkInfo[0]);

                    if (neighbourRouter == null) {
                        neighbourRouter = new Router(linkInfo[0], null);
                        this.allRouterInfoList.put(linkInfo[0], neighbourRouter);

                        this.mappingOfAllRouter.put(neighbourRouter.getId(), neighbourRouter.getWorkId());
                    }


                    if (linkInfo.length == 1) {
                        currentRouter.addNeighbor(neighbourRouter, 1);
                    } else {
                        currentRouter.addNeighbor(neighbourRouter, Integer.parseInt(linkInfo[1]));
                    }

                    this.allRouterInfoList.put(currentRouter.getId(), currentRouter);
                }
            }

            for (Map.Entry<String, Router> routers : this.allRouterInfoList.entrySet()) {
                routers.getValue().getMapping().putAll(this.mappingOfAllRouter);
                routers.getValue().createGraph(routerCount);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void continuefxn() {
        System.out.println("Originate Packet called on all routers");
        for (Map.Entry routers : this.allRouterInfoList.entrySet()) {
            ((Router) routers.getValue()).originatePacket();
        }
    }

    private void printRouterTable(String routerId) {
        allRouterInfoList.get(routerId).printTable();
    }

    private void shutdownRouter(String routerId) {
        allRouterInfoList.get(routerId).shutdown();
    }


    private void start(String routerId) {
        allRouterInfoList.get(routerId).startRouter();
    }

}


class Router {

    private static int idNumber = 0;

    private boolean active;
    private String id;
    private int workId;

    private String network;
    private List<LSP> packets;
    private int[][] graph;

    private Map<Integer, Object[]> neighbourRouters;

    private List<String[]> routerTable;
    private Map<String, Integer> mapping;
    private int[] tickCounter;

    public Map<String, Integer> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, Integer> mapping) {
        this.mapping = mapping;
    }

    Router(String routerId, String networkName) {
        this.active = true;
        this.id = routerId;
        this.workId = Router.idNumber;
        Router.idNumber++;
        this.network = networkName;
        this.packets = new ArrayList<>();
        this.neighbourRouters = new HashMap<Integer, Object[]>();
        this.routerTable = new ArrayList<>();
        mapping = new HashMap<>();
        mapping.put(id, workId);
    }

    public void addNeighbor(Router router, int cost) {
        Object[] neighborInfo = new Object[2];
        neighborInfo[0] = router;
        neighborInfo[1] = cost;

        // mapping for routerId to cost
        mapping.put(router.getId(), router.getWorkId());
        neighbourRouters.put(router.getWorkId(), neighborInfo);
    }


    public void createGraph(int routerCount) {
        graph = new int[routerCount][routerCount];
        graph[this.workId][this.workId] = 0;


        // tick initialisation
        this.tickCounter = new int[routerCount];

        for (int i = 0; i < routerCount; i++) {
            for (int j = 0; j < routerCount; j++) {
                graph[i][j] = -1;
            }
        }

        for (int tickPos = 0; tickPos < routerCount; tickPos++) {
            this.tickCounter[tickPos] = 99;
        }

        for (Map.Entry<Integer, Object[]> entry : neighbourRouters.entrySet()) {
            this.tickCounter[entry.getKey()] = 0;
            Object[] info = entry.getValue();
            graph[this.workId][entry.getKey()] = (int) info[1];
            graph[entry.getKey()][this.workId] = (int) info[1];
        }

        this.createRouterTable(routerCount);
    }


    private void createRouterTable(int routerCount) {
        this.routerTable.clear();

        for (int i = 0; i < routerCount; i++) {
            int cost = this.graph[this.workId][i];
            if (cost != -1 && cost != 0) {
                Object[] neighbourInfo = neighbourRouters.get(i);
                Router neighbourRouter = (Router) neighbourInfo[0];
                this.routerTable.add(new String[]{neighbourRouter.getNetwork(), neighbourRouter.getId(), String.valueOf(cost)});
            }
        }
    }


    public int[][] getGraph() {
        return graph;
    }

    public void setGraph(int[][] graph) {
        this.graph = graph;
    }

    public Map<Integer, Object[]> getNeighbourRouters() {
        return neighbourRouters;
    }

    public void setNeighbourRouters(Map<Integer, Object[]> neighbourRouters) {
        this.neighbourRouters = neighbourRouters;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public static int getIdNumber() {
        return idNumber;
    }


    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }




    public void printTable() {
        System.out.print(String.format("%-15s", "NETWORK"));
        System.out.print(String.format("%-15s", "OUTGOING LINK"));
        System.out.print(String.format("%-15s", "COST"));
        System.out.println();
        for (String[] routerRow : this.routerTable) {
            // need to be edited

            System.out.print(String.format("%-15s", routerRow[0]));
            System.out.print(String.format("%-15s", routerRow[1]));
            System.out.print(String.format("%-15s", routerRow[2]));
            System.out.println();
        }
    }

    public void shutdown() {
        this.setActive(false);
        System.out.println("router " + this.network + " shutdown");
    }

    public void startRouter() {
        this.setActive(true);
        System.out.println("router " + this.network + " started");
    }


    public void originatePacket() {
        if (!this.active)
            return;
        System.out.println("packet originated by " + this.network);

        // initialisation and formula in the function

        this.checktick();
        LSP lspObj = this.generateLsp();

        this.packets.add(lspObj);

        for (Map.Entry<Integer, Object[]> entry : this.neighbourRouters.entrySet()) {
            Router neighborRouter = (Router) entry.getValue()[0];
            neighborRouter.receivePacket(lspObj, this.id);
        }
    }

    private void receivePacket(LSP packet, String origin) {
        if (!this.active) {
        } else {
            if (packet.getTtl() < 1 || !this.isValidPacket(packet)) {

            } else {
                packet.setTtl((packet.getTtl() - 1));
                this.packets.add(packet);
                this.compare(packet);
                this.performDijkstra(graph.length);

                for (Map.Entry<Integer, Object[]> routerEntry : this.neighbourRouters.entrySet()) {
                    Router neighborRouter = (Router) routerEntry.getValue()[0];
                    if (!origin.equalsIgnoreCase(neighborRouter.getId())) {
                        neighborRouter.receivePacket(packet, this.id);
                    }
                }

            }
        }
    }


    private boolean isValidPacket(LSP packet) {
        for (LSP lsp : this.packets) {
            if (lsp.getOrigin().equalsIgnoreCase(packet.getOrigin()) && lsp.getSeq() >= packet.getSeq()) {
                return false;
            }
        }
        return true;
    }

    private void compare(LSP packet) {
        System.out.println("router Id" + this.id + "packet origin" + packet.getOrigin());

        System.out.println("mapping" + mapping.toString());

        int workId = mapping.get(packet.getOrigin());
        int i = 0;
        List<Integer> costList = packet.getPacketInfo().getCost();
        for (Router router : packet.getPacketInfo().getRouters()) {
            int cost = costList.get(i);
            this.graph[workId][router.getWorkId()] = cost;
            this.graph[router.getWorkId()][workId] = cost;
            i++;
        }
    }

    private Object[] performDijkstra(int routerCount) {
        int dist[] = new int[routerCount];
        //      .fill(Infinity);
        boolean[] sptSet = new boolean[routerCount];
        //(routerCount).fill(false);
        int[] parent = new int[routerCount];
        //).fill(-1);

        int[] prev = new int[routerCount];
        Object[] path = new Object[routerCount];

        for (int i = 0; i < routerCount; i++) {
            dist[i] = 9999999;
            sptSet[i] = false;
            parent[i] = -2;

            path[i] = new Object[routerCount];
        }
        dist[this.workId] = 0;

        for (int count = 0; count < routerCount; count++) {
            int minimumIndex = this.minimumDistance(dist, sptSet);
            sptSet[minimumIndex] = true;
            for (int v = 0; v < routerCount; v++) {
                if (!sptSet[v] && dist[minimumIndex] != 9999999 && ((dist[minimumIndex] + graph[minimumIndex][v]) < dist[v])) {
                    System.out.println("Entered here");
                    parent[v] = minimumIndex;
                    graph[minimumIndex][v] = dist[minimumIndex] + graph[minimumIndex][v];
                }
            }
        }

        this.createRouterTable(routerCount);



        Object[] objects = new Object[2];
        objects[0] = dist;
        objects[1] = prev;

        return objects;
    }

    private int minimumDistance(int distance[], boolean[] sptSet) {
        int minDistance = 999999999;
        int minIndex = 0;

        for (int i = 0; i < distance.length; i++) {
            if (!sptSet[i] && distance[i] <= minDistance) {
                minDistance = distance[i];
                minIndex = i;
            }
        }
        return minIndex;

    }



    private LSP generateLsp() {
        LSP packet = new LSP(this.id);

        for (Map.Entry<Integer, Object[]> entry : this.neighbourRouters.entrySet()) {
            Router neighborRouter = (Router) entry.getValue()[0];
            int cost = (int) entry.getValue()[1];
            packet.getPacketInfo().getRouters().add(neighborRouter);
            packet.getPacketInfo().getNetworks().add(neighborRouter.getNetwork());
            packet.getPacketInfo().getCost().add(cost);
        }
        return packet;
    }

    private void checktick() {
        for (int i = 0; i < this.tickCounter.length; i++) {
            this.tickCounter[i]++;
            if (this.tickCounter[i] == 2) {
                this.tickCounter[i] = 0;
                this.graph[this.workId][i] = -1;
            }
        }
    }
}


class Packet {

    private List<Router> routers;
    private List<String> networks;
    private List<Integer> cost;


    public Packet() {
        this.routers = new ArrayList<>();
        this.networks = new ArrayList<>();
        this.cost = new ArrayList<>();
    }


    public List<Router> getRouters() {
        return routers;
    }

    public void setRouters(List<Router> routers) {
        this.routers = routers;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }

    public List<Integer> getCost() {
        return cost;
    }

    public void setCost(List<Integer> cost) {
        this.cost = cost;
    }
}


class LSP {

    private int ttl = 10;
    private int seq = 1;
    private String origin;

    private Packet packetInfo;


    public LSP(String id) {
        this.packetInfo = new Packet();
        this.origin = id;
    }


    public int getTtl() {
        return ttl;
    }

    public int getSeq() {
        return seq;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Packet getPacketInfo() {
        return packetInfo;
    }

    public void setPacketInfo(Packet packetInfo) {
        this.packetInfo = packetInfo;
    }
}
