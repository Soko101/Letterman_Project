import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *  Stores all of the dictionary data and provides methods for solving the puzzle
 */

public class Letterman {

    private Config c;

    // variable to store our dictionary
    private ArrayList<WordInfo> dictionary;

    private int beginIndex = -1;
    private int endIndex = -1;

    public Letterman(Config c) {
        this.c = c;

    }

    /**
     * read the dictionary from standard input
     */

    public void readDictionary() {
        // Scanner object
        Scanner in = new Scanner(System.in);


        // get number of words in dictionary
        int count = in.nextInt();

        // read to the end of the line
        in.nextLine();

        // construct our AL
        dictionary = new ArrayList<>(count);



        //read all of the words
        while (in.hasNextLine()) {
            String line = in.nextLine();

            // check for blank line
            if (line.length() == 0) {
                break;
            }


            // either  have a word or a comment
            // comment begins with two slashes
            // FIXME there is a bug
            if (line.charAt(0) == '/' && line.charAt(1) == '/') {
                // comment
                continue;
            }
            // check whether what you have is the firstword/ beginword
            if (line.equals(c.getBeginWord())) {
                beginIndex = dictionary.size();
            } else if (line.equals(c.getEndWord())) {
                endIndex = dictionary.size();
            }
            if (!c.isLengthMode()) {
                if (line.length() == (c.getBeginWord().length())) {
                    dictionary.add(new WordInfo(line));
                }
            }

            // TODO if swap or change are enabled, do not add any words to the dictionary that are not of the same

            // we have a word
            dictionary.add(new WordInfo(line));
        }
        if (beginIndex == -1) {
            System.err.println("Start word is not available in the dictionary");
            System.exit(1);
        }
        if (endIndex == -1) {
            System.err.println("End word is not available in the dictionary");
            System.exit(1);

        }

        // print the size of the dictionary
        System.out.println("Words in dictionary: " + count);
        /**
         *  search for a beginning word to an end word
         */

    }

    // search from beginning of the word to the end word
    public void backTrack() {
        ArrayList<String> words = new ArrayList<>();
        int index = endIndex;
        while (index != -1) {
            words.add(0, dictionary.get(index).text);
            index = dictionary.get(index).previousIdx;
        }
        System.out.println("Words in morph: " + words.size());

        if (c.isWordOutput()) {
            for (int i = 0; i < words.size(); i++) {
                System.out.println(words.get(i));
            }
        } else {
            System.out.println(dictionary.get(beginIndex).text);
            int k = 1;
            for (int i = 0; i < words.size(); i++) {
                if (k >= words.size()) {
                    break;
                }
                modificationOutput(words.get(i), words.get(k));
                k++;
            }
        }
    }

    public void search() {
        // deque to keep track of our reachable collection
        // store the index of the word were processing from our dictionary AL
        ArrayDeque<Integer> processing = new ArrayDeque<>();


        // initially populate this with the starting word
        // mark as visited and add to the deque
        dictionary.get(beginIndex).visited = true;
        dictionary.get(beginIndex).previousIdx = -1;


        if (c.isStackMode()) {
            processing.addFirst(beginIndex);
            if (c.isCheckpoint2()) {
                processPrintOut("add", dictionary.get(beginIndex).text, 0);
            }

        } else {
            processing.addLast(beginIndex);
            if (c.isCheckpoint2()) {
                processPrintOut("add", dictionary.get(beginIndex).text, 0);
            }
        }
        // Keeps track of the count and increases
        int count = 1;
        int numbs = 0;

        // while not empty
        while (!processing.isEmpty() && !dictionary.get(endIndex).visited) {
            // remove the next element
            int currIdx = processing.removeFirst();
            WordInfo curr = dictionary.get(currIdx);
            // loop through dictionary and check for sufficiently similar items
            numbs++;
            if (c.isCheckpoint2()) {
                processPrintOut("processing", curr.text, numbs);
            }
            for (int i = 0; i < dictionary.size(); i++) {
                // skip ourselves
                if (currIdx == i) {
                    continue;
                }
                WordInfo other = dictionary.get(i);
                if (!other.visited && sufficientlySimilar(curr.text, other.text)) {

                    // visit and add
                    other.visited = true;
                    other.previousIdx = currIdx;

                    if (c.isStackMode()) {
                        processing.addFirst(i);
                        if (c.isCheckpoint2()) {
                            processPrintOut("add", other.text, 0);
                        }
                    } else {
                        processing.addLast(i);
                        if (c.isCheckpoint2()) {
                            processPrintOut("add", other.text, 0);
                        }

                    }
                    count++;
                    if(processing.contains(endIndex)){
                        break;
                    }

                }

            }
        }

        if (!(dictionary.get(endIndex).visited)) {
            System.out.println("No solution, " + count + " words checked.");
            System.exit(0);
        }
        else{
            System.out.println("Solution, " + count + " words checked.");
            backTrack();
            }

    }



    private boolean sufficientlySimilar(String a, String b) {
        // change: 1 character difference
        // swap: 2 character different with the two characters adjacent and swapped
        // length: 1 character different and 1 character length

        int charDiff = 0;
        int diffIdx = 0;
        boolean first = false;
        boolean second = false;


        // are there ways to return as soon as we know we've failed to be sufficiently similar?
        if (a.length() == b.length()) {
            // only swap and change will apply
            // go character by character to check for equivalence
            for(int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    charDiff++;
                    diffIdx = i;
                }
            }
            if (c.isChangeMode()) {
                if (charDiff <= 1) {
                    return true;
                }
            }

            if (c.isSwapMode()) {
                //  we are keeping track of the index of the change
                // check if difference is 2 and then whether true for both first and second
                //
                // lastly return true

                    if (a.charAt(diffIdx) == b.charAt(diffIdx - 1)) {
                        first = true;
                    }

                    if (a.charAt(diffIdx - 1) == b.charAt(diffIdx)) {
                        second = true;
                    }
                return charDiff == 2 && (first) && (second);
                }

        } else {
            // only length will apply here
            String lngStr = a;
            String shrtStr = b;
            int nextChardiff = 0;



            if (b.length() > a.length()) {
                String temporary = a;
                lngStr = b;
                shrtStr = temporary;
            }
            if (lngStr.length() - shrtStr.length() == 1) {
                int t = 0;

                for(int i = 0; i < shrtStr.length(); i++) {
                    if (shrtStr.charAt(i) != lngStr.charAt(t)) {
                        nextChardiff++;
                        i--;
                    }
                    t++;
                    if(nextChardiff > 1) {
                        return false;
                        }
                    }
                if (nextChardiff == 0 && (shrtStr.charAt(shrtStr.length() - 1) != lngStr.charAt(lngStr.length() - 1))) {
                    return true;
            }
                if (nextChardiff == 0 && (shrtStr.charAt(shrtStr.length() - 1) == lngStr.charAt(lngStr.length() - 1))) {
                    return true;
                } else {
                    return nextChardiff == 1;
                }
            }

        }
        return false;
    }


    /**
     * Output the modification required to go from string a to string b
     * At this point, we already know one of the morphs applies, we just have to determine which one
     * @param a starting word for the morph
     * @param b ending word for the morph
     */
    public void modificationOutput(String a, String b ) {
        // need to find the first difference between string a and string b
        int pos = 0;
        int index = 0;
        // length of the shorter string - 1
        // the length of the shorter is -1 and hence is the
        // biggest index
        int maxPosition = Math.min(a.length(), b.length());
        //
        int charDifference = 0;
        while (pos < maxPosition) {
            // check for a difference
            if (a.charAt(pos) != b.charAt(pos)) {
                // we have found the position of the change
//                index = pos;
                break;
            }
            pos++;
        }

        // pos is either (1) the position of the change or (2) the index of the last character in the longer string
        // change, swap, insert, or delete?
        if (a.length() == b.length()) {
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    charDifference++;
                }
            }
            // Here we are implementing change
            if (charDifference == 1) {
                System.out.println("c," + pos + "," + b.charAt(pos));

            } else { // implement swap
                System.out.println("s," + pos);
            }
        }

        // Here we are carrying out insertion
        if (a.length() < b.length()) {
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    pos = i;
                    break;
                }
            }
            System.out.println("i," + pos + "," + b.charAt(pos));
        }
        //  Here we are carrying out deletion
        if (a.length() > b.length()) {
            for (int i = 0; i < b.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    pos = i;
                    break;
                }
            }
            System.out.println("d," + pos);
        }

    }

    /**
     *  output all words in the dictionary
     */

    public void printDictionary() {
        // enhanced for loop (for-each/ for-in loop)
        for(WordInfo w : dictionary) {
            System.out.println(w.text);
        }
    }
    // Method for simply printing output from
    private void processPrintOut(String a, Object w, int b) {
        if (a.equals("add") && b == 0) {
            System.out.println("adding " + w);
        } else if (a.equals("processing")) {
            System.out.println(b + ": processing " + w);
        }

    }

    //inner class (helper class) to store dictionary words
    private class WordInfo {
        public int previousIdx;
        String text;
        boolean visited;

        public WordInfo(String text) {

            this.text = text;
            this.visited = false;
        }
     }
}
