package org.example.lab;

import java.util.Arrays;

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

    double[] additionFunction;
    double[][] additionalMatrix;
    int numOfEquations;
    int numOfVariables;

    public Simplex(double[][] coefficients, double[] constants, double[] objectiveFunctionCoefficients, SimplexMode simplexMode) {
        numOfEquations = constants.length;
        numOfVariables = coefficients[0].length;

        this.coefficients = coefficients;
        this.constants = constants;
        this.objectiveFunctionCoefficients = objectiveFunctionCoefficients;

        this.additionFunction = new double[numOfEquations];
        this.additionalMatrix = createIdentityMatrix(numOfVariables);

        SimplexTable firstSimplexTable = newStartSimplexTable();

        int tableIndex = 1;
        while (firstSimplexTable.isNotOptimal()) {
            firstSimplexTable = newSecondtSimplexTable(firstSimplexTable, tableIndex);
            tableIndex++;
        }
        firstSimplexTable.printSolution();

    }

    public void convertToStandardForm(double[][] coefficients, double[] constants, double[] objectiveFunctionCoefficients) {
        int numOfEquations = coefficients.length; // количество уравнений
        int numOfVariables = coefficients[0].length; // количество переменных

        // Добавление дополнительных переменных и уравнений при необходимости
        for (int equationIndex = 0; equationIndex < numOfEquations; equationIndex++) {
            if (constants[equationIndex] < 0) {
                // умножение уравнения на -1, если свободный член отрицателен
                for (int variableIndex = 0; variableIndex < numOfVariables; variableIndex++) {
                    coefficients[equationIndex][variableIndex] *= -1;
                }
                constants[equationIndex] *= -1;
            }
        }

        // Приведение неравенств к равенствам
        for (int equationIndex = 0; equationIndex < numOfEquations; equationIndex++) {
            if (constants[equationIndex] != 0) {
                // деление уравнения на свободный член, чтобы он стал равен 1
                for (int variableIndex = 0; variableIndex < numOfVariables; variableIndex++) {
                    coefficients[equationIndex][variableIndex] /= constants[equationIndex];
                }
                constants[equationIndex] = 1;
            }
        }

        // Приведение задачи к стандартной форме
        for (int equationIndex = 0; equationIndex < numOfEquations; equationIndex++) {
            // вычитание одного уравнения из другого
            for (int variableIndex = 0; variableIndex < numOfEquations; variableIndex++) {
                if (equationIndex != variableIndex) {
                    for (int k = 0; k < numOfVariables; k++) {
                        coefficients[equationIndex][k] -= coefficients[variableIndex][k];
                    }
                    constants[equationIndex] -= constants[variableIndex];
                }
            }
        }
    }

    public void initializeBasicFeasibleSolution(double[][] coefficients, double[] constants, double[] objectiveFunctionCoefficients) {
        // Шаг 2: Инициализация начального базисного плана
        // Инициализация начального базисного плана
        double[] basicSolution = new double[numOfVariables];
        for (int i = 0; i < numOfVariables; i++) {
            basicSolution[i] = 0; // начальное значение базисного плана
        }

        // Выбор начального базиса (просто первые numOfEquations переменных)
        int[] basicVariables = new int[numOfEquations];
        for (int i = 0; i < numOfEquations; i++) {
            basicVariables[i] = i; // просто выбираем первые numOfEquations переменных в качестве базисных
        }

        // Вычисление соответствующих базисных переменных (просто свободные переменные)
        double[] basicVariableValues = new double[numOfEquations];
        for (int i = 0; i < numOfEquations; i++) {
            basicVariableValues[i] = constants[i]; // просто присваиваем значения свободных переменных в качестве базисных
        }
    }

    public boolean isOptimalSolution(double[] objectiveFunctionCoefficients) {
        // Шаг 3: Проверка оптимальности текущего базисного плана
        // Проверка оптимальности текущего базисного плана
        // Проверка коэффициентов целевой функции
        // Возвращение true, если текущий базисный план оптимален, иначе false
        boolean isOptimal = true;
        for (int i = 0; i < numOfVariables; i++) {
            if (objectiveFunctionCoefficients[i] < 0) {
                isOptimal = false;
                break;
            }
        }
        return isOptimal;
    }

    public int[] findPivotElement(double[][] tableau) {
        // Шаг 4: Поиск ведущей строки и столбца
        // Поиск ведущей строки и столбца
        // Выбор ведущего элемента для входящей переменной
        // Возвращение индексов ведущей строки и столбца
        int pivotRow = 0;
        int pivotColumn = 0;
        double minRatio = Double.MAX_VALUE;

        for (int i = 1; i < numOfVariables + 1; i++) {
            if (tableau[numOfEquations][i] < minRatio) {
                minRatio = tableau[numOfEquations][i];
                pivotColumn = i;
            }
        }

        for (int i = 0; i < numOfEquations; i++) {
            double currentRatio = tableau[i][numOfVariables] / tableau[i][pivotColumn];
            if (currentRatio > 0 && currentRatio < minRatio) {
                minRatio = currentRatio;
                pivotRow = i;
            }
        }

        return new int[]{pivotRow, pivotColumn};
    }

    public void updateBasicSolution(double[][] tableau, int pivotRow, int pivotColumn) {
        // Шаг 5: Пересчет базисного плана
        // Пересчет базисного плана
        // Обновление базисных переменных и соответствующих значений
        double pivotElement = tableau[pivotRow][pivotColumn];
        for (int i = 0; i < numOfEquations + 1; i++) {
            for (int j = 0; j < numOfVariables + 1; j++) {
                if (i != pivotRow && j != pivotColumn) {
                    tableau[i][j] -= tableau[pivotRow][j] * tableau[i][pivotColumn] / pivotElement;
                }
            }
        }
        for (int i = 0; i < numOfEquations + 1; i++) {
            if (i != pivotRow) {
                tableau[i][pivotColumn] = 0;
            }
        }
        for (int j = 0; j < numOfVariables + 1; j++) {
            if (j != pivotColumn) {
                tableau[pivotRow][j] /= pivotElement;
            }
        }
        tableau[pivotRow][pivotColumn] = 1;
    }

    public SimplexTable newStartSimplexTable() {
        double[] firstSolution = calculateSolution(additionFunction, coefficients, objectiveFunctionCoefficients);
        double[] secondSolution = calculateSolution(additionFunction, additionalMatrix, additionFunction);
        int minIndexSolution = findMinIndex(firstSolution);
        double[] minCol = getColumn(coefficients, minIndexSolution);
        double[] Q = divide(constants, minCol);

        SimplexTable first = new SimplexTable(this)
                .setTableName("Start table")
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
        int minQIndex = first.minQIndex;
        double[] minCol = first.minCol;
        int minColIndex = first.minColIndexSolution;

        double divideNumber = first.firstMatrix[minQIndex][minColIndex];

        double[] multipleResult = multiply(minCol, first.minQ);
        double[] newConstants = subtraction(first.constants, multipleResult);

        newConstants[minQIndex] = first.constants[minQIndex] /divideNumber;

        double[][] newFirstMatrix =  calculateNewMatrix(first.firstMatrix, first);
        double[][] newSecondMatrix = calculateNewMatrix(first.secondMatrix, first);

        // double[] newFirst = divideRow(newConstants, first.minQIndex, divideNumber);

        double[] newCi = first.Ci;
        newCi[minQIndex] = objectiveFunctionCoefficients[minColIndex];

        double[] newFirstSolution = calculateSolution(first.Ci, newFirstMatrix, objectiveFunctionCoefficients);
        double[] newSecondSolution = calculateSolution(first.Ci, newSecondMatrix, additionFunction);


        int minIndexSolution = findMinIndex(newFirstSolution);
        double[] minColSolution = getColumn(newFirstMatrix, minIndexSolution);

        int newMin = findMinIndex(newFirstSolution);

        double[] newQ = divide(newConstants, minColSolution);

        first.bi[minQIndex] = minColIndex+1;

        SimplexTable secondSimplexTable = new SimplexTable(this)
                .setCi(newCi)
                .setTableName("Table " + (tableIndex))
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
        int colsSize = oldTable.simplex.numOfVariables;
        int rowsSize = oldTable.simplex.numOfEquations;
        int minQIndex = oldTable.minQIndex;
        double[] minCol = oldTable.minCol;

        double[] minRowDivided = divide(original[minQIndex], minCol[minQIndex]);

        for (int row = 0; row < rowsSize; row++) {
            if (row != minQIndex) {
                for (int col = 0; col < colsSize; col++) {
                    if (col < minQIndex) {
                        original[row][col] -= minCol[row] * minRowDivided[col];
                    } else if (col > minQIndex) {
                        original[row][col] -= minCol[row] * minRowDivided[col - 1];
                    }
                }
            }
        }

        return replaceRow(original, minQIndex, minRowDivided);
    }


    public enum SimplexMode{
        MAX, MIN
    }
}
