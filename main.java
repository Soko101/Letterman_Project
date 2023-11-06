public class Main {
        public static void main(String[] args) {
               // read command line arguments
                Config c = new Config(args);

                // construct the letterman puzzle solver
                Letterman l = new Letterman(c);

                // process input
                l.readDictionary();

                l.search();

                if (c.isCheckpoint1()) {
                        l.printDictionary();
                        System.exit(0);
                }
        }
}