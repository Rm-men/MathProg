package org.example.lab;

import java.util.*;

public class Graph {
    // Метод для поиска кратчайшего пути в графе
    public static Map<Integer, Integer> shortestPath(Map<Integer, Map<Integer, Integer>> graph, int start, int end) {
        Map<Integer, Integer> distances = new HashMap<>(); // Хранит дистанции от стартовой вершины
        Map<Integer, Integer> previous = new HashMap<>(); // Хранит предыдущие вершины в кратчайшем пути
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get)); // Очередь с приоритетами

        // Инициализация
        for (Integer vertex : graph.keySet()) {
            if (vertex == start) {
                distances.put(vertex, 0);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
            }
            queue.add(vertex);
        }

        // Обработка графа
        while (!queue.isEmpty()) {
            Integer current = queue.poll();
            if (distances.get(current) == Integer.MAX_VALUE) break; // Все остальные недостижимы

            for (Map.Entry<Integer, Integer> neighborEntry : graph.get(current).entrySet()) {
                Integer neighbor = neighborEntry.getKey();
                Integer weight = neighborEntry.getValue();

                int newDistance = distances.get(current) + weight;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        // Формирование кратчайшего пути
        List<Integer> path = new ArrayList<>();
        int current = end;
        while (current != start) {
            path.add(current);
            current = previous.get(current);
        }
        path.add(start);
        Collections.reverse(path);

        Map<Integer, Integer> shortestPath = new HashMap<>();
        shortestPath.put(end, distances.get(end));
        for (int i = 1; i < path.size(); i++) {
            shortestPath.put(path.get(i), graph.get(path.get(i - 1)).get(path.get(i)));
        }

        return shortestPath;
    }

    public static void main(String[] args) {
        // Пример графа
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        graph.put(0, Map.of(1, 4, 2, 3));
        graph.put(1, Map.of(3, 2, 0, 2));
        graph.put(2, Map.of(1, 1, 3, 5));
        graph.put(3, Map.of(4, 3));
        graph.put(4, Map.of(1, 1));

        int startVertex = 0;
        int endVertex = 4;
        Map<Integer, Integer> shortestPath = shortestPath(graph, startVertex, endVertex);
        Integer sum = 0;

        // Вывод результатов
        System.out.println("Кратчайший путь от вершины " + startVertex + " к вершине " + endVertex + ":");
        if (startVertex < endVertex) {
            for (Map.Entry<Integer, Integer> entry : shortestPath.entrySet()) {
                System.out.println("Вершина " + entry.getKey() + ": " + entry.getValue());
                sum += entry.getValue();
            }
        } else {
            for (; startVertex >= endVertex; startVertex--) {
                if (Objects.nonNull(shortestPath.get(startVertex))) {
                    System.out.println("Вершина " + startVertex + ": " + shortestPath.values().stream().toList().get(startVertex));
                    sum += shortestPath.values().stream().toList().get(startVertex);
                }
            }
        }
        System.out.println("\nДлинна кратчайшего пути = " + sum);
    }
}
