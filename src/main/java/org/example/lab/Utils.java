package org.example.lab;

public class Utils {
    public static int defaultLength = 3;

    public static void printCollRowsTable(String[][] collRows) {
        int numColumns = collRows[0].length;
        int[] columnWidths = new int[numColumns];

        // Рассчитать ширину столбцов
        for (int i = 0; i < numColumns; i++) {
            int maxWidth = defaultLength;
            for (String[] row : collRows) {
                String value = row[i];
                if (value != null) {
                    maxWidth = len(value);
                }
            }
            columnWidths[i] = maxWidth;
        }

        // Вывести таблицу
        for (String[] row : collRows) {
            for (int i = 0; i < numColumns; i++) {
                String format = "| %" + Math.max(columnWidths[i], defaultLength) + "s ";
                if (row[i] != null) {
                    if (row[i].matches("^-?\\d+\\.\\d+$") && Double.parseDouble(row[i]) == (int) Double.parseDouble(row[i])) {
                        System.out.printf(format, (int)(Double.parseDouble(row[i])));
                    } else
                        System.out.printf(format, row[i]);
                } else {
                    System.out.printf(format, "");
                }
            }

            System.out.println("|");
            for (int i = 0; i < numColumns; i++) {
                System.out.print("|" + "-".repeat(columnWidths[i] + 2));
            }

            System.out.println("|");
        }
    }

    public static int len(String str) {
        return Math.max(defaultLength, str.length());
    }

    public static void insertValues(double[][] source, String[][] destination, int destRow, int destColumn) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[0].length; j++) {
                if (destRow + i < destination.length && destColumn + j < destination[0].length) {
                    if (source[i][j] == Math.floor(source[i][j])) {
                        destination[destRow + i][destColumn + j] = String.valueOf((int) source[i][j]);
                    } else {
                        destination[destRow + i][destColumn + j] = String.valueOf(source[i][j]);
                    }
                }
            }
        }
    }

    public static double[][] createIdentityMatrix(int size) {
        double[][] identityMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            identityMatrix[i][i] = 1.0;
        }
        return identityMatrix;
    }
}
