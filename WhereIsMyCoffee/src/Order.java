import java.util.*;
/**
 * A class for handling an order's components
 * 
 * @author Jessica Corkal and Vitor Betmann
 * @version 04/16/2024
 *
 */
public class Order {
    /**
     * name of the order
     */
    private String name;
    /**
     * list of ingredients needed to make the order
     */
    private ArrayList<Ingredient> order = new ArrayList<Ingredient>();

    /**
     * Order constructor
     * @param fullOrder 
     */
    public Order(String[] fullOrder) {
        this.name = fullOrder[0];
        for (int i = 1; i < fullOrder.length; i++) {
            this.order.add(new Ingredient(fullOrder[i]));
        }
    }
    

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getOrderArray() {
        return order;
    }
    
    public int getOrderSize() {
        return this.order.size();
    }

    public void setOrder(ArrayList<Ingredient> orderArray) {
        this.order = orderArray;
    }
}