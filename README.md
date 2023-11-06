# Letterman_Project
The program uses a stack or queue, morph modes, and different constraints (change, swap, length) to find 
a solution path from a starting word to an ending word in the dictionary.
The architecture of the Letterman project consists of several classes and their interactions. Let's break down the key components and their roles:

1. `Main` class:
   - The entry point for the program.
   - Reads command-line arguments and passes them to the `Config` class.
   - Creates an instance of the `Letterman` class with the configuration and initiates the puzzle-solving process.

2. `Letterman` class:
   - Stores dictionary data and provides methods for solving the puzzle.
   - Reads the dictionary from standard input, filters words based on configuration, and stores them in an ArrayList.
   - Performs a search for the solution using a stack or queue data structure and various modes (change, swap, length).
   - Contains a backtracking method to trace the path from the end word to the start word.
   - Handles the output of word modifications based on the configuration.
   - Contains an inner class `WordInfo` to store dictionary words and their information.
   - Provides a method to print the dictionary.

3. `Config` class:
   - Stores and processes configuration settings for the project.
   - Reads command-line arguments using the `gnu.getopt` library.
   - Determines the data structure (stack or queue), morph modes (change, swap, length), and output format (word or modification).
   - Stores the beginning and end words and checkpoint options.
   - Checks for valid combinations of options and provides a help message.

4. Test cases:
   - Test cases are provided in the format of the number of words in the dictionary followed by the words themselves.

The program uses a stack or queue, morph modes, and different constraints (change, swap, length) to find a solution path from a starting word to an ending word in the dictionary. It also allows you to specify the output format (word or modification) and handles checkpoint options.

The program's architecture appears to be well-organized and modular, allowing for easy configuration and expansion with additional features. The provided test cases are meant to be part of the dictionary that the program uses to find a solution.
