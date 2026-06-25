package cafe.models;

public class Order {
    private final int id;
    private final String customerName;
    private final DrinkType drinkType;
    private final int preparationTime;
    private final int priority;
    

    public Order(int id, String customerName, DrinkType drinkType, int preparationTime, int priority) {
        this.id = id;
        this.customerName = customerName;
        this.drinkType = drinkType;
        this.preparationTime = preparationTime;
        this.priority = priority;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public DrinkType getDrinkType() { return drinkType; }
    public int getPreparationTime() { return preparationTime; }
    public int getPriority() { return priority; }
}