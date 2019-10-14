 package rims.core;

import rims.exception.*;
import rims.resource.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourceList {
    protected HashMap<String, ArrayList<Resource>> resources;

    public ResourceList(HashMap<String, ArrayList<Resource>> resources) {
        this.resources = resources;
    }

    public HashMap<String, ArrayList<Resource>> getResources() {
        return resources;
    }

    public int getAvailableQuantity(String resourceName) {
        ArrayList<Resource> thisResourceArray = resources.get(resourceName);
        int availableQty = 0;
        for (int i = 0; i < thisResourceArray.size(); i++) {
            if (!(thisResourceArray.get(i).isBooked())) {
                availableQty++;
            }
        }
        return availableQty;
    }

    public int getBookedQuantity(String resourceName) {
        ArrayList<Resource> thisResourceArray = resources.get(resourceName);
        int bookedQty = 0;
        for (int i = 0; i < thisResourceArray.size(); i++) {
            if (thisResourceArray.get(i).isBooked()) {
                bookedQty++;
            }
        }
        return bookedQty;
    }

    public int getBookedQuantityOfOrder(String resourceName, int loanId) {
        ArrayList<Resource> thisResourceArray = resources.get(resourceName);
        int bookedQty = 0;
        for (int i = 0; i < thisResourceArray.size(); i++) {
            Resource thisResource = thisResourceArray.get(i);
            if (thisResource.isBooked() && thisResource.getLoanId() == loanId) {
                bookedQty++;
            }
        }
        return bookedQty;
    }

    public int getTotalQuantity(String resourceName) {
        return resources.get(resourceName).size();
    }

    public Resource getAvailableResource(String resourceName) throws Exception {
        ArrayList<Resource> thisResourceArray = resources.get(resourceName);
        for (int i = 0; i < thisResourceArray.size(); i++) {
            Resource thisResource = thisResourceArray.get(i);
            if (!(thisResource.isBooked())) {
                return thisResource;
            }
        }
        throw new Exception("No available items!");
        // throw exception if nothing returned
        // replace with custom exception
    }

    public Resource getBookedResource(String resourceName, int loanId) throws Exception {
        ArrayList<Resource> thisResourceArray = resources.get(resourceName);
        for (int i = 0; i < thisResourceArray.size(); i++) {
            Resource thisResource = thisResourceArray.get(i);
            if (thisResource.isBooked() && thisResource.getLoanId() == loanId) {
                return thisResource;
            }
        }
        throw new Exception("No available items!");
        // replace with custom exception
    }

    public ArrayList<String> generateBookedList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("CURRENTLY BOOKED");
        for (Map.Entry<String, ArrayList<Resource>> entry : resources.entrySet()) {
            ArrayList<Resource> thisResourceArray = entry.getValue();
            int qtyBooked = getBookedQuantity(entry.getKey());
            if (thisResourceArray.size() > 0 && qtyBooked > 0) {
                if (thisResourceArray.get(0).getType() == 'I') {
                    list.add(thisResourceArray.get(0).toString() + " (qty: " + qtyBooked + ")");
                }
                else if (thisResourceArray.get(0).getType() == 'R') {
                    list.add(thisResourceArray.get(0).toString());
                }
            }
        }
        return list;
    }

    public ArrayList<String> generateAvailableList() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("AVAILABLE FOR LOAN");
        for (Map.Entry<String, ArrayList<Resource>> entry : resources.entrySet()) {
            ArrayList<Resource> thisResourceArray = entry.getValue();
            int qtyAvailable = getAvailableQuantity(entry.getKey());
            if (thisResourceArray.size() > 0 && qtyAvailable > 0) {
                if (thisResourceArray.get(0).getType() == 'I') {
                    list.add(thisResourceArray.get(0).toString() + " (qty: " + qtyAvailable + ")");
                }
                else if (thisResourceArray.get(0).getType() == 'R') {
                    list.add(thisResourceArray.get(0).toString());
                }
            }
        }
        return list;
    }

    /**
     * Gets total quantity of resources in ResourceList regardless of status.
     *
     * @return total quantity of resources.
     */
    public int getAllResourcesQuantity() {
        int qty = 0;
        for (ArrayList<Resource> identicalResources : resources.values()) {
            qty += identicalResources.size();
        }
        return qty;
    }

    /**
     * Adds new resource to ResourceList.
     * (OUTDATED + conceptually incorrect because resource id is allocated outside of ResourceList)
     * @param newResource new resource to add, can be item or room.
     */
    public void addResource(Resource newResource) {
        String resourceName = newResource.getName();
        if (resources.containsKey(resourceName)) {
            resources.get(resourceName).add(newResource);
        } else {
            resources.put(resourceName, new ArrayList<Resource>());
            resources.get(resourceName).add(newResource);
        }
    }

    /**
     * Adds a new resource to ResourceList given resource name and type.
     * @param resourceName Name of resource to add.
     * @param resourceType Type of resource: item or room.
     * @throws RimException when type of resource is not item nor room
     */
    public void addResource(String resourceName, char resourceType) throws RimException {
        Resource newResource;
        if (resourceType == 'I') {
            newResource = new Item(resourceName, getAllResourcesQuantity(), false);
        } else if (resourceType == 'R') {
            newResource = new Room(resourceName, getAllResourcesQuantity(), false);
        } else {
            throw new RimException("Invalid type of resource!");
        }

        if (resources.containsKey(resourceName)) {
            resources.get(resourceName).add(newResource);
        } else {
            resources.put(resourceName, new ArrayList<>());
            resources.get(resourceName).add(newResource);
        }
    }

    /**
     * Deletes a resource from ResourceList given the resourceName.
     * @param resourceName Name of resource to delete.
     * @return the resource that was deleted.
     * @throws RimException
     */
    public void deleteResource(String resourceName) throws Exception {

        if (!resources.containsKey(resourceName)) {
            throw new RimException("Resource not in list"); //resource stated not in list
        }

        Resource deletedResource = getAvailableResource(resourceName);
        resources.get(resourceName).remove(deletedResource);

        //Remove empty ArrayList entry in inventory if that resource is depleted
        if (resources.get(resourceName).isEmpty()) {
            resources.remove(resourceName);
        }
    }

    public ArrayList<String> generateAvailableListByDate(String day) throws ParseException {
        ArrayList<String> list = new ArrayList<String>();
        list.add("AVAILABLE FOR LOAN ON THIS DAY");
        for (Map.Entry<String, ArrayList<Resource>> entry : resources.entrySet()) {
            ArrayList<Resource> thisResourceArray = entry.getValue();
            int qtyAvailable = 0;
            for(int i = 0; i < thisResourceArray.size(); i++){
                Resource thisResource = thisResourceArray.get(i);
                if((thisResource.getDateBookedFrom() == null) && (thisResource.getDateBookedTill() == null)){
                    qtyAvailable += 1;
                }
                else if((thisResource.getDateBookedFrom().after(stringToDate(day))) || (thisResource.getDateBookedTill().before(stringToDate(day)))) {
                    qtyAvailable += 1;

                }
                if(i == thisResourceArray.size() - 1 && qtyAvailable > 0){
                    if (thisResourceArray.get(0).getType() == 'I') {
                        list.add(thisResourceArray.get(0).toString() + " (qty: " + qtyAvailable + ")");
                    }
                    else if (thisResourceArray.get(0).getType() == 'R') {
                        list.add(thisResourceArray.get(0).toString());
                    }
                }
            }
        }
        return list;
    }

    public ArrayList<String> generateBookedListByDate(String day) throws ParseException {
        ArrayList<String> list = new ArrayList<String>();
        list.add("CURRENTLY BOOKED ON THIS DAY");
        for (Map.Entry<String, ArrayList<Resource>> entry : resources.entrySet()) {
            ArrayList<Resource> thisResourceArray = entry.getValue();
            int qtyBooked = 0;
            for(int i = 0; i < thisResourceArray.size(); i++){
                Resource thisResource = thisResourceArray.get(i);
                if ((thisResource.getDateBookedFrom() != null) && (thisResource.getDateBookedTill() != null) && (thisResource.getDateBookedFrom().before(stringToDate(day))) && (thisResource.getDateBookedTill().after(stringToDate(day)))) {
                    qtyBooked += 1;
                }
                if((i == thisResourceArray.size() - 1) && qtyBooked > 0){
                    if (thisResourceArray.get(0).getType() == 'I') {
                        list.add(thisResourceArray.get(0).toString() + " (qty: " + qtyBooked + ")");
                    }
                    else if (thisResourceArray.get(0).getType() == 'R') {
                        list.add(thisResourceArray.get(0).toString());
                    }
                }
            }
        }
        return list;
    }

    public Date stringToDate(String stringDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
        Date dateValue = formatter.parse(stringDate);
        return dateValue;
    }

}