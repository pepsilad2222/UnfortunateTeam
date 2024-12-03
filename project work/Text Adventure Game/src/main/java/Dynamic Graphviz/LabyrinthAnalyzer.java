import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.file.*;

public class LabyrinthAnalyzer {
    private static Map<Integer, Set<Integer>> roomConnections = new HashMap<>();
    private static Map<Integer, String> roomTypes = new HashMap<>();
    private static Map<Integer, List<String>> roomEvents = new HashMap<>();
    
    public static void main(String[] args) {
        // Get current directory and list all .java files
        try {
            Path currentDir = Paths.get(".");
            System.out.println("Looking for game file in: " + currentDir.toAbsolutePath());
            
            // Try to find the game file
            File gameFile = null;
            File[] files = currentDir.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals("ShadowLabyrinth.java")) {
                        gameFile = file;
                        break;
                    }
                }
            }
            
            if (gameFile == null) {
                System.out.println("Could not find ShadowLabyrinth.java");
                System.out.println("Available files in directory:");
                if (files != null) {
                    for (File file : files) {
                        System.out.println("- " + file.getName());
                    }
                }
                return;
            }

            System.out.println("Found game file: " + gameFile.getAbsolutePath());
            analyzeGameCode(gameFile.getAbsolutePath());
            generateDotFile("labyrinth_map.dot");
            printEventAnalysis();
            
        } catch (Exception e) {
            System.out.println("Error processing files:");
            e.printStackTrace();
        }
    }
    
    private static void analyzeGameCode(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int currentRoom = -1;
            Pattern roomPattern = Pattern.compile("currentRoom\\s*=\\s*(\\d+)");
            Pattern roomModPattern = Pattern.compile("currentRoom\\s*[+-]=\\s*(\\d+)");
            
            while ((line = reader.readLine()) != null) {
                // Track room assignments
                Matcher roomMatcher = roomPattern.matcher(line);
                if (roomMatcher.find()) {
                    int targetRoom = Integer.parseInt(roomMatcher.group(1));
                    if (currentRoom != -1) {
                        addConnection(currentRoom, targetRoom);
                    }
                    currentRoom = targetRoom;
                }
                
                // Track room modifications (++ or --)
                Matcher modMatcher = roomModPattern.matcher(line);
                if (modMatcher.find()) {
                    int modification = Integer.parseInt(modMatcher.group(1));
                    if (currentRoom != -1) {
                        addConnection(currentRoom, currentRoom + modification);
                    }
                }
                
                // Identify room types and events
                if (currentRoom != -1) {
                    // Room type identification
                    if (line.contains("mathChallenge()")) {
                        roomTypes.put(currentRoom, "Math Challenge");
                    } else if (line.contains("enterShop()")) {
                        roomTypes.put(currentRoom, "Shop");
                    } else if (line.contains("combatRoom()")) {
                        roomTypes.put(currentRoom, "Combat");
                    } else if (line.contains("bossFightWithTimer()")) {
                        roomTypes.put(currentRoom, "Boss Fight");
                    }
                    
                    // Event tracking
                    if (line.contains("typeTextWithCursor")) {
                        String event = extractEventText(line);
                        if (event != null) {
                            roomEvents.computeIfAbsent(currentRoom, k -> new ArrayList<>())
                                    .add(event);
                        }
                    }
                }
            }

            // Add default room connections based on game logic
            for (int i = 1; i < 50; i++) {
                if (i % 2 == 0) { // Even rooms are shops
                    roomTypes.putIfAbsent(i, "Shop");
                    addConnection(i, i + 1);
                } else if (i % 10 == 0) { // Every 10th room is a math challenge
                    roomTypes.putIfAbsent(i, "Math Challenge");
                    addConnection(i, i + 1);
                } else { // Other rooms are combat
                    roomTypes.putIfAbsent(i, "Combat");
                    addConnection(i, i + 1);
                    // Combat rooms can go backwards when fleeing
                    if (i > 1) {
                        addConnection(i, i - 1);
                    }
                }
            }
            // Add boss room
            roomTypes.put(50, "Boss Fight");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String extractEventText(String line) {
        int startQuote = line.indexOf("\"");
        int endQuote = line.lastIndexOf("\"");
        if (startQuote != -1 && endQuote != -1 && startQuote != endQuote) {
            return line.substring(startQuote + 1, endQuote);
        }
        return null;
    }
    
    private static void addConnection(int from, int to) {
        roomConnections.computeIfAbsent(from, k -> new HashSet<>()).add(to);
    }
    
    private static void generateDotFile(String outputFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("digraph ShadowLabyrinth {");
            writer.println("  rankdir=LR;");
            writer.println("  node [shape=box, style=filled];");
            writer.println("  ranksep=1.5;");  // Increase space between nodes
            writer.println("  nodesep=0.5;");  // Increase space between nodes at same rank
            
            // Create invisible nodes for every 5 rooms to help with layout
            for (int i = 0; i < 50; i += 5) {
                writer.printf("  invisible%d [style=invis];\n", i);
            }
            
            // Write nodes with their types
            for (int i = 1; i <= 50; i++) {
                String type = roomTypes.getOrDefault(i, "Unknown");
                String color = getColorForType(type);
                String label = String.format("Room %d\\n(%s)", i, type);
                
                // Add event count if there are events
                List<String> events = roomEvents.get(i);
                if (events != null && !events.isEmpty()) {
                    label += String.format("\\n%d events", events.size());
                }
                
                writer.printf("  room%d [label=\"%s\", fillcolor=\"%s\"];\n", 
                            i, label, color);
            }
            
            // Write edges with different styles
            writer.println("\n  // Room connections");
            for (Map.Entry<Integer, Set<Integer>> entry : roomConnections.entrySet()) {
                int from = entry.getKey();
                for (int to : entry.getValue()) {
                    String style = to < from ? "dashed" : "solid"; // Dashed lines for fleeing
                    String color = to < from ? "red" : "black";
                    writer.printf("  room%d -> room%d [style=%s, color=%s];\n", 
                                from, to, style, color);
                }
            }
            
            writer.println("}");
            System.out.println("Generated GraphViz map in: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String getColorForType(String type) {
        switch (type) {
            case "Math Challenge": return "#ffcccb";
            case "Shop": return "#98fb98";
            case "Combat": return "#87ceeb";
            case "Boss Fight": return "#ff9999";
            default: return "#ffffff";
        }
    }
    
    private static void printEventAnalysis() {
        System.out.println("\n=== Room Event Analysis ===");
        TreeMap<Integer, List<String>> sortedEvents = new TreeMap<>(roomEvents);
        
        // Print a summary first
        System.out.println("\nRoom Type Summary:");
        Map<String, Integer> typeCounts = new HashMap<>();
        roomTypes.values().forEach(type -> 
            typeCounts.merge(type, 1, Integer::sum)
        );
        typeCounts.forEach((type, count) -> 
            System.out.printf("- %s: %d rooms\n", type, count)
        );

        // Print detailed event analysis
        System.out.println("\nDetailed Room Analysis:");
        sortedEvents.forEach((room, events) -> {
            System.out.printf("\nRoom %d (%s):\n", 
                            room, 
                            roomTypes.getOrDefault(room, "Unknown"));
            events.forEach(event -> System.out.println("- " + event));
        });
    }
}