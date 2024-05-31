import java.util.List;

public class RamenCook implements Runnable {
    private final List<OrderItem> orderQueue;
    private final String[] burners = new String[]{"_", "_", "_", "_"};
    private final Object lock;

    public RamenCook(List<OrderItem> orderQueue, Object lock) {
        this.orderQueue = orderQueue;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            OrderItem currentOrder = null;
            synchronized (lock) {
//                while (orderQueue.isEmpty()) {
//                    try {
//                        System.out.println(Thread.currentThread().getName() + " is waiting for orders.");
//                        lock.wait();
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                        return;
//                    }
//                }

                if (orderQueue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " is waiting for orders.");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                currentOrder = orderQueue.get(0);
                if (currentOrder.getCount() <= 0) {
                    orderQueue.remove(0);
                    continue;
                }
                currentOrder.decrementCount();
                if (currentOrder.getCount() == 0) {
                    orderQueue.remove(0);
                }
            }

            if (currentOrder == null) {
                continue;
            }

            String itemName = currentOrder.getName();
            int itemCount = currentOrder.getCount();

            for (int i = 0; i < burners.length; i++) {


                synchronized (lock) {
                    if (!burners[i].equals("_")) {
                        continue;
                    }
                    burners[i] = itemName;
                    System.out.println(
                            Thread.currentThread().getName()
                                    + ": [" + (i + 1) + "]번 버너에서 " + itemName + " 요리 시작 (" + itemCount + "개 남음)");
                    showBurners();      //버너 상태 출력
                }

                // 요리 중인 시간을 10초로 가정
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock) {
                    burners[i] = "_";
                    System.out.println(
                            "                 "
                                    + Thread.currentThread().getName()
                                    + ": [" + (i + 1) + "]번 버너 사용완료");
                    showBurners();
                }
                if (itemCount == 0) {
                    System.out.println(itemName + " 요리 완료!");
                }
                break;
            }
            try {
                Thread.sleep(Math.round(1000 * Math.random())); // Random delay between tasks
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

        }
    }


    private void showBurners() {
        String stringToPrint
                = "                         ";
        for (int i = 0; i < burners.length; i++) {
            stringToPrint += (" " + burners[i]);
        }
        System.out.println(stringToPrint);
    }
}
