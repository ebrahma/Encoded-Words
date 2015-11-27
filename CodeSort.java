import java.io.IOException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Class to order list of coded words from ordered sample.
 * @author Emily, Hansin, Mariya
 */
public final class CodeSort {
    
    /**
     * Number of arguments needed.
     */
    public static final int NUM_ARGS = 3;

    /**
     * Constructor for Checkstyle compliance.
     */
    private CodeSort() {
        
    }
    
    /**
     *Orders list of coded words from ordered sample.
     * @param args user's input.
     * @throws IOException if insufficient arguments are input.
     */
    public static void main(String[] args) throws IOException {
        
        
        
        if (args.length < NUM_ARGS) {
            throw new IllegalArgumentException("Three arguments are required.");
        }
        String sortFile = args[0];
        String unsortFile = args[1];
        String mySortFile = args[2];
        Scanner fromFile = new Scanner(new FileReader(sortFile));
        Scanner fromFile2 = new Scanner(new FileReader(unsortFile));
        PrintWriter writer = new PrintWriter(mySortFile);
        ArrayList<String> wordList = new ArrayList<String>();
        ArrayList<Word> unsortedList = new ArrayList<Word>();
        Map<Character, Integer> myMap = new HashMap<Character, Integer>();

        while (fromFile.hasNextLine()) {
            wordList.add(fromFile.nextLine());
        }


        while (fromFile2.hasNextLine()) {
            Word w = new Word(fromFile2.nextLine());
            unsortedList.add(w);
        }

        List<Character> myList = findAlphaOrdering(wordList);


        for (int k = 0; k < myList.size(); k++) {
            myMap.put(myList.get(k), k);
        }

        selectionSort(unsortedList, myMap);

        for (int j = 0; j < unsortedList.size(); j++) {
            writer.println(unsortedList.get(j).word);

        }

        writer.close();

        fromFile.close();
        fromFile2.close();
    }

    /**
     * Selection sort to organize elements.
     * @param arr ArrayList of words.
     * @param map HashMap of characters and integers.
     */
    public static void selectionSort(ArrayList<Word> arr, 
            Map<Character, Integer> map) {
        for (int i = 0; i < arr.size() - 1; ++i) {
            int minIndex = i;
            for (int j = i + 1; j < arr.size(); ++j) {
                if (arr.get(j).compare(arr.get(minIndex), map) < 0) {
                    minIndex = j;
                }
            }
            // String changed to T
            Word temp = arr.get(i);
            arr.set(i, arr.get(minIndex));
            arr.set(minIndex, temp);
        }
    }


    /**
     * Determines the "alphabetical" ordering.
     * @param words ArrayList of all words.
     * @return List of words in order.
     */
    private static List<Character> findAlphaOrdering(ArrayList<String> words) {
        Graph<Character> followsGraph = new Graph<Character>();
        buildFollowsGraph(followsGraph, words);

        return followsGraph.topoSort();
    }
    
    /**
     * Establishes the relations between symbols.
     * @param followsGraph Graph to arrange symbols within.
     * @param words ArrayList of all words.
     */
    private static void buildFollowsGraph(Graph<Character> followsGraph, 
            ArrayList<String> words) {
        String prev = words.get(0);
        insertVertices(followsGraph, prev);

        for (int i = 1; i < words.size(); ++i) {
            insertVertices(followsGraph, words.get(i));
            generateRelationship(followsGraph, prev, words.get(i));
            prev = words.get(i);
        }
    }
    
    /**
     * Method to add Vertices to Graph.
     * @param followsGraph Graph to add characters to as Vertices
     * @param word to be broken down into characters.
     */
    private static void insertVertices(Graph<Character> followsGraph, 
            String word) {
        for (int i = 0; i < word.length(); ++i) {
            followsGraph.addVertex(word.charAt(i));
        }
    }

    /**
     * Cycles through two words character by character and compare 
     * to find what the proper ordering is.
     * @param followsGraph Graph to add Edge, 
     * to establish relation between two words.
     * @param prev first word for comparison.
     * @param curr second word for comparison.
     */
    private static void generateRelationship(Graph<Character> followsGraph, 
            String prev, String curr) {
        int i = 0;
        int j = 0;
        while (i < prev.length() && j < curr.length() 
                && prev.charAt(i) == curr.charAt(j)) {
            ++i;
            ++j;
        }

        if (i < prev.length() && j < curr.length()) {
            followsGraph.addEdge(curr.charAt(j), prev.charAt(i));
        }
    }

    /**
     * Class for Graph object composed of Vertices representing symbols.
     * @param <V> Vertices
     */
    static final class Graph<V> {
        
        /**
         * Produces HashMap of edges representing relations between
         * the symbols.
         */
        private Map<V, LinkedList<V>> edgeMap = new HashMap<V, LinkedList<V>>();
        
        /**
         * Add a new Vertex.
         * @param v Vertex to be added to the Graph.
         */
        public void addVertex(V v) {
            if (!this.edgeMap.containsKey(v)) {
                this.edgeMap.put(v, new LinkedList<V>());
            }
        }

        /**
         * Add a new Edge.
         * @param from starting vertex.
         * @param to ending vertex.
         */
        public void addEdge(V from, V to) {
            List<V> relations = this.edgeMap.get(from);
            relations.add(to);
        }
        
        /**
         * Method to get String representation of edgeMap.
         * @return String representation of edgeMap.
         */
        public String toString() {
            return this.edgeMap.toString();
        }

        /**
         * Method to carry out topological sort.
         * @return sorted List of Vertices.
         */
        public List<V> topoSort() {
            List<V> orderedVertices = new LinkedList<V>();
            
            Set<V> discovered = new HashSet<V>();
            Set<V> processed = new HashSet<V>();
            Stack<V> dfsStack = new Stack<V>();

            for (V startNode:this.edgeMap.keySet()) {
                if (discovered.contains(startNode)) {
                    continue;       
                }

                dfsStack.push(startNode);
                while (!dfsStack.isEmpty()) {
                    V top = dfsStack.peek();
                    if (!discovered.contains(top)) {
                        discovered.add(top);
                        for (V predecessor: this.edgeMap.get(top)) {
                            dfsStack.push(predecessor);
                        }
                    } else {
                        dfsStack.pop();
                        if (!processed.contains(top)) {
                            processed.add(top);
                            orderedVertices.add(top);
                        }
                    }
                }

            }
            return orderedVertices;
        }

    }
    
    /**
     * Class for Word object.
     */
    static final class Word {
        
        /**
         * The word itself.
         */
        String word;
        
        /**
         * Constructor for Word object.
         * @param s the word itself.
         */
        public Word(String s) {
            this.word = s;
        }
        
        /**
         * Compare method for comparing this word to another.
         * @param b word to be compared to.
         * @param map Map of characters.
         * @return integer representing whether or not this word
         * is greater than the word it is compared to.
         */
        public int compare(Word b, Map<Character, Integer> map) {
            int length1 = this.word.length();
            int length2 = b.word.length();
            int smaller = 0;
            boolean greater = false;
            
            if (length1 > length2) {
                greater = true;
                smaller = length2;
            } else {
                smaller = length1;
            }

            for (int i = 0; i < smaller; i++) {
                if (map.get(this.word.charAt(i)) < map.get(b.word.charAt(i))) {
                    return -1;
                } else if (map.get(this.word.charAt(i)) 
                        > map.get(b.word.charAt(i))) {
                    return 1;
                }
            } 

            if (greater) {
                return 1;
            }

            return -1;

        }
    }


}