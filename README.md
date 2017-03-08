# Text Search Engine (In-memory)

This is a basic plaintext-based search engine. The input is a number of plaintext files stored in .txt format.
The user enters plaintext queries, and is presented by an ordered list of results.
There is a maximum limit of 10 results (a constant set in the code) which can easily be configured
to the user's ease.

## Ranked retrieval
The program uses a basic tf*idf comparison model, as detailed here: (https://en.wikipedia.org/wiki/Tf%E2%80%93idf).

## Testing
The program has been tested using plain text files from [Project Gutenberg](https://www.gutenberg.org/).


## Future work
The basic comparison function does not take into account the position of the words within the
documents. A future improvement is to add that functionality, by storing the locations of
where each word occurs in the documents.

## Setup

### IntelliJ/jar
IntelliJ can be used easily to create a jar for this project, by creating a new project and importing these
files. Once you have the jar file, run the following:

`java -jar TextSearchEngine.jar <document-directory>`

### Command line
To manually compile the java files into class files, run:

`javac Catalog.java DocumentNameFilter.java NoDocumentsFoundException.java ResultComparator.java Document.java Main.java Result.java`

Then the program can be run by placing the class files in com/abhishek, and running:

`java com/abhishek/Main <document-directory>`

