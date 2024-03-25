package org.example.lab;

public class Utils {
    public static int defaultLength = 3;

    public static double[][] createIdentityMatrix(int size) {
        double[][] identityMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            identityMatrix[i][i] = 1.0;
        }
        return identityMatrix;
    }


    // region multiply
    public static double[] multiply(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] * arr2[i];
        }
        return result;
    }

    public static double[] multiply(double[] arr1, double arg) {
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] * arg;
        }
        return result;
    }

    public static double[][] multiplyCols(double[][] source, double[] arg) {
        double[][] result = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = multiply(source[i], arg);
        }
        return result;
    }

    public static String[] multiplyString(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        String[] result = new String[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = "("+arr1[i] + "*" + arr2[i]+")";
        }
        return result;
    }
    // endregion multiply

    // region divide
    public static double[] divide(double[] arr1, double arg) {
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] / arg;
        }
        return result;
    }
    public static double[] divide(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] / arr2[i];
        }
        return result;
    }

    public static double[][] divideRow(double[][] source, int row, double arg) {
        for (int i = 0; i < source[row].length; i++) {
            source[row][i] = source[row][i] / arg;
        }
        return source;
    }

    public static double[][] divideRow(double[][] source, int row, double[] arg) {
        for (int i = 0; i < source[row].length; i++) {
            source[row][i] = source[row][i] / arg[i];
        }
        return source;
    }

    public static double[][] divideCol(double[][] source, int col, double arg) {
        double[][] result = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i][col] = source[i][col] / arg;
        }
        return result;
    }

    public static String[] divideString(double[] arr1, double[] arr2) {
        String[] result = new String[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = "("+arr1[i] + "/" + arr2[i]+")";
        }
        return result;
    }
    // endregion divide

    // region subtraction
    public static double[] subtraction(double[] arr1, double[] arr2) {
        if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] - arr2[i];
        }
        return result;
    }

    public static double[][] subtraction(double[][] source, double[][] arg) {
        double[][] result = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = subtraction(source[i], arg[i]);
        }
        return result;

    }

    public static double[] subtraction(double[] arr1, double arg){
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] - arg;
        }
        return result;
    }

    public static double[][] subtractionCols(double[][] source, double[] arg) {
        double[][] result = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            result[i] = subtraction(source[i], arg);
        }
        return result;
    }



    public static String[] subtractionString(double[] arr1, double[] arr2) {
        String[] result = new String[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] + "-" + arr2[i];
        }
        return result;
    }
    // endregion subtraction

    // region sum

    public static double sum(double[] arr) {
        double sum = 0;
        for (double v : arr) {
            sum += v;
        }
        return sum;
    }

    public static int sum(int[] arr) {
        int sum = 0;
        for (int v : arr) {
            sum += v;
        }
        return sum;
    }
    // endregion sum

    // region
    public static double[][] replaceRow (double[][] original, int row, double[] replacedRow){
        for (int i = 0; i < original[row].length; i++) {
            original[row][i] = replacedRow[i];
        }
        return original;
    }
    // endregion


    public static int maxLen(String str, int curMax) {
        return Math.max(curMax, str.length());
    }

    public static double[] getColumn(double[][] matrix, int columnIndex) {
        double[] column = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][columnIndex];
        }
        return column;
    }

    public static double[] getRow(double[][] matrix, int rowIndex) {
        return matrix[rowIndex];
    }

    public static double[] merge(double[] arr1, double[] arr2) {
        double[] merged = new double[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, merged, 0, arr1.length);
        System.arraycopy(arr2, 0, merged, arr1.length, arr2.length);
        return merged;
    }

    public static Integer findMinIndex(double[] arr) {
        if (arr == null) {
            return null;
        }
        int minIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }
}
