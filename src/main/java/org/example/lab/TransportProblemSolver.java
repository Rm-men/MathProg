package org.example.lab;

import java.util.Arrays;

public class TransportProblemSolver {

    //        int[][] costs = {
//                {5, 3, 1},
//                {3, 2, 4},
//                {4, 1, 2}
//        };
//        int[] supply = {10, 20, 30};
//        int[] demand = {15, 20, 25};

    //        int[][] costs = {
//                {4, 5, 3, 6},
//                {7, 2, 1, 5},
//                {6, 1, 4, 2}
//        };
//        int[] supply = {30, 25, 20};
//        int[] demand = {20, 15, 25, 20};

    public static void main(String[] args) {
        // Пример входных данных: стоимости перевозок и объемы поставок и спроса
//        int[][] costs = {
//                {5, 3, 1},
//                {3, 2, 4},
//                {4, 1, 2}
//        };
//        int[] supply = {10, 20, 30};
//        int[] demand = {15, 20, 25};

        int[][] costs = {
                {4, 5, 3, 6},
                {7, 2, 1, 5},
                {6, 1, 4, 2}
        };
        int[] supply = {30, 25, 20};
        int[] demand = {20, 15, 25, 20};

        TransportProblemSolver solver = new TransportProblemSolver();
        int[][] result = solver.solveTransportProblem(costs, supply, demand);

        System.out.println("Оптимальное распределение:");
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public int[][] solveTransportProblem(int[][] costs, int[] supply, int[] demand) {
        int m = supply.length; // количество поставщиков
        int n = demand.length; // количество потребителей
        int[][] result = new int[m][n]; // результирующая матрица

        // Заполняем базис методом минимального элемента
        while (true) {
            int[] minCostCell = findMinCostCell(costs); // находим ячейку с минимальной стоимостью
            int i = minCostCell[0];
            int j = minCostCell[1];
            if (costs[i][j] == Integer.MAX_VALUE) // если все ячейки уже определены
                break;
            int quantity = Math.min(supply[i], demand[j]); // определяем минимальное количество товара для перемещения
            result[i][j] = quantity;
            supply[i] -= quantity;
            demand[j] -= quantity;
            costs[i][j] = Integer.MAX_VALUE; // помечаем использованную ячейку
        }

        // Решаем задачу методом дифференциальных рент
        while (true) {
            int[] uv = computeUV(result, costs); // вычисляем u и v
            if (uv == null) // если не удалось вычислить, значит решение найдено
                break;
            int u[] = new int[m];
            int v[] = new int[n];
            for (int i = 0; i < m; i++) {
                u[i] = uv[i];
            }
            for (int j = 0; j < n; j++) {
                v[j] = uv[m + j];
            }
            int[] delta = computeDelta(u, v, costs); // вычисляем дельта
            int[] minDeltaCell = findMinDeltaCell(delta, costs); // находим ячейку с минимальной дельтой
            int i = minDeltaCell[0];
            int j = minDeltaCell[1];
            if (delta[i] == Integer.MAX_VALUE) // если дельты все положительные, значит решение найдено
                break;
            result[i][j] = Math.min(supply[i], demand[j]); // перемещаем товар
            supply[i] -= result[i][j];
            demand[j] -= result[i][j];
        }

        return result;
    }

    // Находим ячейку с минимальной стоимостью
    private int[] findMinCostCell(int[][] costs) {
        int[] cell = {0, 0};
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < costs.length; i++) {
            for (int j = 0; j < costs[i].length; j++) {
                if (costs[i][j] < minCost) {
                    minCost = costs[i][j];
                    cell[0] = i;
                    cell[1] = j;
                }
            }
        }
        return cell;
    }

    // Вычисляем u и v методом потенциалов
    private int[] computeUV(int[][] result, int[][] costs) {
        int m = result.length;
        int n = result[0].length;
        int[] u = new int[m];
        int[] v = new int[n];
        boolean[] markedCells = new boolean[m * n];
        Arrays.fill(u, Integer.MAX_VALUE);
        u[0] = 0;
        while (true) {
            boolean updated = false;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (result[i][j] != 0 && !markedCells[i * n + j]) {
                        if (u[i] != Integer.MAX_VALUE && v[j] == Integer.MAX_VALUE) {
                            v[j] = costs[i][j] - u[i];
                            markedCells[i * n + j] = true;
                            updated = true;
                        } else if (u[i] == Integer.MAX_VALUE && v[j] != Integer.MAX_VALUE) {
                            u[i] = costs[i][j] - v[j];
                            markedCells[i * n + j] = true;
                            updated = true;
                        }
                    }
                }
            }
            if (!updated)
                break;
        }
        for (int i = 0; i < m; i++) {
            if (u[i] == Integer.MAX_VALUE)
                return null; // не удалось вычислить u и v
        }
        return concatArrays(u, v);
    }

    // Вычисляем дельта
    private int[] computeDelta(int[] u, int[] v, int[][] costs) {
        int m = u.length;
        int n = v.length;
        int[] delta = new int[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                delta[i * n + j] = u[i] + v[j] - costs[i][j];
            }
        }
        return delta;
    }

    // Находим ячейку с минимальной дельтой
    private int[] findMinDeltaCell(int[] delta, int[][] costs) {
        int m = costs.length;
        int n = costs[0].length;
        int[] cell = {0, 0};
        int minDelta = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (delta[i * n + j] < minDelta) {
                    minDelta = delta[i * n + j];
                    cell[0] = i;
                    cell[1] = j;
                }
            }
        }
        return cell;
    }

    // Объединяем два массива
    private int[] concatArrays(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
