class OrderItem {
    private String name;
    private int count;

    public OrderItem(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
    public void decrementCount() {
        --count;
    }

}