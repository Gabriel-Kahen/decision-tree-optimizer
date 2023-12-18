import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Main {
  public static void main(String[] args) {
    String fileName;
    //fileName = "testData.csv";
    fileName = "testData2.csv";
    
    String[][] data = readCSV(fileName);
    Node decisionTree = makeDecisionTree(data);

    //print(data);
    //decisionTree.printTree();
    play(fileName);
  }

  public static void print(String[][] data){
    int[] lens = new int[data[0].length];
    int current = 0;
    for(int c = 0; c < data[0].length; c++){
      for(int r = 0; r < data.length; r++){
        if(data[r][c].length() > current){
          current = data[r][c].length();
        }
      }
      lens[c] = current;
      current = 0;
    }
  
    for(int i = 0; i < data.length; i ++){
      for(int j = 0; j < data[0].length; j++){
        System.out.print(data[i][j]);
        for(int k = 0; k < lens[j] - data[i][j].length() + 2; k++){
          System.out.print(" ");
        }
      }
      System.out.println();
    }
  }

  public static String[][] readCSV(String fileName) {
    try {
        int numRows = 0;
        Scanner s = new Scanner(new File(fileName));
        while (s.hasNextLine()) {
            s.nextLine();
            numRows++;

        }

      String[][] data = new String[numRows][];
        s = new Scanner(new File(fileName));
        String[] currentRow;
        for(int i = 0; i < numRows; i++){
            currentRow = s.nextLine().split(",", 0);
            data[i] = currentRow;
        }
        s.close();
        return data;
    }
    catch (FileNotFoundException e) {
        System.out.println("broken");
        return null;
    }
    }

  public static String[][] removeHeader(String[][] data){
    String[][] ret = new String[data.length - 1][data[0].length];
    for(int i = 0; i < data.length - 1; i++){
      for(int j = 0; j < data[0].length; j++){
        ret[i][j] = data[i+1][j];
      }
    }
    return ret;
  }

  public static String[][] filterOption(String[][] data, int colNum, String option){
    String[] firstRow = data[0];
    int index = colNum;

    int count = 0;

    for(int i = 0; i < data.length; i++){
      if(data[i][index].equals(option)){
        count++;
      }
    }

    String[][] ret = new String[count + 1][data[0].length];
    ret[0] = firstRow;
    int idk = 1;
    for(int i = 1; i < data.length; i++){
      if(data[i][index].equals(option)){
        for(int j = 0; j < data[0].length; j++){
          ret[idk][j] = data[i][j];
        }
        idk++;
      }
    }
    return ret;
  }
  
  public static double entropy(String[][] data){

    if(data.length <= 2){
      return 0;
    }
    
    int numRows = data.length - 1;
    int numColumns = data[0].length;
    double numYes = 0.0;
    
    for(int i = 0; i < data.length; i++){
      if((data[i][numColumns - 1].equals("Yes")) || (data[i][numColumns - 1].equals("Y")) || (data[i][numColumns - 1].equals("y")) || (data[i][numColumns - 1].equals("1")) || (data[i][numColumns - 1].equals("YES")) || (data[i][numColumns - 1].equals("yes"))){
                numYes++;
            }
        }
    double pYes = numYes/numRows;
    double pNo = 1 - pYes;
    double entropy = (-pYes * (Math.log(pYes)/Math.log(2)) - pNo * (Math.log(pNo)/Math.log(2)));
    if(Double.isNaN(entropy)){
      return 0;
    }
    return entropy;
  }

  public static double infoGain(String[][] data, String category){
    String[] options = getOptions(data, category);
    double[] entropys = new double[options.length];
    int categoryIndex = 0;
    for(int i = 0; i < data[0].length; i++){
      if(data[0][i].equals(category)){
        categoryIndex = i;
      }
    }

    for(int i = 0; i < entropys.length; i++){
      entropys[i] = entropy(filterOption(data, categoryIndex, options[i]));
    }
    double numTotal = data.length - 1;
    double currentEntropy;
    double numOption;
    
    double infoGain = entropy(data);
    for(int i = 0; i < options.length; i++){
      currentEntropy = entropys[i];
      numOption = 0;

      for(int j = 1; j < data.length; j++){
        for(int k = 0; k < data[0].length; k++){
          if(data[j][k].equals(options[i])){
            numOption++;
          }
        }
      }
      
      infoGain -= (numOption/numTotal)*currentEntropy;
    }

    //might have to check if it is NaN like in entropy idk if this problem exists tho
    return infoGain;
  }

  public static double highestInfoGain(String[][] data){
    double highestGain = 0;
    double currentGain;
    for(int i = 0; i < data[0].length - 1; i++){
      currentGain = infoGain(data, data[0][i]);
      if(currentGain > highestGain){
        highestGain = currentGain;
      }
    }
    return highestGain;
  }

  public static String highestInfoGainCategory(String[][] data){
    double highestGain = 0;
    String highestGainCategory = null;
    double currentGain;
    for(int i = 0; i < data[0].length - 1; i++){
      currentGain = infoGain(data, data[0][i]);
      if(currentGain > highestGain){
        highestGain = currentGain;
        highestGainCategory = data[0][i];
      }
    }
    return highestGainCategory;
  }

  public static String[] getOptions(String[][] data, String category){
    ArrayList<String> options = new ArrayList<String>(); 

    String[] firstRow = data[0];
    int index = 0;
    for(int i = 0; i < firstRow.length; i++){
      if(firstRow[i].equals(category)){
        index = i;
      }
    }

    for(int j = 1; j < data.length; j++){
      if(!(options.contains(data[j][index]))){
        options.add(data[j][index]);
      }
    }
      
    String[] ret = options.toArray(new String[options.size()]);
    return ret;

  }

  public static int getCategoryIndex(String[][] data, String category){
    for(int i = 0; i < data[0].length; i++){
      if(data[0][i].equals(category)){
        return i;
      }
    }
    return 666;
  }

  public static Node makeDecisionTree(String[][] data){
    
    String category = highestInfoGainCategory(data);
    Node decisionTree = new Node(category);
    String[] options = getOptions(data, category);
    addOptions(data, decisionTree, category);

    for(int i = 0; i < options.length; i++){
      String[][] current = filterOption(data, getCategoryIndex(data, category), options[i]);
      if((entropy(current)) == 0.0){
        decisionTree.getChild(i).addChild(new Node(yesOrNo(current)));
      }

      else if(current.length == 2){
        decisionTree.getChild(i).addChild(new Node(current[1][data[0].length]));
      }
      
      else if(hasNoAnswer(current)){
        decisionTree.getChild(i).addChild(new Node("No Answer"));
      }
        
      else{
        decisionTree.getChild(i).addChild(makeDecisionTree(current));
      }
    }
    return decisionTree;
  }

  public static Node addOptions(String[][] data, Node node, String category){
    String[] options = getOptions(data, category);
    for(int i = 0; i < options.length; i++){
       node.addChild(new Node(options[i])); 
    }
    return node;
  }

  public static String yesOrNo(String[][] data){
    return data[1][data[0].length - 1];
  }

  public static boolean hasNoAnswer(String[][] data){
    for(int c = 0; c < data[0].length - 1; c++){
      for(int r = 1; r < data.length - 1; r++){
        if(!(data[r][c].equals(data[r + 1][c]))){
          return false;
        }
      }
    }

    for(int r = 0; r < data.length - 1; r++){
      if(!(data[r][data[0].length - 1].equals(data[r + 1][data[0].length - 1]))){
        return true;
      }
    }
    return false;
  }

  public static void play(String fileName){
    String[][] data = readCSV(fileName);
    Node decisionTree = makeDecisionTree(data);
    playHelper(decisionTree);
  }

  public static void playHelper(Node n){
    Scanner input = new Scanner(System.in);
    if(n.getValue().equals("No Answer")){
      System.out.println("No Answer");
      input.close();
    }
    else{
    System.out.println(n.getValue() + "?");
    String answer = input.nextLine();
    int index = -1;
    for(int i = 0; i < n.getChildren().size(); i++){
      if(n.getChild(i).getValue().toUpperCase().equals(answer.toUpperCase())){
        index = i;
      }
    }
    if(index == -1){
      System.out.println("Your input is not a valid option. Either your combination does not exist in the dataset or you mistyped your answer.");
      System.exit(0);
    }
    if(
n.getChild(index).getChild(0).getValue().toUpperCase().equals("YES") || n.getChild(index).getChild(0).getValue().toUpperCase().equals("NO") || n.getChild(index).getChild(0).getValue().toUpperCase().equals("Y") || n.getChild(index).getChild(0).getValue().toUpperCase().equals("N"))
    {
      System.out.println("The answer is: " + n.getChild(index).getChild(0).getValue());
    }
    else{
      playHelper(n.getChild(index).getChild(0));
    }
    input.close();
  }
  }
}