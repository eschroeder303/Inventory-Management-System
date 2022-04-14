package Model;

/**
 * InHouse Part Class
 * @author Edward Schroeder
 */

public class InHouse extends Part{
    private int machineID;

    /**
     * InHouse Part Constructor
     * @param id - Part ID
     * @param name - Part Name
     * @param price - Part Price
     * @param stock - Current Stock
     * @param min - Minimum Stock
     * @param max - Maximum Stock
     * @param machineID - InHouse Machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Setter
     * @param machineID - InHouse Machine ID
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * Getter
     * @return - machineID of InHouse Part
     */
    public int getMachineID(){
        return machineID;
    }
}