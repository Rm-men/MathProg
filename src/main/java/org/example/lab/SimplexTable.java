package org.example.lab;

import java.util.HashMap;
import java.util.Map;

import static org.example.lab.FormatTextUtils.TextFormat.*;
import static org.example.lab.FormatTextUtils.format;
import static org.example.lab.Utils.*;

public class SimplexTable {
    // region Задаваемые параметры таблицы
    /**
     * Дополнительное количество строк по умолчанию
     */
    final int additionRows = 2;
    /**
     * Дополнительное количество столбцов по умолчанию
     */
    final int additionColumns = 3;

    public String tableName;
    // endregion

    // region Содержимое таблицы
    double[][] firstMatrix;
    double[][] secondMatrix;
    double[] constants;
    // endregion

    // region Вычисляемые результаты таблицы
    double[] objectiveFunctionCoefficients;
    double[] additionalVariables;
    Integer targetQIndex;
    Integer targetColIndexSolution;
    // endregion

    String[][] rowCols;
    Simplex simplex;

    public double[] firstSolution;
    public double[] secondSolution;
    public double[] Q;
    public double resultF;
    public double[] Ci;
    public double[] nullCols;
    public int[] bi;
    /**
     * Смещение столбцов
     */
    int colOffset = 2;
    int colSize;
    int rowSize;


    int minRowIndexSolution;
    double[] minCol;
    double minQ;

    SimplexTable(Simplex simplex) {
        this.simplex = simplex;
        this.objectiveFunctionCoefficients = simplex.objectiveFunctionCoefficients;
        this.additionalVariables = new double[simplex.numOfEquations];
        this.Ci = new double[simplex.numOfEquations];
        this.nullCols = new double[simplex.numOfEquations];

        bi = new int[simplex.numOfEquations];
        for (int i = 0; i < simplex.numOfEquations; i++) {
            bi[i] = (i + simplex.numOfVariables + 1);
        }
    }

    public SimplexTable construct() {
        int countAnotherColl = 4;
        rowSize = simplex.numOfEquations + 3;
        colSize = simplex.numOfVariables + simplex.numOfEquations + countAnotherColl;
        this.rowCols = new String[rowSize][colSize];
        // * Подготовка строк
        rowCols[0][0] = " ";
        rowCols[0][1] = "Ci";
        rowCols[0][2] = " ";

        // Прописывание значений функции и нулей
        for (int i = 0; i < simplex.numOfVariables; i++) {
            rowCols[0][i + countAnotherColl - 1] = String.valueOf(objectiveFunctionCoefficients[i]);
            rowCols[1][i + countAnotherColl - 1] = "X" + (i + 1);
        }

        for (int i = 0; i < simplex.numOfEquations; i++) {
            rowCols[0][i + simplex.numOfVariables + countAnotherColl - 1] = String.valueOf(nullCols[i]);
            rowCols[1][i + simplex.numOfVariables + countAnotherColl - 1] = "X" + (simplex.numOfVariables + i + 1);
        }

        //  * Подготовка столбцов
        rowCols[1][0] = "Ci";
        insertColumn(Ci, 2, 0);
        rowCols[1][1] = "bi";

        // *Столбец с bi
        updateBi(bi);

        rowCols[rowSize - 1][1] = "di";

        // Столбец со свободными членами
        for (int curIndex = 0; curIndex < simplex.numOfEquations; curIndex++) {
            rowCols[2 + curIndex][2] = String.valueOf(constants[curIndex]);
        }

        int startRow = 2;
        int startColumn = 3;
        // * Вписать матрицу коэффициентов
        insertValues(firstMatrix, startRow, startColumn);

        // * Вписать единичную матрциу
        insertValues(secondMatrix, startRow, startColumn + simplex.numOfVariables);

        // * Высчитать для целевой функции


        // * Последняя строка решений
        if (firstSolution != null && secondSolution != null) {
            resultF = sum(multiply(Ci, constants));
            rowCols[rowSize - 1][2] = String.valueOf(resultF);
            insertRow(firstSolution, rowSize - 1, startColumn);
            // minIndexFirstSolution = findMaxModuleIndex(firstSolution);
            insertRow(secondSolution, rowSize - 1, startColumn + simplex.numOfVariables);
            // minIndexSecondSolution = findMaxModuleIndex(secondSolution);
        }


        rowCols[1][colSize - 1] = "Q";
        if (Q != null) {
            targetQIndex = findMinPositiveIndex(Q);
            insertColumn(Q, 2, colSize - 1);
        }

        if (firstSolution != null) {
            int minIndexSolution = findMinIndex(firstSolution);
            if (firstSolution[minIndexSolution] < 0) {
                targetColIndexSolution = minIndexSolution;
                minCol = getColumn(simplex.coefficients, targetColIndexSolution);
            }
        }
        if (Q != null) {
            minRowIndexSolution = findMinIndex(Q);
            minQ = Q[minRowIndexSolution];
        }


        return this;
    }

    private void updateBi(int[] newBi) {
        bi = newBi;
        for (int i = 0; i < newBi.length; i++) {
            rowCols[i + colOffset][1] = "X" + (bi[i]);
        }
    }

    public SimplexTable printCollRowsTable() {
        System.out.println(format(tableName, BOLD));
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
                String format = "| %" + Math.max(columnWidths[colIndex], defaultLength) + "s ";
                // * Форматы под подсветку
                if (isNotOptimal()) {
                    boolean isQColour = targetQIndex != null &&
                            rowIndex == targetQIndex + colOffset && colIndex == colSize - 1;
                    boolean isSolutionColour = targetColIndexSolution != null &&
                            rowIndex == rowSize - 1 && colIndex == targetColIndexSolution + colOffset + 1;
                    if (isQColour || isSolutionColour) {
                        format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", ORANGE);
                    } else {
                        boolean oldX = targetQIndex != null &&
                                rowIndex == targetQIndex + colOffset && colIndex == colOffset - 1;
                        if (oldX) {
                            format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", RED);
                        }
                        boolean newX = targetColIndexSolution != null &&
                                rowIndex == colOffset - 1 && colIndex == targetColIndexSolution + colOffset + 1;
                        if (newX) {
                            format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", BLUE);
                        }
                    }
                } else {
                    if (simplex.simplexMode.equals(Simplex.SimplexMode.INT)) {
                        Map map = new HashMap();
                        for (int i = 0; i < simplex.objectiveFunctionCoefficients.length; i++) {
                            map.put(i + 1, 0);
                        }
                        for (int i = 0; i < simplex.constants.length; i++) {
                            if (map.get(bi[i]) != null) {
                                map.remove(bi[i]);
                                map.put(bi[i], simplex.objectiveFunctionCoefficients[bi[i] - 1]);
                            }
                        }


                    }
                    if (rowIndex != rowSize - 1 && colIndex == colOffset) {
                        format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", GREEN);
                    } else if (rowIndex == rowSize - 1 && colIndex == colOffset) {
                        format = "| " + format("%" + Math.max(columnWidths[colIndex], defaultLength) + "s ", CYAN);
                    }
                }
                System.out.printf(format, row[colIndex]);
            }

            System.out.println("|");
            for (int i = 0; i < numColumns; i++) {
                System.out.print("|" + "-".repeat(columnWidths[i] + 2));
            }

            System.out.println("|");
        }
        // printF();

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

    public void insertColumn(double[] source, int destRow, int destCol) {
        for (int i = 0; i < source.length; i++) {
            rowCols[destRow + i][destCol] = String.valueOf(source[i]);
        }
    }

    public void insertRow(double[] source, int destRow, int destCol) {
        for (int i = 0; i < source.length; i++) {
            rowCols[destRow][destCol + i] = String.valueOf(source[i]);
        }
    }

    public void insertValues(double[][] source, int destRow, int destColumn) {
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

    public SimplexTable setBi(int[] newBi) {
        this.bi = newBi;
        return this;
    }

    public SimplexTable setFirstMatrix(double[][] firstMatrix) {
        this.firstMatrix = firstMatrix;
        return this;
    }

    public SimplexTable setSecondMatrix(double[][] secondMatrix) {
        this.secondMatrix = secondMatrix;
        return this;
    }

    public SimplexTable setConstants(double[] constants) {
        this.constants = constants;
        return this;
    }

    public SimplexTable setFirstSolution(double[] firstSolution) {
        this.firstSolution = firstSolution;
        return this;
    }

    public SimplexTable setSecondSolution(double[] secondSolution) {
        this.secondSolution = secondSolution;
        return this;
    }

    public SimplexTable setQ(double[] Q) {
        this.Q = Q;
        return this;
    }

    public SimplexTable setCi(double[] Ci) {
        this.Ci = Ci;
        return this;
    }

    public SimplexTable setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public boolean isNotOptimal() {
        for (double solution : firstSolution) {
            if (solution < 0) {
                return true;
            }
        }
        return false;
    }

    public boolean allNegative() {
        for (double solution : firstSolution) {
            if (solution >= 0) {
                return false;
            }
        }
        return true;
    }


    public void printF() {
        double result = 0;
        for (int i = 0; i < simplex.numOfEquations; i++) {
            result += simplex.objectiveFunctionCoefficients[i] * constants[i];
        }
        System.out.println("F = " + roundString(String.valueOf(Q[0]), 3));
    }
}
