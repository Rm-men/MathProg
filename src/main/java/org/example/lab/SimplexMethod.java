package org.example.lab;

import static org.example.lab.Utils.*;

public class SimplexMethod {

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
    int numOfEquations;
    int numOfVariables;

    public SimplexMethod(double[][] coefficients, double[] constants, double[] objectiveFunctionCoefficients) {
        this.coefficients = coefficients;
        this.constants = constants;
        this.objectiveFunctionCoefficients = objectiveFunctionCoefficients;

        numOfEquations = coefficients.length;
        numOfVariables = coefficients[0].length;
        // Шаг 1: Приведение задачи к стандартной форме
        // Добавление дополнительных переменных и уравнений при необходимости
        // Приведение неравенств к равенствам
        // Приведение задачи к стандартной форме
        // convertToStandardForm(coefficients, constants, objectiveFunctionCoefficients);


        // Шаг 2: Инициализация начального базисного плана
        // Инициализация начального базисного плана
        // Выбор начального базиса
        // Вычисление соответствующих базисных переменных

        // Шаг 3: Проверка оптимальности текущего базисного плана
        // Проверка оптимальности текущего базисного плана
        // Проверка коэффициентов целевой функции
        // Возвращение true, если текущий базисный план оптимален, иначе false

        // Шаг 4: Поиск ведущей строки и столбца
        // Поиск ведущей строки и столбца
        // Выбор ведущего элемента для входящей переменной
        // Возвращение индексов ведущей строки и столбца

        // Шаг 5: Пересчет базисного плана
        // Пересчет базисного плана
        // Обновление базисных переменных и соответствующих значений


        // Шаг 6: Повторение шагов 3-5 до достижения оптимального решения или обнаружения отсутствия решения

        // Проверка наличия решения и другие возможные случаи
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

    public double[][] buildSimplexTableau() {
        int numOfEquations = coefficients.length;
        int numOfVariables = coefficients[0].length;

        double[][] tableau = new double[numOfEquations + 1][numOfVariables + 1];

        for (int i = 0; i < numOfEquations; i++) {
            for (int j = 0; j < numOfVariables; j++) {
                tableau[i][j] = coefficients[i][j];
            }
            tableau[i][numOfVariables] = constants[i];
        }

        for (int j = 0; j < numOfVariables; j++) {
            tableau[numOfEquations][j] = -objectiveFunctionCoefficients[j];
        }
        tableau[numOfEquations][numOfVariables] = 0;

        return tableau;
    }

    public void printSimplexTableau() {
        int countAnotherColl = 4;
        int rows = numOfEquations + 3;
        int columns = numOfVariables * 2 + countAnotherColl;
        String[][] collRows = new String[rows][columns];
        // * Подготовка строк
        collRows[0][0] = " ";
        collRows[0][1] = "Ci";
        collRows[0][2] = " ";

        // Прописывание значений функции и нулей
        for (int countEquation = 0; countEquation < numOfVariables; countEquation++) {
            collRows[0][countEquation + countAnotherColl - 1] = String.valueOf(objectiveFunctionCoefficients[countEquation]);
            collRows[0][countEquation + numOfVariables + countAnotherColl - 1] = "0";
            collRows[1][countEquation + countAnotherColl - 1] = "X" + (countEquation + 1);
            collRows[1][countEquation + numOfEquations + countAnotherColl - 1] = "X" + (countEquation + numOfEquations + 1);
        }

        //  * Подготовка столбцов
        collRows[1][0] = "Ci";
        collRows[2][0] = "0";
        collRows[3][0] = "0";
        collRows[4][0] = "0";

        collRows[1][1] = "bi";

        // Столбец с X
        int colOffset = 2;
        for (int i = 0; i < numOfEquations; i++) {
            collRows[i + colOffset][1] = "X" + (i + numOfEquations + 1);
        }

        collRows[rows - 1][1] = "di";

        // Столбец со свободными членами
        for (int curIndex = 0; curIndex < numOfEquations; curIndex++) {
            collRows[2 + curIndex][2] = String.valueOf(constants[curIndex]);
        }

        int startRow = 2;
        int startColumn = 3;
        // * Вписать матрицу коэффициентов
        insertValues(coefficients, collRows, startRow, startColumn);

        // * Вписать единичную матрциу
        double[][] oneMatrix = createIdentityMatrix(numOfVariables);
        insertValues(oneMatrix, collRows, startRow, startColumn+numOfVariables);


        printCollRowsTable(collRows);
    }
}
