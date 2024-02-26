package org.example;

import org.example.lab.SimplexMethod;

public class Main {
    public static void main(String[] args) {
        double[][] A = {
                {2, 3, 6},
                {4, 2, 4},
                {4, 6, 8}
        };
        double[] b = {240, 200, 160};
        double[] F = {4, 5, 4};
        SimplexMethod simplexMethod = new SimplexMethod(A, b, F);
        var result = simplexMethod.buildSimplexTableau();
        simplexMethod.printSimplexTableau();
    }
}