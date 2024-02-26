package org.example.lab;

import static org.example.lab.Simplex.calculateSolution;
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
    // endregion

    // region Содержимое таблицы
    double[][] firstMatrix;
    double[][] secondMatrix;
    double[] constants;
    // endregion

    // region Вычисляемые результаты таблицы
    double[] objectiveFunctionCoefficients;
    double[] additionalVariables;
    double[] mergedArray;
    // endregion

    String[][] rowCols;
    Simplex simplex;

    public double[] solutions;
    public double[] solutionsAddition;
    public double[] Q;
    public double resultF;
    public double[] Ci;

    SimplexTable(Simplex simplex) {
        this.simplex = simplex;
        this.objectiveFunctionCoefficients = simplex.objectiveFunctionCoefficients;
        this.additionalVariables = new double[simplex.numOfEquations];
        this.mergedArray = merge(objectiveFunctionCoefficients, additionalVariables);
        this.Ci = new double[simplex.numOfEquations];
    }

    public SimplexTable construct(){
        int countAnotherColl = 4;
        int rows = simplex.numOfEquations + 3;
        int columns = simplex.numOfVariables * 2 + countAnotherColl;
        this.rowCols = new String[rows][columns];
        // * Подготовка строк
        rowCols[0][0] = " ";
        rowCols[0][1] = "Ci";
        rowCols[0][2] = " ";

        // Прописывание значений функции и нулей
        for (int i = 0; i < simplex.numOfVariables; i++) {
            rowCols[0][i + countAnotherColl - 1] = String.valueOf(objectiveFunctionCoefficients[i]);
            rowCols[0][i + simplex.numOfVariables + countAnotherColl - 1] = String.valueOf(Ci[i]);
            rowCols[1][i + countAnotherColl - 1] = "X" + (i + 1);
            rowCols[1][i + simplex.numOfEquations + countAnotherColl - 1] = "X" + (i + simplex.numOfEquations + 1);
        }

        //  * Подготовка столбцов
        rowCols[1][0] = "Ci";
        insertColumn(additionalVariables, 2, 0);
        rowCols[1][1] = "bi";

        // Столбец с X
        int colOffset = 2;
        for (int i = 0; i < simplex.numOfEquations; i++) {
            rowCols[i + colOffset][1] = "X" + (i + simplex.numOfEquations + 1);
        }

        rowCols[rows - 1][1] = "di";

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
        resultF = sum(multiply(additionalVariables, constants));
        rowCols[rows - 1][2] = String.valueOf(resultF);

        // * Последняя строка решений
        solutions = calculateSolution(additionalVariables, firstMatrix, objectiveFunctionCoefficients);
        insertRow(solutions,  rows - 1, startColumn);

        solutionsAddition = calculateSolution(additionalVariables, secondMatrix, additionalVariables);
        insertRow(solutionsAddition,  rows - 1, startColumn + simplex.numOfEquations);

        int minIndexSolution = findMinIndex(solutions);

        Q = divide(constants, getColumn(firstMatrix, minIndexSolution));

        rowCols[1][columns - 1] = "Q";
        insertColumn(Q, 2, columns - 1);
        return this;
    }

    public SimplexTable printCollRowsTable() {
        int numColumns = rowCols[0].length;
        int[] columnWidths = new int[numColumns];

        // Рассчитать ширину столбцов
        for (int i = 0; i < numColumns; i++) {
            int maxWidth = defaultLength;
            for (String[] row : rowCols) {
                String value = row[i];
                if (value != null) {
                    maxWidth = len(value);
                }
            }
            columnWidths[i] = maxWidth;
        }

        // Вывести таблицу
        for (String[] row : rowCols) {
            for (int i = 0; i < numColumns; i++) {
                String format = "| %" + Math.max(columnWidths[i], defaultLength) + "s ";
                if (row[i] != null) {
                    if (row[i].matches("^-?\\d+\\.\\d+$") && Double.parseDouble(row[i]) == (int) Double.parseDouble(row[i])) {
                        System.out.printf(format, (int) (Double.parseDouble(row[i])));
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
        return this;
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

    public SimplexTable setFirstMatrix(double[][] firstMatrix){
        this.firstMatrix = firstMatrix;
        return this;
    }

    public SimplexTable setSecondMatrix(double[][] secondMatrix){
        this.secondMatrix = secondMatrix;
        return this;
    }

    public SimplexTable setConstants(double[] constants){
        this.constants = constants;
        return this;
    }
}
