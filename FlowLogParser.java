import java.io.*;
import java.util.*;


public class FlowLogParser {
    public static void main(String[] args) throws IOException {
        // These are the input files(flow_logs.txt and lookup_table.csv)
        String flowLogFile = "flow_logs.txt";
        String lookupFile = "lookup_table.csv";

        // Read lookup table from the file and store it in a map
        Map<String, Set<String>> lookupTable = readLookupTable(lookupFile);
        // Create reverse lookup table mapping tags to port-protocol combinations
        Map<String, Set<String>> tagToCombinations = reverseLookupTable(lookupTable);

        // Parse flow logs and count occurrences of tags and port/protocol combinations
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();
        parseFlowLogs(flowLogFile, lookupTable, tagCounts, portProtocolCounts);

        printResults(tagCounts, portProtocolCounts, tagToCombinations);
    }


    
    /**
     * Reads the lookup table from the specified CSV file and stores the port-protocol combinations 
     * as keys and corresponding tags as values in a Map. Each key is a combination of the destination 
     * port and protocol (e.g., "443,tcp"), and the value is a set of tags that can be applied to this combination.
     * 
     * @param lookupFile The path to the lookup table CSV file containing the mappings.
     * @return A Map where each key is a port-protocol combination, and the value is a set of tags associated with that combination.
     * @throws IOException If an error occurs while reading the file.
     */
    private static Map<String, Set<String>> readLookupTable(String lookupFile) throws IOException {
        // The map to store the lookup table data
        Map<String, Set<String>> lookupTable = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(lookupFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                
                // If line has valid number of columns, add to lookup table
                if (parts.length == 3) {
                    String key = parts[0] + "," + parts[1].toLowerCase();
                    lookupTable.putIfAbsent(key, new HashSet<>());
                    lookupTable.get(key).add(parts[2].toLowerCase());
                }
            }
        }

        return lookupTable;
    }



    /**
     * Creates a reverse mapping from tags to their associated port-protocol combinations. 
     * This method takes the original lookup table, where keys are port-protocol combinations 
     * and values are sets of tags, and reverses it so that each tag points to all the 
     * combinations it can be applied to. 
     * 
     * @param lookupTable A Map where each key is a port-protocol combination, and the value is a set of tags.
     * @return A Map where each key is a tag, and the value is a set of port-protocol combinations associated with that tag.
    */
    private static Map<String, Set<String>> reverseLookupTable(Map<String, Set<String>> lookupTable) {
        // The map to store reverse lookup data: tag to port-protocol combinations
        Map<String, Set<String>> tagToCombinations = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : lookupTable.entrySet()) {
            String combination = entry.getKey();
            
            // For each tag, add the port-protocol combination to the tag map
            for (String tag : entry.getValue()) {
                tagToCombinations.putIfAbsent(tag, new HashSet<>());
                tagToCombinations.get(tag).add(combination);
            }
        }

        return tagToCombinations;
    }



    /**
     * Parses the flow log file and counts the occurrences of each tag and port-protocol combination. 
     * For each line in the flow log, this method extracts the destination port and protocol, 
     * checks the lookup table for matching tags, and updates the count for each found tag and combination.
     * If no matching tag is found, it increments the count for the "Untagged" category.
     *
     * @param flowLogFile The path to the flow log file containing the data to be parsed.
     * @param lookupTable A Map containing port-protocol combinations as keys and sets of tags as values.
     * @param tagCounts A Map to store the count of each tag.
     * @param portProtocolCounts A Map to store the count of each port-protocol combination.
     * @throws IOException If an error occurs while reading the flow log file.
    */
    private static void parseFlowLogs(String flowLogFile, Map<String, Set<String>> lookupTable,
                                      Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(flowLogFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                
                // Ensure line matches default format and version is 2
                if (parts.length >= 8 && "2".equals(parts[0])) {
                    String port = parts[5];
                    String protocol = parts[7];
                    // Construct key for lookup
                    String key = port + "," + protocol.toLowerCase();
                    // Retrieve corresponding tags or default to "Untagged"
                    Set<String> tags = lookupTable.getOrDefault(key, new HashSet<>(Collections.singleton("Untagged")));

                    // Update tag counts
                    for (String tag : tags) {
                        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
                    }

                    // Update port-protocol combination counts
                    portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);
                }
            }
        }
    }



    private static void printResults(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts, Map<String, Set<String>> tagToCombinations) {
        System.out.println("Tag Counts:");
        System.out.println("Tag,Count");
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

        System.out.println("\nPort/Protocol Combination Counts:");
        System.out.println("Port,Protocol,Count");
        for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
            String[] parts = entry.getKey().split(",");
            System.out.println(parts[0] + "," + parts[1] + "," + entry.getValue());
        }

        System.out.println("\nTag to Port/Protocol Combinations:");
        for (Map.Entry<String, Set<String>> entry : tagToCombinations.entrySet()) {
            System.out.println("Tag: " + entry.getKey());
            for (String combination : entry.getValue()) {
                System.out.println(" - " + combination);
            }
        }
    }
}
