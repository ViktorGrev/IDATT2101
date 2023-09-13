import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Match {
  private Stack<Character> stack;

  public Match() {
    stack = new Stack<>();
  }

  public static void main(String[] args) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(new File("src/JosephusProblem.java")));
      Match s = new Match();

      String line;
      while ((line = br.readLine()) != null) {
        for (char c : line.toCharArray()) {
          if (isOpenParenthesis(c)) {
            s.addToStack(c);
          } else if (isCloseParenthesis(c) && !s.stack.isEmpty() && isMatchingPair(s.stack.peek(), c)) {
            s.removeFromStack();
          }
        }
      }

      System.out.println("Tabellen er tom: " + s.isEmpty());
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean isOpenParenthesis(char c) {
    return c == '{' || c == '(' || c == '[';
  }

  public static boolean isCloseParenthesis(char c) {
    return c == '}' || c == ')' || c == ']';
  }

  public static boolean isMatchingPair(char open, char close) {
    return (open == '{' && close == '}') || (open == '(' && close == ')') || (open == '[' && close == ']');
  }

  public boolean isEmpty() {
    return stack.isEmpty();
  }

  public void addToStack(char character) {
    stack.push(character);
    System.out.println("Legger til: " + character);
    System.out.println(stack.toString());
  }

  public void removeFromStack() {
    if (!isEmpty()) {
      char removedChar = stack.pop();
      System.out.println("Removed character: " + removedChar);
    }
  }
}
