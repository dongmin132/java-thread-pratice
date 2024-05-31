import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int NUM_CHEFS = 4;
    private static final List<OrderItem> orderQueue = new ArrayList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        List<OrderItem> orders = new ArrayList<>(List.of(
                new OrderItem("피자", 2),
                new OrderItem("파스타", 3),
                new OrderItem("샐러드", 1),
                new OrderItem("스테이크", 4),
                new OrderItem("수프", 2),
                new OrderItem("디저트", 2)
        ));

        // Add orders to the queue
        synchronized (lock) {
            orderQueue.addAll(orders);
        }


        RamenCook ramenCook = new RamenCook(orderQueue, lock);
        // Create and start threads
        List<Thread> chefs = new ArrayList<>();
        for (int i = 0; i < NUM_CHEFS; i++) {


            Thread chefThread = new Thread(ramenCook, "Chef " + (i + 1));
            chefs.add(chefThread);
            chefThread.start();
        }

        Thread orderThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("주문 추가: 1을 입력하세요.");
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    synchronized (lock) {
                        orderQueue.add(new OrderItem("피자", 2));
                        lock.notifyAll(); // Notify all waiting threads
                        System.out.println("새 주문이 추가되었습니다.");
                    }
                }
            }
        });

        orderThread.start();



        System.out.println("All orders have been processed.");
    }
}