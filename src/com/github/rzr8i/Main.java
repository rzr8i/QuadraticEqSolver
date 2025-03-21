package com.github.rzr8i;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String input;
    QuadraticEquation eq;
    double roots[];

    while (true) {
      System.out.print("Enter equation(0 for exit): ");
      input = sc.nextLine();
      if (input.equals("0")) {
        break;
      }

      try {
        eq = new QuadraticEquation(input);
      } catch (InvalidEquationException e) {
        System.err.println("Error: " + e.getMessage() + '\n');
        continue;
      }

      roots = eq.solve();
      if (roots == null) {
        System.out.println("No roots");
      } else if (roots.length == 1) {
        System.out.println("x = " + roots[0]);
      } else {
        System.out.println("x1 = " + roots[0]);
        System.out.println("x2 = " + roots[1]);
      }

      System.out.println();
    }

    sc.close();
  }
}
