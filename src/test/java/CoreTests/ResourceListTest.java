package CoreTests;

import rims.core.Parser;
import rims.core.ResourceList;
import rims.core.Ui;
import rims.exception.*;
import rims.resource.Item;
import rims.resource.Resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class covers the following methods:
 * getResourceById
 * getResourceByName
 * getNumberOfResource(name)
 * stringToDate
 * dateToString
 * 
 */
public class ResourceListTest {
    private static Parser parserUnderTest;
    private static Ui ui;
    private static ResourceList listUnderTest;
    private static Resource resourceUnderTest;

    @BeforeAll
    private static void init() throws RimsException {
        ui = new Ui();
        ArrayList<Resource> emptyList = new ArrayList<Resource>();
        listUnderTest = new ResourceList(ui, emptyList);

        resourceUnderTest = new Item(1, "testobject");
        listUnderTest.add(resourceUnderTest);

        parserUnderTest = new Parser(ui, listUnderTest);
    }

    @BeforeEach
    private void setup() {
        ;
    }

    @AfterEach
    private void cleanup() {
        ;
    }

    /**
     * User enters a valid input
     */
    @Test
    public void Test1(){
    }
}