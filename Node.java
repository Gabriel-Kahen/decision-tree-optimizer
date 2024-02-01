import java.util.ArrayList;

public class Node{
  String value;
  ArrayList<Node> children;

  public Node(String value, ArrayList<Node> children){
    this.value = value;
    this.children = children;
  }

  public Node(String value){
    this.value = value;
    this.children = new ArrayList<Node>();
  }

  public Node(){
    value = null;
    this.children = new ArrayList<Node>();
  }

  public String getValue(){
    return this.value;
  }

  public ArrayList<Node> getChildren(){
    return this.children;
  }

  public Node getChild(int index){
    return this.children.get(index);
  }

  public void setValue(String value){
    this.value = value;
  }

  public void setChildren(ArrayList<Node> children){
    this.children = children;
  }

  public void addChild(Node child){
    this.children.add(child);
  }

  public void printTree(){
    printHelper(0);
  }

  public void printHelper(int level) {
    //from https://stackoverflow.com/questions/33383473/how-to-visually-print-a-non-binary-tree
    for (int i = 1; i < level; i++) {
      System.out.print("\t");
    }
    System.out.println(value);
    for (Node child : children) {
      child.printHelper(level + 1);
    }
  }
}