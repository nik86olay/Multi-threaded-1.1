package org.example;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int numberRoutesStreams = 1000;
    public static int counterThreads = 0;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(numberRoutesStreams);

        for (int i = 0; i < numberRoutesStreams; i++) {
            threadPool.submit(Main::addResult);
        }
        threadPool.shutdown();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            return;
        }

        Map.Entry<Integer, Integer> maxValueKey = sizeToFreq.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        System.out.println("Самое частое количество повторений " + maxValueKey.getKey() + " (встретилось " + maxValueKey.getValue() + " раз)");
        System.out.println("Другие примеры: ");
        sizeToFreq.forEach((a, b) -> System.out.println("- " + a + " (" + b + " раз)"));
        System.out.println(counterThreads);
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        System.out.println(route);
        return route.toString();
    }

    public static void addResult() {
        Integer i = (int) generateRoute("RLRFR", 100).chars().filter(ch -> ch == 'R').count();
        synchronized (sizeToFreq) {
            if (sizeToFreq.get(i) != null) sizeToFreq.put(i, sizeToFreq.remove(i) + 1);
            else {
                sizeToFreq.put(i, 1);
            }
            counterThreads++;
        }
    }
}