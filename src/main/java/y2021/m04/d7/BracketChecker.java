package y2021.m04.d7;

/**
 * @author adolphor
 */
public class BracketChecker {

  private String input;

  public BracketChecker(String in) {
    input = in;
  }

  public void check() {
    int size = input.length();
    MyCharStack charStack = new MyCharStack(size);
    for (int i = 0; i < input.length(); i++) {
      char ch = input.charAt(i);
      switch (ch) {
        case '{':
        case '[':
        case '(':
          charStack.push(ch);
          break;
        case '}':
        case ']':
        case ')':
          if (!charStack.isEmpty()) {
            char pop = charStack.pop();
            if ((ch == '}' && pop != '{') ||
              (ch == ']' && pop != '[') ||
              (ch == ')' && pop != '(')) {
              System.err.println("Error: " + ch + " at " + i);
            }
          } else {
            System.err.println("Error: " + ch + " at " + i);
          }
          break;
        default:
          break;
      }
    }
  }

}
