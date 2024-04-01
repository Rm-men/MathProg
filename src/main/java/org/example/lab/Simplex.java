package org.example.lab;

import static org.example.lab.FormatTextUtils.TextFormat.BOLD;
import static org.example.lab.FormatTextUtils.format;
import static org.example.lab.Utils.*;

public class Simplex {

    /**
     * A - матрица коэффициентов
     */
    double[][] coefficients;
    /**
     * b - свободные члены
     */
    double[] constants;
    /**
     * c - целевая функция
     */
    double[] objectiveFunctionCoefficients;

    SimplexMode simplexMode;

    double[] additionFunction;
    double[][] additionalMatrix;
    int numOfEquations;
    int numOfVariables;

    public Simplex(double[][] coefficients, double[] constants, double[] objectiveFunctionCoefficients, SimplexMode simplexMode) {
        if (simplexMode == SimplexMode.DOUBLE) {
            coefficients = invertMatrix(coefficients);
            double[] newConstants = constants;
            constants = objectiveFunctionCoefficients;
            objectiveFunctionCoefficients = newConstants;
            /*
            double minus = -1;
            coefficients = multiply(coefficients, minus);
            constants = multiply(constants, minus);*/
        }

        numOfEquations = constants.length;
        numOfVariables = coefficients[0].length;

        this.coefficients = coefficients;
        this.constants = constants;
        this.objectiveFunctionCoefficients = objectiveFunctionCoefficients;

        this.additionFunction = new double[numOfEquations];
        this.additionalMatrix = createIdentityMatrix(numOfEquations);

        this.simplexMode = simplexMode;


        System.out.println("\n" + "\t".repeat(numOfVariables + numOfEquations) + format(simplexMode.name, BOLD));
        SimplexTable firstSimplexTable = newStartSimplexTable();

        int tableIndex = 1;
        while (firstSimplexTable.isNotOptimal()) {
            firstSimplexTable = newSecondtSimplexTable(firstSimplexTable, tableIndex);
            tableIndex++;
            if (firstSimplexTable.allNegative()) {
                System.out.println("Все элементы меньше нуля, решение не существует");
                break;
            }
        }

    }


    public SimplexTable newStartSimplexTable() {
        double[] firstSolution = calculateSolution(additionFunction, coefficients, objectiveFunctionCoefficients);
        double[] secondSolution = calculateSolution(additionFunction, additionalMatrix, additionFunction);
        int minIndexSolution = findMinIndex(firstSolution);
        double[] minCol = getColumn(coefficients, minIndexSolution);
        double[] Q = divide(constants, minCol);

        SimplexTable first = new SimplexTable(this)
                .setTableName("Стартовая таблица")
                .setFirstMatrix(coefficients)
                .setSecondMatrix(additionalMatrix)
                .setConstants(constants)
                .setFirstSolution(firstSolution)
                .setSecondSolution(secondSolution)
                .setQ(Q)
                .construct();

        return first.printCollRowsTable();
    }

    public SimplexTable newSecondtSimplexTable(SimplexTable first, int tableIndex) {
        int minQIndex = first.targetQIndex;
        double[] minCol = first.minCol;
        int minColIndex = first.targetColIndexSolution;

        double divideNumber = first.firstMatrix[minQIndex][minColIndex];

        double[] multipleResult = multiply(minCol, first.minQ);
        double[] newConstants = subtraction(first.constants, multipleResult);

        newConstants[minQIndex] = first.constants[minQIndex] / divideNumber;

        double[][] newFirstMatrix = calculateNewMatrix(first.firstMatrix, first);
        double[][] newSecondMatrix = calculateNewMatrix(first.secondMatrix, first);

        // double[] newFirst = divideRow(newConstants, first.minQIndex, divideNumber);

        double[] newCi = first.Ci;
        newCi[minQIndex] = objectiveFunctionCoefficients[minColIndex];

        double[] newFirstSolution = calculateSolution(first.Ci, newFirstMatrix, objectiveFunctionCoefficients);
        double[] newSecondSolution = calculateSolution(first.Ci, newSecondMatrix, additionFunction);


        int minIndexSolution = findMinIndex(newFirstSolution);
        double[] minColSolution = getColumn(newFirstMatrix, minIndexSolution);

        double[] newQ = divide(newConstants, minColSolution);

        first.bi[minQIndex] = minColIndex + 1;

        SimplexTable secondSimplexTable = new SimplexTable(this)
                .setCi(newCi)
                .setTableName("Таблица " + (tableIndex))
                .setFirstMatrix(newFirstMatrix)
                .setSecondMatrix(newSecondMatrix)
                .setConstants(newConstants)
                .setFirstSolution(newFirstSolution)
                .setSecondSolution(newSecondSolution)
                .setQ(newQ)
                .setBi(first.bi)
                .construct();

        return secondSimplexTable.printCollRowsTable();
    }

    public double[][] buildSimplexTableau() {
        int numOfEquations = coefficients.length;
        int numOfVariables = coefficients[0].length;

        double[][] tableau = new double[numOfEquations + 1][numOfVariables + 1];

        for (int i = 0; i < numOfEquations; i++) {
            System.arraycopy(coefficients[i], 0, tableau[i], 0, numOfVariables);
            tableau[i][numOfVariables] = constants[i];
        }

        for (int j = 0; j < numOfVariables; j++) {
            tableau[numOfEquations][j] = -objectiveFunctionCoefficients[j];
        }
        tableau[numOfEquations][numOfVariables] = 0;

        return tableau;
    }

    public static double[] calculateSolution(double[] first, double[][] second, double[] addition) {
        int count = addition.length;
        double[] solutions = new double[count];
        for (int i = 0; i < count; i++) {
            solutions[i] = sum(multiply(first, getColumn(second, i))) - addition[i];
        }
        return solutions;
    }


    public static double[][] newSolution(double[][] firstMatrix, double[] minCol) {
        return subtraction(firstMatrix, multiplyCols(firstMatrix, minCol));
    }


    public static double[][] calculateNewMatrix(double[][] original, SimplexTable oldTable) {
        int colsSize = original[0].length;
        int rowsSize = original.length;
        int minQIndex = oldTable.targetQIndex;
        double[] minCol = oldTable.minCol;

        double[] minRowDivided = divide(original[minQIndex], minCol[minQIndex]);

        for (int row = 0; row < rowsSize; row++) {
            if (row != minQIndex) {
                for (int col = 0; col < colsSize; col++) {
                    original[row][col] -= minCol[row] * minRowDivided[col];
                }
            }
        }

        return replaceRow(original, minQIndex, minRowDivided);
    }


    public enum SimplexMode {
        DIRECT("Прямая задача \n"),
        DOUBLE("Двойственный симплекс\n");

        private final String name;

        SimplexMode(String name) {
            this.name = name;
        }
    }
}
