import java.util.List;

class Chef implements Runnable {
    private final List<OrderItem> orderQueue;
    private final Object lock;
    private final String[] burners = {"_", "_", "_", "_"};
    private final int chefId;

    public Chef(List<OrderItem> orderQueue, Object lock, int chefId) {
        this.orderQueue = orderQueue;
        this.lock = lock;
        this.chefId = chefId;
    }

    @Override
    public void run() {
        while (true) {
            OrderItem orderItem;
            synchronized (lock) {
                while (orderQueue.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                orderItem = orderQueue.remove(0);
            }
            cook(orderItem);
        }
    }

    private void cook(OrderItem orderItem) {
        String itemName = orderItem.getName();
        int itemCount = orderItem.getCount();
        for (int i = 1; i <= itemCount; i++) {
            synchronized (lock) {
                System.out.println("Chef " + chefId + " is cooking " + itemName + " (" + i + "/" + itemCount + ")");
                for (int j = 0; j < burners.length; j++) {
                    if (burners[j].equals("_")) {
                        burners[j] = "Chef " + chefId;
                        System.out.println("Chef " + chefId + " is using burner " + (j + 1));
                        showBurners();
                        try {
                            Thread.sleep(10000); // Simulate cooking time
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                        burners[j] = "_";
                        System.out.println("Chef " + chefId + " has finished using burner " + (j + 1));
                        showBurners();
                        break;
                    }
                }
            }
        }
        System.out.println("Chef " + chefId + " has completed " + itemName + "!");
    }

    private void showBurners() {
        StringBuilder stringToPrint = new StringBuilder("Burners: ");
        for (String burner : burners) {
            stringToPrint.append(" ").append(burner);
        }
        System.out.println(stringToPrint);
    }
}