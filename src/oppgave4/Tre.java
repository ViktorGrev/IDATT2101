package oppgave4;

public class Tre {
  TreNode rot;

  public Tre(TreNode rot) {
    this.rot = rot;
  }

  public double operations(Object o, double l, double r) {
    switch (o.toString()) {
      case "+":
        return l + r;
      case "-":
        return l - r;
      case "*":
        return l * r;
      case "/":
        return l / r;
      default:
        throw new IllegalArgumentException(o.toString());
    }

  }

  public double sum() {
    if (rot == null) return 0;
    return sumrek(rot);

  }

  //inordentraversering
  private double sumrek(TreNode n) {
    if (n.left == null && n.right == null) return (double) n.element;
    return operations(n.element, sumrek(n.left), sumrek(n.right));
  }

  public void print() {
    if (rot == null) return;
    printOut(rot);
  }

  private void printOut(TreNode n) {
    if (n.left != null) {
      System.out.print("(");
      printOut(n.left);
    }
    System.out.print(n.element + " ");
    if (n.right != null) {
      printOut(n.right);
      System.out.print(")");
    }
  }

  public static void main(String[] args) {
    TreNode node2l = new TreNode(2.0, null, null);
    TreNode node4 = new TreNode(4.0, null, null);
    TreNode plusOperator = new TreNode("+", node2l, node4);
    TreNode node3 = new TreNode(3.0, null, null);
    TreNode multiplyLeftSide = new TreNode("*", node3, plusOperator);

    TreNode node2rl = new TreNode(2.0,null,null);
    TreNode node2rr = new TreNode(2.0,null,null);
    TreNode multiplyRightSide = new TreNode("*",node2rl,node2rr);
    TreNode node7 = new TreNode(7.0,null,null);
    TreNode minus = new TreNode("-",node7,multiplyRightSide);

    TreNode rot = new TreNode("/", multiplyLeftSide,minus);

    Tre uttrykstre = new Tre(rot);
    System.out.println(uttrykstre.sum());
    uttrykstre.print();
  }
}

class TreNode {
  Object element;
  TreNode left;
  TreNode right;

  public TreNode(Object e, TreNode l, TreNode r) {
    element = e;
    left = l;
    right = r;
  }
}