import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * Store and process all configurations for this project
 */

public class Config {
    // member variables to store our settings

    //stack vs queue
    private boolean stackMode;

    // morph modes
    private boolean changeMode;
    private boolean swapMode;
    private boolean length;

    // output mode
    private boolean wordOutput = false;

    // modification output
    private boolean modificationOutput = false;

    // begin and end word
    private String beginWord;
    private String endWord;

    // checkpoints
    private boolean checkpoint1;
    private boolean checkpoint2;

    /**
     * construct our configuration object and process the command line arguments
     * @param args string of the command line arguments
     */

    public Config(String[] args) {


        // Getopt processing
        LongOpt[] longOptions = {
                new LongOpt("stack", LongOpt.NO_ARGUMENT, null, 's'),
                new LongOpt("queue", LongOpt.NO_ARGUMENT, null, 'q'),
                new LongOpt("length", LongOpt.OPTIONAL_ARGUMENT, null, 'l'),
                new LongOpt("change", LongOpt.OPTIONAL_ARGUMENT, null, 'c'),
                new LongOpt("swap", LongOpt.OPTIONAL_ARGUMENT, null, 'p'),
                new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
                new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b'),
                new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
                new LongOpt("help", LongOpt.OPTIONAL_ARGUMENT, null, 'h'),
                new LongOpt("checkpoint1", LongOpt.NO_ARGUMENT, null, 'x'),
                new LongOpt("checkpoint2", LongOpt.NO_ARGUMENT, null, 'y')


        };

        // construct the Getopt object
        Getopt g = new Getopt("Letterman", args, "sqcplo:b:e:hxy", longOptions);
        g.setOpterr(true);

        int choice;

        // error checking variables
        boolean routingModeSet = false;

        // error to check if both stack or queue is set on command
        routeFrequency(args);

        // loop through all the arguments
        while((choice = g.getopt()) != -1) {
            switch(choice) {
                case 's':
                    stackMode = true;
                    routingModeSet = true;
                    break;

                case 'q':
                    stackMode = false;
                    routingModeSet = true;
                    break;

                case 'c':
                    changeMode = true;
                    break;

                case 'p':
                    swapMode = true;
                    break;

                case 'l':
                    length = true;
                    break;

                case 'o':
                    // read the required  string argument
                    String mode = g.getOptarg();
                    if (!mode.equals("M") && !mode.equals("W")) {
                        //we have an error
                        System.err.println("Only W and M are supported for modes" + mode);
                        System.exit(1);
                    }

                    // Checking for when wordOutput command W is added to the flag
                    if (mode.equals("W") || mode.isEmpty()) {
                        wordOutput = true;
                    }

                    // Checking for when modificationOutput command M is added to the flag
                    if (mode.equals("M")) {
                        modificationOutput = true;
                    }


                    break;

                case 'b':
                    beginWord = g.getOptarg();
                    break;

                case 'e':
                    endWord = g.getOptarg();
                    break;

                case 'h':
                    // Calls help message method which prints out instructions on how the program works
                    printHelp();

                    break;

                case 'x':
                    checkpoint1 = true;

                    break;

                case 'y':
                    checkpoint2 = true;
                    break;

                default:
                    // Prints if the user enters an option or command not specified in the program or help message
                    System.err.println("Unknown command line argument option: " + choice);
                    System.exit(1);
            }
        }

        // if the routing mode is not set (neither stack or queue is specified)
        if (!routingModeSet) {
            System.err.println("One of the stack or queue mode must be specified");
            System.exit(1);
        }
        // If neither swap, length or change mode is specified
        if (!(swapMode || length || changeMode)){
            System.err.println("Atleast one mode should be specified");
            System.exit(1);
        }
        // if length mode is not specified and the length of the first word is not equal
        // to that of the last word
        if (!length && (beginWord.length()!= endWord.length())){
            System.err.println("Length mode needs to be input");
            System.exit(1);
        }

    }
    // help message to describe what happens as each key/ term is implemented
    private void printHelp() {
        System.out.println("Welcome! Please find instructions on how to use Letterman down below.");
        System.out.println("Use --queue (-q) or --stack(-s) in switching the data structure of Letterman.");
        System.out.println("Use --change (-c) to have Letterman change a single letter of a word.");
        System.out.println("Use --swap (-p) to have Letterman swap any single pair of adjacent letters.");
        System.out.println("Use --length (-l) to have Letterman add or remove a letter from a word.");
        System.out.println("Use --output(W|M) (-o(W|M)) to indicate the output file format, this option requires an additional " +
                "argument: either W for word or M for modification");
        System.out.println("Use --begin<word> (-b<word>) to specify which word should Letterman start with. This option" +
                "requires entering a word after the flag.");
        System.out.println("Use --end<word> (-e<word>) to specify which word should Letterman end with. This option" +
                "requires entering a word after the flag.");
        System.exit(0);;
    }
    // Method to check frequency and number of times that either the stack or
    // queue argument is added to the flag (hence to avoid adding both or more than once)
    public static void routeFrequency (String [] args) {

        // These count variables keep track of the stack and queue arguments on the cmd
        int count_t = 0;
        int count_g = 0;

        for(String str: args) {
            if(str.equals("-s") || str.equals("--stack")) {
                count_t += 1;

            } else if(str.equals("-q") || str.equals("--queue")) {
                count_g += 1;
            }
        }
        // checking whether argument added is greater than 1
        if(count_t + count_g > 1) {
            System.err.println("Only add 1 stack or queue argument");
            System.exit(1);
        }
    }
    // Methods used to call all of the variables and arguments are listed below from isChangeMode to getEndWord
    public boolean isChangeMode() {
        return changeMode;
    }

    public boolean isCheckpoint1() {
        return checkpoint1;
    }

    public boolean isCheckpoint2() {
        return checkpoint2;
    }

    public boolean isLengthMode() {
        return length;
    }

    public boolean isStackMode() {
        return stackMode;
    }

    public boolean isSwapMode() {
        return swapMode;
    }

    public boolean isWordOutput() {
        return wordOutput;
    }

    public String getBeginWord() {
        return beginWord;
    }

    public String getEndWord() {

        return endWord;
    }
}
