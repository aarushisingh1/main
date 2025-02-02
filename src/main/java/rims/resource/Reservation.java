package rims.resource;

import rims.exception.RimsException;

import java.util.Calendar;
import java.util.Date;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

//@@author isbobby
/**
 * This class represents an instance of a Reservation. Contains the reservation ID, the resource ID of the Resource
 * that it represents a booking of, and the dates between which this Resource has been booked.
 */
public class Reservation {
    private int reservationId;
    private int userId;
    private int resourceId;
    private Date dateFrom;
    private Date dateTill;

    /**
     * Constructor for a newly created Reservation.
     * @param reservationId the generated reservation ID of this new Reservation
     * @param resourceId the ID of the resource for which this Reservation has been made.
     * @param userId the ID of the user who made this Reservation.
     * @param dateFrom the Date from which this Reservation has been made.
     * @param dateTill the Date till which this Reservation has been made.
     */
    public Reservation(int reservationId, int resourceId, int userId,
        Date dateFrom, Date dateTill) {
        this.reservationId = reservationId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.dateFrom = dateFrom;
        this.dateTill = dateTill;
    }

    /**
     * Constructor for an existing Reservation loaded from storage.
     * @param reservationId the reservation ID of this Reservation
     * @param resourceId the ID of the resource for which this Reservation has been made.
     * @param userId the ID of the user who made this Reservation.
     * @param dateFrom the string representation of the date from which this Reservation has been made.
     * @param dateTill the string representation of the date till which this Reservation has been made.
     * @throws ParseException if the dates given are in an invalid format.
     */
    public Reservation(int reservationId, int resourceId, int userId,
        String dateFrom, String dateTill) throws RimsException {
        this.reservationId = reservationId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.dateFrom = stringToDate(dateFrom);
        this.dateTill = stringToDate(dateTill);
    }

    /**
     * Returns the reservation ID of this Reservation.
     * @return the reservation ID of this Reservation.
     */
    public int getReservationId() {
        return reservationId;
    }

    /**
     * Returns the resource ID for which this Reservation has been made.
     * @return the resource ID for which this Reservation has been made.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Returns the ID of the user who made this Reservation.
     * @return the ID of the user who made this Reservation.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the starting date of this Reservation.
     * @return the Date object representing the starting date of this Reservation.
     */
    public Date getStartDate() {
        return dateFrom;
    }

    /**
     * Returns the ending date of this Reservation.
     * @return the Date object representing the ending date of this Reservation.
     */
    public Date getEndDate() {
        return dateTill;
    }

    //@@author rabhijit
    /**
     * Checks if this Reservation has expired (the current date is past the ending date of this Reservation).
     * @return a boolean: true if this Reservation has not expired yet, false otherwise.
     */
    public boolean isStillValid() {
        Date currentDate = new Date(System.currentTimeMillis());
        if (currentDate.after(dateFrom) && currentDate.after(dateTill)) {
            return false;
        }
        return true;
    }

    /**
     * Converts the stored Reservation to a readable String for output to the CLI.
     * @return a String version of the attributes of the Reservation.
     */
    public String toString() {
        String output = "[" + reservationId + "]" + " borrowed by user: " + userId + " from "
                + getDateToPrint(dateFrom) + " till " + getDateToPrint(dateTill);
        if (isOverdue()) {
            return output + " [OVERDUE]";
        }
        return output;
    }

    /**
     * Converts the stored Reservation into a compact String to be stored in a data file.
     * @return a compact String version of the attributes of the Reservation.
     */
    public String toDataFormat() {
        return reservationId + "," + resourceId + "," + userId + ","
                + dateToString(dateFrom) + "," + dateToString(dateTill);
    }

    /**
     * Converts a date and time inputted by the user in String format, into a Date object.
     * @param stringDate the date and time inputted by the user in String format.
     * @return a Date object representing the date and time inputted by the user.
     */
    public Date stringToDate(String stringDate) throws RimsException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
        Date dateValue;
        try {
            dateValue = formatter.parse(stringDate);
        } catch (ParseException e) {
            throw new RimsException("Invalid format of date " + stringDate + "!");
        }
        return dateValue;
    }

    /**
     * Converts a Date object to a compact String.
     * @param thisDate the Date object to be converted into a String.
     * @return a String representing the Date object.
     */
    public String dateToString(Date thisDate) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HHmm");
        String stringDate = format.format(thisDate);
        return stringDate;
    }

    /**
     * Converts a Date object into a human-readable String, for the user's reading.
     * @param date the Date object to be converted into a String.
     * @return a human-readable String representing the Date object.
     */
    public String getDateToPrint(Date date) {
        DateFormat dayFormat = new SimpleDateFormat("d");
        int actualDay = Integer.parseInt(dayFormat.format(date));
        int roundedDay = actualDay % 10;
        String suffix;
        if (actualDay < 20 && actualDay > 9) {
            suffix = "th";
        } else if (roundedDay == 1) {
            suffix = "st";
        } else if (roundedDay == 2) {
            suffix = "nd";
        } else if (roundedDay == 3) {
            suffix = "rd";
        } else {
            suffix = "th";
        }
        String stringDate = (new SimpleDateFormat("EEEEE, ")).format(date) + actualDay + suffix
                + " " + (new SimpleDateFormat("MMMMM yyyy, hh:mm aaa")).format(date);
        return stringDate;
    }

    /**
     * Returns the number of days within which this Reservation expires.
     * @return the number of days within which this Reservation expires.
     */
    public int getDaysDueIn() {
        Date currentDate = new Date(System.currentTimeMillis());
        int daysLeftToDue = (int) (TimeUnit.DAYS.convert((getEndDate().getTime()
                - currentDate.getTime()), TimeUnit.MILLISECONDS));
        return daysLeftToDue;
    }

    /**
     * Checks if this Reservation is expiring within a given number of days.
     * @param daysDue the number of days within which this Reservation is checked for expiry
     * @return a boolean: true if it is expiring within the given number of days, false otherwise
     */
    public boolean isDueInDays(int daysDue) {
        return getDaysDueIn() <= daysDue;
    }

    //@author hin1
    /**
     * Checks if this Reservation has already expired.
     * @return a boolean: true if it has already expired, false otherwise.
     */
    public boolean isOverdue() {
        Date currentDate = new Date(System.currentTimeMillis());
        int daysLeftToDue = (int) (TimeUnit.DAYS.convert((getEndDate().getTime()
                - currentDate.getTime()), TimeUnit.MILLISECONDS));
        boolean wasDuePast = daysLeftToDue < 0;
        boolean wasDueEarlierToday = (daysLeftToDue == 0 && (getEndDate().getTime() - currentDate.getTime() < 0));
        if (wasDuePast || wasDueEarlierToday) {
            return true;
        }
        return false;
    }

}