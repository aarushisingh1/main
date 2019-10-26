package rims.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import rims.command.Command;
import rims.exception.RimsException;

/**
 * The main class that instantiates all the sub-classes that carry out
 * the relevant sub-tasks of RIMS.
 */
public class Rim {
    private Storage storage;
    private ResourceList resources;
    private Ui ui;
    private Parser parser;

    /**
     * Constructor for RIMS that instantiates the necessary sub-classes for
     * its operation.
     * @param resourceFilePath the file path of the document where resource data is stored.
     * @param reserveFilePath the file path of the document where reservation data is stored.
     * @throws FileNotFoundException if any file path is invalid
     * @throws ParseException if data is stored in an invalid format and is thus unable to be parsed
     */
    public Rim(String resourceFilePath, String reserveFilePath) throws FileNotFoundException, ParseException {
        ui = new Ui();
        storage = new Storage(resourceFilePath, reserveFilePath);
        resources = new ResourceList(storage.getResources());
        parser = new Parser(ui, resources);
    }

    /**
     * This method repeatedly runs the parser, which obtains and parses the input, and
     * depending to the parsed input, creates an executable command, which then carries out
     * the necessary tasks. Will halt when a command issues an exit code of true.
     * @throws ParseException if input is un-parsable
     * @throws IOException if there is an error in reading input or printing output
     */
    public void run() throws ParseException, IOException {
        Boolean toExit = false;
        while (!toExit) {
            try {
                Command c = parser.parseInput(ui.getInput());
                c.execute(ui, storage, resources);
                toExit = c.getExitCode();
            }
            catch (RimsException e) {
                e.displayError();
            }
        }
    }

    /**
     * The main method that calls the RIMS constructor and sets the ball rolling.
     * @throws FileNotFoundException if file path does not exist
     * @throws ParseException if any input is un-parsable
     * @throws IOException if there is an error in reading input or printing output
     * @throws DukeException if the input has no meaning or does not follow our format
     */
    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException, RimsException {
        new Rim("data/resources.txt", "data/reserves.txt").run();
    }
}