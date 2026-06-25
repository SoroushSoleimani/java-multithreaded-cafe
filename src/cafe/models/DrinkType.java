package cafe.models;

public enum DrinkType {
    ESPRESSO("Espresso", 2, 0, 1, true),
    LATTE("Latte", 2, 3, 1, true),
    CAPPUCCINO("Cappuccino", 2, 2, 1, true),
    TEA("Tea", 0, 0, 1, false);

    private final String name;
    private final int coffeeRequired;
    private final int milkRequired;
    private final int cupsRequired;
    private final boolean needsCoffeeMachine;

    DrinkType(String name, int coffeeRequired, int milkRequired, int cupsRequired, boolean needsCoffeeMachine) {
        this.name = name;
        this.coffeeRequired = coffeeRequired;
        this.milkRequired = milkRequired;
        this.cupsRequired = cupsRequired;
        this.needsCoffeeMachine = needsCoffeeMachine;
    }

    public String getName() { return name; }
    public int getCoffeeRequired() { return coffeeRequired; }
    public int getMilkRequired() { return milkRequired; }
    public int getCupsRequired() { return cupsRequired; }
    public boolean isNeedsCoffeeMachine() { return needsCoffeeMachine; }
}