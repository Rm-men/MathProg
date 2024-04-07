package org.example.lab;

import java.util.*;

import static org.example.lab.FormatTextUtils.TextFormat.*;
import static org.example.lab.FormatTextUtils.format;
import static org.example.lab.Simplex.SimplexMode.INT;
import static org.example.lab.Utils.*;
import static org.example.lab.Utils.generateArray;

public class TransportTable extends Table {
    // region Задаваемые параметры таблицы
    /**
     * Дополнительное количество строк по умолчанию
     */
    final int additionRows = 2;
    /**
     * Дополнительное количество столбцов по умолчанию
     */
    final int additionColumns = 2;

    // endregion

    // region Содержимое таблицы
    int[][] costs;
    int[] supply;
    int[] demand;
    // endregion

    int colSize;
    int rowSize;

    FormatTextUtils.TextFormat[][] rowColsChanged;

    int[][] result;

    TransportTable oldTable;

    TransportTable(int[][] costs, int[] supply, int[] demand, String tableName, int[][] result) {
        this.costs = costs;
        this.supply = supply;
        this.demand = demand;
        this.colSize = costs[0].length;
        this.rowSize = costs.length;
        this.tableName = tableName;
        this.result = result;
        construct();
        prepareTable();
    }

    TransportTable(int[][] costs, int[] supply, int[] demand, String tableName, int[][] result, TransportTable oldTable) {
        this.costs = costs;
        this.supply = supply;
        this.demand = demand;
        this.colSize = costs[0].length;
        this.rowSize = costs.length;
        this.tableName = tableName;
        this.result = result;
        this.oldTable = oldTable;
        construct();
        prepareTable();
    }

    public TransportTable construct() {
        this.rowCols = new String[rowSize + additionRows][colSize + additionColumns];
        this.rowColsChanged = new FormatTextUtils.TextFormat[rowSize + additionRows][colSize + additionColumns];
        if (oldTable == null) {
            rowCols[0][0] = "П/З = " + Arrays.stream(supply).sum() + "/" + Arrays.stream(demand).sum();
        }
        insertValues(costs, 1, 1);
        insertRow(generateArray(colSize), 0, 1);
        insertColumn(generateArray(rowSize), 1, 0);
        rowCols[0][colSize + 1] = "Запасы";
        insertColumn(supply, 1, colSize + 1);
        rowCols[rowSize + 1][0] = "Потребности";
        insertRow(demand, rowSize + 1, 1);
        return this;
    }

    public TransportTable prepareTable() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                if (costs[i][j] == Integer.MAX_VALUE && costs[i][j] != result[i][j]) {
                    if (oldTable != null) {
                        if (oldTable.rowColsChanged[i + additionRows - 1][j + additionColumns - 1] == null) {
                            rowColsChanged[i + 1][j + 1] = CYAN;
                        } else {
                            rowColsChanged[i + additionRows - 1][j + additionColumns - 1] = BLUE;
                        }
                        rowCols[i + 1][j + 1] = String.valueOf(result[i][j]);
                    }
                }
            }
        }
        return this;
    }

    public TransportTable printCollRowsTable() {
        System.out.println("\t".repeat(colSize) + format(tableName, BOLD));
        int numColumns = rowCols[0].length;
        int[] columnWidths = new int[numColumns];

        // Рассчитать ширину столбцов
        for (int i = 0; i < numColumns; i++) {
            int maxWidth = defaultLength;
            for (String[] row : rowCols) {
                row[i] = roundString(row[i], 3);
                String value = row[i];
                if (value != null) {
                    maxWidth = maxLen(value, maxWidth);
                }
            }
            columnWidths[i] = maxWidth;
        }

        for (int rowIndex = 0; rowIndex < rowCols.length; rowIndex++) {
            String[] row = rowCols[rowIndex];
            for (int colIndex = 0; colIndex < numColumns; colIndex++) {
                String format;
                // * Форматы под подсветку
                if (rowColsChanged[rowIndex][colIndex] != null) {
                    format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", rowColsChanged[rowIndex][colIndex]);
                } else {
                    format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ");
                }
                System.out.printf(format, row[colIndex]);
            }

            System.out.println("|");
            for (int i = 0; i < numColumns; i++) {
                System.out.print("|" + "-".repeat(columnWidths[i] + 2));
            }

            System.out.println("|");
        }

        System.out.print("\n".repeat(2));

        return this;
    }


    public String removeCharactersAfterDot(String input, int offset) {
        int dotIndex = input.indexOf('.');
        if (dotIndex != -1) {
            int endIndex = dotIndex + offset + 1;
            if (endIndex < input.length()) {
                return input.substring(0, endIndex);
            }
        }
        return input;
    }

    public void insertColumn(int[] source, int destRow, int destCol) {
        for (int i = 0; i < source.length; i++) {
            rowCols[destRow + i][destCol] = String.valueOf(source[i]);
        }
    }

    public void insertRow(int[] source, int destRow, int destCol) {
        for (int i = 0; i < source.length; i++) {
            rowCols[destRow][destCol + i] = String.valueOf(source[i]);
        }
    }

    public void insertValues(int[][] source, int destRow, int destColumn) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[0].length; j++) {
                if (destRow + i < rowCols.length && destColumn + j < rowCols[0].length) {
                    if (source[i][j] == Math.floor(source[i][j])) {
                        rowCols[destRow + i][destColumn + j] = String.valueOf((int) source[i][j]);
                    } else {
                        rowCols[destRow + i][destColumn + j] = String.valueOf(source[i][j]);
                    }
                }
            }
        }
    }
}
