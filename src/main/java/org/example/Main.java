package org.example;

import org.example.lab.Simplex;

import static org.example.lab.Simplex.SimplexMode.*;
import static org.example.lab.Utils.invertMatrix;

public class Main {
    public static void main(String[] args) {

        // * Прямая
/*        double[][] A = {
                {2, 3, 6},
                {4, 2, 4},
                {4, 6, 8}
        };
        double[] b = {240, 200, 160};
        double[] F = {4, 5, 4};
        Simplex simplexDIRECT = new Simplex(A, b, F,  DIRECT);
*/
        //  Прямая INT
//        double[][] A = {
//                {2, 3, 6},
//                {4, 2, 4},
//                {4, 6, 8}
//        };
//        double[] b = {240, 200, 160};
//        double[] F = {4, 5, 4};
//        Simplex simplexDIRECT = new Simplex(A, b, F,  INT);

        // * Обратная
/*
        double[][] A2 = {
                {2, 3, 6},
                {4, 2, 4},
                {4, 6, 8}
        };
        double[] b2 = {240, 200, 160};
        double[] F2 = {4, 5, 4};
        Simplex simplexDOUBLE = new Simplex(invertMatrix(A2), F2, b2,  DOUBLE);
*/

        // * Не одно решение
        // Прямая
/*                double[][] A = {
                {-2, 3},
                {1, -1},
                {1, 1}
        };
        double[] b = {9, 2, 8};
        double[] F = {1, -1};
        Simplex simplex = new Simplex(A, b, F, DIRECT);*/

        // Обратная
/*        double[][] A2 = {
                {-2, 3},
                {1, -1},
                {1, 1}
        };
        double[] b2 = {9, 2, 8};
        double[] F2 = {1, -1};
        Simplex simplexDOUBLE = new Simplex(invertMatrix(A2), F2, b2,  DOUBLE);*/

        // * Нот квадратиш
/*        double[][] A = {
                {5, 1, 0, 2},
                {4, 2, 2, 1},
                {1, 0, 2, 1}
        };
        double[] b = {1000, 600, 150};
        double[] F = {6, 2, 2.5, 4};
        Simplex simplex = new Simplex(A, b, F, DIRECT);*/


       // *V1
/*         double[][] A = {
                {2, 8, 3, 4},
                {-1, 3, 1, 4},
                {5, 1, 2, 4}
        };
        double[] b = {5, 4, 24};
        double[] F = {3, -1, 8, 2};
        Simplex simplex = new Simplex(A, b, F, DIRECT);*/

        // *V1 INT
//        double[][] A = {
//                {2, 8, 3, 4},
//                {-1, 3, 1, 4},
//                {5, 1, 2, 4}
//        };
//        double[] b = {5, 4, 24};
//        double[] F = {3, -1, 8, 2};
//        Simplex simplex = new Simplex(A, b, F, INT);

//        double[][] A = {
//                {4, 7},
//                {8, 3},
//                {9, 5}
//        };
//        double[] b = {49, 46, 70};
//        double[] F = {6, 5};
//        Simplex simplex = new Simplex(A, b, F, INT);

       // *V3
/*         double[][] A = {
                {2, 8, 3, 4},
                {-1, 3, 1, 4},
                {5, 1, 2, 4}
        };
        double[] b = {5, 4, 24};
        double[] F = {3, -1, 8, 2};
        Simplex simplex = new Simplex(A, b, F, DIRECT);*/


    }
}