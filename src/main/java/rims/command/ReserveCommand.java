package rims.command;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import rims.core.ResourceList;
import rims.core.Storage;
import rims.core.Ui;
import rims.resource.Reservation;
import rims.resource.ReservationList;
import rims.resource.Resource;
import rims.exception.RimsException;

//@@author isbobby
/**
 * Creates a Reservation for a Resource in the ResourceList, given the ID of the user,
 * the name of the Resource and the dates between which the Reservation is valid.
 */
public class ReserveCommand extends Command {
    protected String resourceName;
    protected int qty;
    protected Date dateFrom;
    protected Date dateTill;
    protected String stringDateFrom = null;
    protected String stringDateTill;
    protected int userId;

    //@@author rabhijit
    /**
     * Constructor for a ReserveCommand, for a Room which is to be loaned from effective immediately
     * till a certain future date.
     * @param roomName the name of the Room to be loaned out.
     * @param stringDateTill the date by which the Room must be returned, in String format.
     * @param userId the ID of the user making the loan.
     */
    public ReserveCommand(String roomName, String stringDateTill, int userId) {
        resourceName = roomName;
        this.qty = 1;
        this.dateFrom = new Date(System.currentTimeMillis());
        this.stringDateTill = stringDateTill;
        this.userId = userId;
    }

    /**
     * Constructor for a ReserveCommand, for an Item which is to be loaned from effective immediately
     * till a certain future date.
     * @param itemName the name of the Item to be loaned out.
     * @param qty the quantity of the Item to be loaned out.
     * @param stringDateTill the date by which the Item(s) must be returned, in String format.
     * @param userId the ID of the user making the loan.
     */
    public ReserveCommand(String itemName, int qty, String stringDateTill, int userId) {
        resourceName = itemName;
        this.qty = qty;
        this.dateFrom = new Date(System.currentTimeMillis());
        this.stringDateTill = stringDateTill;
        this.userId = userId;
    }

    /**
     * Constructor for a ReserveCommand, for a Room which is to be reserved from a given date in the future
     * till a further future date.
     * @param roomName the name of the Room to be reserved.
     * @param stringDateFrom the date from which the Room is to be loaned out, in String format.
     * @param stringDateTill the date by which the Room must be returned, in String format.
     * @param userId the ID of the user making the reservation.
     */
    public ReserveCommand(String roomName, String stringDateFrom, String stringDateTill, int userId) {
        resourceName = roomName;
        this.qty = 1;
        this.stringDateFrom = stringDateFrom;
        this.stringDateTill = stringDateTill;
        this.userId = userId;
    }

    /**
     * Constructor for a ReserveCommand, for an Item which is to be reserved from a given date in the future
     * till a further future date.
     * @param itemName the name of the Item to be reserved.
     * @param qty the quantity of the Item to be reserved.
     * @param stringDateFrom the date from which the Item is to be loaned out, in String format.
     * @param stringDateTill the date by which the Item must be returned, in String format.
     * @param userId the ID of the user making the reservation.
     */
    public ReserveCommand(String itemName, int qty, String stringDateFrom, String stringDateTill, int userId) {
        resourceName = itemName;
        this.qty = qty;
        this.stringDateFrom = stringDateFrom;
        this.stringDateTill = stringDateTill;
        this.userId = userId;
    }

    //@@author isbobby
    /**
     * Checks if the reservation is possible given the number of available Resources and Reservations
     * that are already in place, and if it is possible, creates a Reservation for the desired number of 
     * Resources between the given dates.
     * @param ui An instance of the user interface.
     * @param storage An instance of the Storage class.
     * @param resources The ResourceList, containing all the created Resources thus far.
     * @throws RimsException if there are not enough Resources available between the desired dates
     *                       to make the Reservation possible
     * @throws ParseException if the dates specified are invalid.
     */
    @Override
    public void execute(Ui ui, Storage storage, ResourceList resources) throws RimsException, ParseException {
        if (!(stringDateFrom == null)) {
            dateFrom = resources.stringToDate(stringDateFrom);
        }
        dateTill = resources.stringToDate(stringDateTill);
        if (resources.getAvailableNumberOfResource(resourceName) < qty) {
            throw new RimsException("We don't have that many of this resource currently available!");
        }
        ArrayList<Resource> allOfResource = resources.getAllOfResource(resourceName);
        ArrayList<Resource> bookedResources = new ArrayList<Resource>();
        int qtyBooked = 0;
        for (int j = 0; j < allOfResource.size(); j++) {
            Resource thisResource = allOfResource.get(j);
            if (thisResource.isAvailableFrom(dateFrom, dateTill)) {
                thisResource.book(resources.generateReservationId(), userId, dateFrom, dateTill);
                bookedResources.add(thisResource);
                qtyBooked++;
            }
            if (qtyBooked == qty) {
                break;
            }
        }
        if (qtyBooked != 0) {
            ui.printLine();
            ui.print("Done! I've marked these resources as loaned:");
            for (int i = 0; i < bookedResources.size(); i++) {
                ui.print(bookedResources.get(i).toString() + " (ID: " + bookedResources.get(i).getResourceId() + ")");
            }
            ui.printLine();
        } else {
            throw new RimsException("This item is not available between the dates you've selected!");
        }
    }
}