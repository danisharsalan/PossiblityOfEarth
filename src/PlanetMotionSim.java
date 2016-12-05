import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class PlanetMotionSim {

  static int numOfSims; //1
  static double radiusOfGalaxy; //1000000000000.0
  static int numOfSuns; //100000
  static int numOfPlanets; //50000
  static ArrayList <Double> planets;
  static ArrayList <Double> suns;
  static ArrayList <Double> moons;
  static ArrayList <Double> sunHolder;
  static int numOfCollisions = 0;
  static ArrayList <Double> planetMass;
  static double sunMass;
  static double GValue =  6.67259 * Math.pow(10, -11);
  static int habitablePlanets = 0;
  static ArrayList <Integer> xPosOfHabitablePlanets = new ArrayList <Integer> ();
  private static final String FILE_HEADER = "Planet #,X Position,Y Position,Radius,Mass";
  static ArrayList <JTable> tablesHolder;
  final static JFrame frame = new JFrame("Planet Simulation");


  public static void main(String[] args) throws IOException {
    int h = -1;
    while (h < 0){
      numOfSims = Integer.parseInt(JOptionPane.showInputDialog("How many times do you want to run this simulation? (Please enter an integer larger than 0)"));
      if(numOfSims > 0){
        h++;
      } else {
        JOptionPane.showMessageDialog(null, "Enter an integer larger than 0!");
      }
    }
    int h2 = -1;
    while (h2 < 0){
      radiusOfGalaxy = Double.parseDouble(JOptionPane.showInputDialog("What do you want the radius of your galaxy (in lightyears) to be this simulation? (Please enter an integer larger than 0)")) * 9.461 * Math.pow(10, 12);
      if(radiusOfGalaxy > 0){
        h2++;
      } else {
        JOptionPane.showMessageDialog(null, "Enter an integer larger than 0!");
      }
    }
    int h3 = -1;
    while (h3 < 0){
      numOfSuns = Integer.parseInt(JOptionPane.showInputDialog("How many suns do you want in this simulation? (Please enter an integer larger than 0)"));
      if(numOfSuns > 0){
        h3++;
      } else {
        JOptionPane.showMessageDialog(null, "Enter an integer larger than 0!");
      }
    }
    int h4 = -1;
    while (h4 < 0){
      numOfPlanets = Integer.parseInt(JOptionPane.showInputDialog("How many planets do you want in this simulation? (Please enter an integer larger than 0)"));
      if(numOfPlanets > 0){
        h4++;
      } else {
        JOptionPane.showMessageDialog(null, "Enter an integer larger than 0!");
      }
    }
    //List myEntries = new ArrayList();
    JTable table;
    tablesHolder = new ArrayList <JTable>();
    String[][] myEntries = new String[0][0];
    String[] columnNames = {"Planet #","X Position","Y Position","Radius","Mass"};
    for(int g = 0; g < numOfSims; g++){
      System.out.println("Galaxy #" + (g+1));
      createGalaxy();
      System.out.println(g);
      System.out.println("Number of habitable planets found: " + habitablePlanets);
      if(habitablePlanets == 0){
         myEntries = new String[1][1];
         myEntries[0][0] = "No habitable planets were found";
         table = new JTable(myEntries, columnNames);
      } else if (habitablePlanets == 1){
        System.out.println("Only planet #" + xPosOfHabitablePlanets.get(0) + " is habitable.");
        myEntries = new String[1][5];
        myEntries[0][0] = xPosOfHabitablePlanets.get(0).toString();
        myEntries[0][1] = planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3)) + "";
        myEntries[0][2] = planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3) + 1) + "";
        myEntries[0][3] = planetMass.get(xPosOfHabitablePlanets.get(0)-1) + " kg";
        myEntries[0][4] = planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3) + 2) + " km";
        table = new JTable(myEntries, columnNames);
      } else {
        for(int i = 0; i < xPosOfHabitablePlanets.size(); i++){
          System.out.println("Planet #" + xPosOfHabitablePlanets.get(i) + " is habitable.");
          myEntries = new String[xPosOfHabitablePlanets.size()][5];
          myEntries[i][0] = xPosOfHabitablePlanets.get(i).toString();
          myEntries[i][1] = planets.get(((xPosOfHabitablePlanets.get(i)-1) * 3)) + "";
          myEntries[i][2] = planets.get(((xPosOfHabitablePlanets.get(i)-1) * 3) + 1) + "";
          myEntries[i][3] = planetMass.get(xPosOfHabitablePlanets.get(i)-1) + " kg";
          myEntries[i][4] = planets.get(((xPosOfHabitablePlanets.get(i)-1) * 3) + 2) + " km";
         
        }
        table = new JTable(myEntries, columnNames);
        tablesHolder.add(table);
      }
      generateCsvFile("PlanetsTest" + (g+1) + ".csv");
      xPosOfHabitablePlanets.clear();
      habitablePlanets = 0;
      /*JScrollPane scrollPane = new JScrollPane(table);
      table.setFillsViewportHeight(true);

      JLabel lblHeading = new JLabel("Simulation # " + (g + 1));
      lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));

      frame.getContentPane().setLayout(new BorderLayout());

      frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
      frame.getContentPane().add(scrollPane,BorderLayout.CENTER);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(550, 200);
      frame.setVisible(true);*/
      
      //CSVReader reader = new CSVReader(new FileReader("PlanetsTest" + (g+1) + ".csv"));
      //myEntries.add(reader.readAll());
    }
    //generateTables(tablesHolder);
    
  }

  /*public static void test() throws IOException {
    CSVReader reader = new CSVReader(new FileReader("yourfile.csv")); 
    List myEntries = reader.readAll();
  }*/
  
  /*private static void generateTables(ArrayList<JTable> tableArray){
    for(int d = 0; d < tableArray.size(); d++){
      JScrollPane scrollPane = new JScrollPane(tablesHolder.get(d));
      tablesHolder.get(d).setFillsViewportHeight(true);

      JLabel lblHeading = new JLabel("Simulation # " + (d + 1));
      lblHeading.setFont(new Font("Arial",Font.TRUETYPE_FONT,24));

      frame.getContentPane().setLayout(new BorderLayout());

      frame.getContentPane().add(lblHeading,BorderLayout.PAGE_START);
      frame.getContentPane().add(scrollPane,BorderLayout.CENTER);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(550, 200);
      frame.setVisible(true);
    }*/
    /*for(int d = 0; d < tableArray.size(); d++){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container c = frame.getContentPane();
    c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
    c.add(tableArray.get(d).getTableHeader());
    c.add(tableArray.get(d));
    frame.pack();
    frame.setVisible(true);
    }
  }*/

  private static void generateCsvFile(String sFileName)
  {
    try
    {
      FileWriter writer = new FileWriter(sFileName);
      writer.append(FILE_HEADER.toString());
      writer.append('\n');
      if(habitablePlanets == 0){
        writer.append("There are no habitable planets in this galaxy");
      } else if (habitablePlanets == 1){
        writer.append(xPosOfHabitablePlanets.get(0) + "");
        writer.append(",");
        writer.append(planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3)) + "");
        writer.append(",");
        writer.append(planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3) + 1) + "");
        writer.append(",");
        writer.append(planetMass.get(xPosOfHabitablePlanets.get(0)-1) + " kg");
        writer.append(",");
        writer.append(planets.get(((xPosOfHabitablePlanets.get(0)-1) * 3) + 2) + " km");
        writer.append("\n");
        writer.append("There is 1 habitable planet in this galaxy");
      } else {
        for(int g = 0; g < xPosOfHabitablePlanets.size(); g++){
          writer.append(xPosOfHabitablePlanets.get(g) + "");
          writer.append(",");
          writer.append(planets.get(((xPosOfHabitablePlanets.get(g)-1) * 3)) + "");
          writer.append(",");
          writer.append(planets.get(((xPosOfHabitablePlanets.get(g)-1) * 3) + 1) + "");
          writer.append(",");
          writer.append(planetMass.get(xPosOfHabitablePlanets.get(g)-1) + " kg");
          writer.append(",");
          writer.append(planets.get(((xPosOfHabitablePlanets.get(g)-1) * 3) + 2) + " km");
          writer.append("\n");
        }
        writer.append("There are " + habitablePlanets + " habitable planets in this galaxy");
      }

      writer.flush();
      writer.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static int searchPlanets() {
    //double solarMass;
    //double actualMass;
    double gravityOnSurface;
    boolean goodGravity = false;
    boolean goodDistanceFromASun = false;
    int numOfHabitable = 0;
    for(int f = 0; f < planets.size(); f += 3){
      System.out.println("Mass of planet: " + (f/3 + 1) + " Mass: " + planetMass.get(f/3) + " Radius: " + planets.get(f+2));
      gravityOnSurface = (GValue) * (planetMass.get(f/3)) / Math.pow((planets.get(f + 2)), 2) / (Math.pow(10, 11));
      System.out.println("Planet: " + (f/3 + 1) + " Gravity: " + gravityOnSurface);
      if(gravityOnSurface < 29.421 && gravityOnSurface > 2.6){
        goodGravity = true;
        System.out.println(" Gravity status: good!");
      } else {
        System.out.println(" Gravity status: bad!");
      }
      for(int w = 0; w < suns.size(); w += 3){
        double distance = Math.sqrt(Math.pow((suns.get(w) - planets.get(f)), 2) + Math.pow((suns.get(w+1) - planets.get(f+1)), 2));
        if(distance >= (planets.get(f+2) + suns.get(w+2) + 473) && distance <= (planets.get(f+2) + suns.get(w+2) + 12577.0)){
          goodDistanceFromASun = true;
          System.out.println(" Sun Distance: Good!");
          break;
        }
      }
      if(goodGravity == true && goodDistanceFromASun == true){
        numOfHabitable++;
        xPosOfHabitablePlanets.add(f/3 + 1);
      }
      goodGravity = false;
      goodDistanceFromASun = false;
    }
    System.out.println("Search complete.");
    return numOfHabitable;
  }

  public static void createGalaxy(){
    System.out.println(" created.");
    createSuns();
  }


  public static void createSuns(){
    double distanceFromCenter;
    double angle;
    suns = new ArrayList <Double> ();
    for(int x = 0; x < numOfSuns; x++){
      distanceFromCenter = Math.random() * GetGalaxyRadius();
      angle = Math.toRadians(Math.random() * (360));
      suns.add(distanceFromCenter * Math.cos(angle)); //x
      suns.add(distanceFromCenter * Math.sin(angle)); //y
      suns.add(Math.random() * (0.9876 - 0.0002) + 0.0002); //r
      //System.out.println("Sun: " + (x+1) + " x: " + suns.get(x*3) + " y: " + suns.get(x*3 + 1) + " r: " + suns.get(x*3 +2));
    }
    System.out.println("All Suns created.");
    createPlanets();
  }

  private static void createPlanets() {
    double distanceFromCenter2;
    double angle2;
    planets = new ArrayList <Double> ();
    planetMass = new ArrayList <Double> ();
    sunHolder = getSuns();
    for(int y = 0; y < numOfPlanets; y++){
      if(Math.random() < 0.05){
        distanceFromCenter2 = Math.random() * GetGalaxyRadius();
        angle2 = Math.toRadians(Math.random() * (360));
        planetMass.add((Math.random() * (1.346 - 0.00001501) + 0.00001501) * (1.989 * Math.pow(10, 30)));
        planets.add(distanceFromCenter2 * Math.cos(angle2)); //x
        planets.add(distanceFromCenter2 * Math.sin(angle2)); //y
        planets.add(Math.abs(Math.random() * (12577.0 - 473.0) + 473.0)); //r
      } else {
        int sunXPos =  (int) (Math.random() * (sunHolder.size())/3) * 3;
        sunMass = (Math.random() * (120 - 0.008) + 0.008) * (1.989 * Math.pow(10, 30));
        double radius = Math.abs(Math.random() * (12577.0 - 473.0) + 473.0);
        planetMass.add((Math.random() * (1.346 - 0.00001501) + 0.00001501) * (1.989 * Math.pow(10, 30)));
        //double velocityOfPlanet = Math.sqrt((GValue) * (planetMass) / radius) / 1000; //in km/s
        double distFromSun = sunMass * suns.get(sunXPos + 2) / planetMass.get(y);
        planets.add(suns.get(sunXPos) + distFromSun); //x
        planets.add(suns.get(sunXPos + 1)); //y
        planets.add(radius); //r
      }
      System.out.println("Planet: " + (y+1) + " x: " + planets.get(y*3) + " y: " + planets.get(y*3 + 1) + " r: " + planets.get(y*3 +2));
    }
    System.out.println("All planets created.");
    createMoons();
  }

  private static void createMoons() {
    double distanceFromCrustOfPlanet;
    double sizeOfMoon;
    double angle3;
    double xCoor;
    double yCoor;
    moons = new ArrayList <Double> ();
    for(int b = 0; b < planets.size(); b += 3){
      distanceFromCrustOfPlanet = Math.random() * (0.0004 - 0.000356922) - 0.000356922;
      angle3 = Math.toRadians(Math.random() * (360));
      xCoor = planets.get(b) + Math.cos(angle3)*(planets.get(b+2) + distanceFromCrustOfPlanet);
      yCoor = planets.get(b+1) + Math.sin(angle3)*(planets.get(b+2) + distanceFromCrustOfPlanet);
      sizeOfMoon = Math.random() * (0.000005262 - 0.000001066) + 0.000001066;
      moons.add(xCoor); //x
      moons.add(yCoor); //y
      moons.add(sizeOfMoon); //r
      System.out.println("Moon: " + (b/3 +1) + " x: " + moons.get(b) + " y: " + moons.get(b + 1) + " r: " + moons.get(b + 2));
    }
    System.out.println("All moons created");
    habitablePlanets += searchPlanets();
    //checkForCollision(getSuns(), getPlanets(), getMoons());
  }


  @SuppressWarnings("unused")
  private static void checkForCollision(ArrayList <Double> suns, ArrayList <Double> planets, ArrayList <Double> moons){
    for(int a = 0; a < planets.size(); a += 3){
      for(int c = 0; c < suns.size(); c += 3){
        if(Math.sqrt(Math.pow((suns.get(c) - planets.get(a)), 2) + Math.pow((suns.get(c+1) - planets.get(a+1)), 2)) <= (planets.get(a+2) + suns.get(c+2))){
          numOfCollisions++;
        }
      }
    }
    for(int d = 0; d < moons.size(); d += 3){
      for(int c = 0; c < suns.size(); c += 3){
        if(Math.sqrt(Math.pow((suns.get(c) - moons.get(d)), 2) + Math.pow((suns.get(c+1) - moons.get(d+1)), 2)) <= (planets.get(d+2) + suns.get(c+2))){
          numOfCollisions++;
        }
      }
    }
    //System.out.println("Number of collision: " + numOfCollisions);
  }


  public static double GetGalaxyRadius (){
    return radiusOfGalaxy;
  }

  public static ArrayList<Double> getSuns() {
    return suns;
  }    

  public static ArrayList<Double> getPlanets() {
    return planets;
  }

  @SuppressWarnings("unused")
  private static ArrayList<Double> getMoons()
  {
    return moons;
  }    
}
