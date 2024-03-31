package org.example;

import org.example.lab.Simplex;

import static org.example.lab.Simplex.SimplexMode.MAX;

public class Main {
  public static void main(String[] args) {

       /*   double[][] A = {
                {2, 3, 6},
                {4, 2, 4},
                {4, 6, 8}
        };
        double[] b = {240, 200, 160};
        double[] F = {4, 5, 4};
        Simplex simplex = new Simplex(A, b, F,  MAX);*/

        double[][] A = {
                {5, 1, 0, 2},
                {4, 2, 2, 1},
                {1, 0, 2, 1}
        };
        double[] b = {1000, 600, 150};
        double[] F = {6, 2, 2.5, 4};
        Simplex simplex = new Simplex(A, b, F, MAX);
    }
}