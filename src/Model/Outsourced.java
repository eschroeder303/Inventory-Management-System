package Model;

/**
 * Outsourced Part Class
 * @author Edward Schroeder
 */

public class Outsourced extends Part{
    private String companyName;

    /**
     * Outsourced Part Constructor
     * @param id - Part ID
     * @param name - Part Name
     * @param price - Part Price
     * @param stock - Current Stock
     * @param min - Minimum Stock
     * @param max - Maximum Stock
     * @param companyName - Outsourced Company Name
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Setter
     * @param companyName - Outsourced Company Name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Getter
     * @return - companyName of Outsourced Company Name
     */
    public String getCompanyName() {
        return companyName;
    }
}