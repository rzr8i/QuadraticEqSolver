package com.github.rzr8i;

public class QuadraticEquation {
  private String equation;
  private int pos;
  private int a, b, c;

  public QuadraticEquation(String equation) throws InvalidEquationException {
    this.equation = equation.toLowerCase();
    this.equation = this.equation.replaceAll(" ", "");
    this.pos = 0;
    this.parse();
  }

  /**
   * @return <code>equation[pos]</code> if <code>pos</code>
   *         in bound and <code>'\0'</code> otherwise.
   */
  private char peek() {
    if (pos >= 0 && pos < equation.length())
      return equation.charAt(pos);
    else
      return '\0';
  }

  /**
   * Parses a number starting from <code>pos</code>.
   * 
   * @throws IllegalStateException if there is no number at
   *                               <code>equation[pos]</code>.
   */
  private int parseNumber() {
    int start = pos;

    while (Character.isDigit(peek()))
      pos++;

    if (start == pos) {
      throw new IllegalStateException("Expected a number at equation[" + pos + "].");
    }

    return Integer.parseInt(equation.substring(start, pos));
  }

  private Term parseTerm(boolean isFirstTerm) throws InvalidEquationException {
    int coefficient = 1;
    int exponent = 0;

    if (peek() == '-' || peek() == '+') {
      coefficient *= (peek() == '-' ? -1 : 1);
      pos++;
    } else if (!isFirstTerm) {
      throw new InvalidEquationException(
          "Expected +/- at position " + pos + " of the equation but found '" + peek() + "'.");
    }

    if (peek() != 'x' && !Character.isDigit(peek())) {
      throw new InvalidEquationException(
          "Expected x/digit at position " + pos + " of the equation but found '" + peek() + "'.");
    }

    if (Character.isDigit(peek())) {
      coefficient *= parseNumber();
    }

    if (peek() == 'x') {
      exponent = 1;
      pos++;
      if (peek() == '^') {
        pos++;
        exponent = parseNumber();
      }
    }

    return new Term(coefficient, exponent);
  }

  private void parse() throws InvalidEquationException {
    Term terms[] = new Term[3];
    Term term;
    boolean isFirstTerm = true;

    while (peek() != '\0') {
      term = parseTerm(isFirstTerm);
      if (term.getExponent() < 0 || term.getExponent() > 2) {
        throw new InvalidEquationException("Invalid term: " + term);
      }

      if (terms[term.getExponent()] != null)
        terms[term.getExponent()].add(term);
      else
        terms[term.getExponent()] = term;

      isFirstTerm = false;
    }

    if (terms[2] == null || terms[2].getCoefficient() == 0) {
      throw new InvalidEquationException("This equation is not a quadratic equation.");
    }

    a = (terms[2] == null) ? 0 : terms[2].getCoefficient();
    b = (terms[1] == null) ? 0 : terms[1].getCoefficient();
    c = (terms[0] == null) ? 0 : terms[0].getCoefficient();
  }

  /**
   * Solves the quadratic equation and returns the roots (if any).
   *
   * @return An array of two/one double(s) representing the root(s)
   *         of the equation. If the equation has no real root(s),
   *         null is returned.
   */
  public double[] solve() {
    double delta = (b * b) - (4 * a * c);

    if (delta < 0)
      return null;
    else if (delta == 0) {
      return new double[] { (-b) / (2 * a) };
    } else {
      double delta_sqrt = Math.sqrt(delta);
      return new double[] {
        ((-b) + delta_sqrt) / (2 * a),
        ((-b) - delta_sqrt) / (2 * a)
      };
    }
  }
}

class InvalidEquationException extends Exception {
  public InvalidEquationException(String msg) {
    super(msg);
  }
}

class Term {
  private int coefficient;
  private int exponent;

  public Term(int coefficient, int exponent) {
    this.coefficient = coefficient;
    this.exponent = exponent;
  }

  public Term() {
    this(0, 0);
  }

  @Override
  public String toString() {
    if (exponent == 0)
      return Integer.toString(coefficient);

    String result = "";
    if (coefficient == 1 || coefficient == -1)
      result += (coefficient == -1 ? "-" : "");
    else
      result += coefficient;

    return result + "x" + (exponent == 1 ? "" : "^" + exponent);
  }

  public void add(Term other) {
    if (this.exponent != other.exponent) {
      throw new IllegalArgumentException("Can't add two terms with different exponents.");
    }

    this.coefficient += other.coefficient;
  }

  public int getCoefficient() {
    return coefficient;
  }

  public void setCoefficient(int coefficient) {
    this.coefficient = coefficient;
  }

  public int getExponent() {
    return exponent;
  }

  public void setExponent(int exponent) {
    this.exponent = exponent;
  }
}
