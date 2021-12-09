package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Formula1ChampionshipManager extends JPanel implements ChampionshipManager, ActionListener {
//    Add a button which every time it is pressed it generates one random race with random
//    positions achieved by the existing drivers. This automatically updates the Formula 1
//    championship table by adding the race (points, positions and other statistics). The
//    positions should be entirely random and not hardcoded in your source code. The
//    button should generate a diferent race with diferent driver positions every time it is
//    clicked. The user should be able to see the randomly generated race with the driver
//    positions (in addition to the table of standings), in order to be able to verify the
//    correctness of your code for the updated information of the table. (8 marks).

    ArrayList<Formula1Driver> drivers = new ArrayList<>();
    File driverData = new File("driverData.txt");
//    File raceRound = new File("raceRound.txt");
//    File dates = new File("dates.txt");


    JFrame frame;
    JTable table;

    JButton buttonOrderTableDesc;
    JButton buttonOrderTableAsc;
    JButton buttonOrderTableFirstWinsDesc;
    JButton buttonRace = new JButton("Lets race!");

    JPanel tablePanel = new JPanel();
    JPanel menuPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JLabel titleLabel = new JLabel("Formula 1 Championship");

    public static void main(String[] args) throws FileNotFoundException {

        Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
        //
        manager.orderTableDesc(manager.table);
    }

    // run main GUI using constructor
    public Formula1ChampionshipManager() throws FileNotFoundException {
        // load data from driverData.txt into arrayList drivers
        loadData();
        // creating main frame window to add all components
        frame = new JFrame("Formula 1 Championship Manager 2021");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setting cosmetics for title panel
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEtchedBorder());
        // setting menuPanel layout
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        // adding menu buttons to menuPanel and adding action listeners to register events
        buttonOrderTableDesc = new JButton("Order Table by Points (Desc)");
        buttonOrderTableAsc = new JButton("Order Table by Points (Asc)");
        buttonOrderTableFirstWinsDesc = new JButton("Order Table by First Wins");
        menuPanel.add(buttonOrderTableDesc);
        buttonOrderTableDesc.addActionListener(this);
        menuPanel.add(buttonOrderTableAsc);
        buttonOrderTableAsc.addActionListener(this);
        menuPanel.add(buttonOrderTableFirstWinsDesc);
        buttonOrderTableFirstWinsDesc.addActionListener(this);
        menuPanel.add(buttonRace);
        buttonRace.addActionListener(this);
        // design for menu button panel
        menuPanel.setBackground(Color.lightGray);
        menuPanel.setBorder(BorderFactory.createMatteBorder(270, 30, 200, 30, Color.lightGray));
        //creating JTable contents
        String[] columnNames = {"First Name",
                "Last Name",
                "Country",
                "Team",
                "Total points",
                "First Pos Wins",
                "Second Pos Wins",
                "Third Pos Wins",
                "Races completed"};
        Object[][] data = {{"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}
                , {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}, {"", "", "", "", "", "", "", "", ""}};

        table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 160));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        tablePanel.setBackground(Color.white);
        // assigning panels to top, bottom, left etc. areas of the frame container
        frame.getContentPane().add(tablePanel, "South");
        frame.getContentPane().add(titlePanel, "North");
        frame.getContentPane().add(menuPanel, "West");


        frame.setSize(1280, 720);
//        frame.pack();
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == buttonOrderTableDesc) {

                orderTableDesc(table);
                System.out.println("buttonOrderTableDesc");

            } else if (e.getSource() == buttonOrderTableAsc) {

                orderTableAsc(table);
                System.out.println("buttonOrderTableAsc");

            } else if (e.getSource() == buttonOrderTableFirstWinsDesc) {

                orderTableFirstWinsDesc(table);
                System.out.println("buttonOrderTableFirstWinsDesc");

            } else if (e.getSource() == buttonRace) {

                race();
                System.out.println("buttonRace");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void orderTableDesc(JTable table) throws FileNotFoundException {
        //sort the unsorted data in Desc order of points
        Collections.sort(drivers, new TotalPointsComparatorDescending());
        // write the sorted (desc) data to the driverData txt file
        saveData();
        // populate the JTable with new data in descending order,
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < 10; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();

    }

    @Override
    public void orderTableAsc(JTable table) throws FileNotFoundException {
        Collections.sort(drivers, new TotalPointsComparatorAscending());
        // write the sorted (Asc) data to the driverData txt file
        saveData();
        // populate the JTable with new data in ascending order,
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < 10; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    @Override
    public void orderTableFirstWinsDesc(JTable table) throws FileNotFoundException {
        Collections.sort(drivers, new FirstPosWinsComparatorDescending());
        // write the sorted (desc) data to the driverData txt file
        saveData();
        // populate the JTable with new data in descending order,
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < 10; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();

    }

    @Override
    public void race() {
        ArrayList<Integer> driverIndex = new ArrayList<>();
        Random rand = new Random();
        // same result as shuffling to a deck of cards numbered 0-9
        for (int i; driverIndex.size() != 10; ) { // populating driverIndex with uniquely random ints 0-9
            i = rand.nextInt(drivers.size());
            if (driverIndex.contains(i)) {
                while (driverIndex.contains(i)) {
                    i = rand.nextInt(drivers.size());
                }
                //while loop breaks, then we add driver to driverIndex arrayList
            }
            driverIndex.add(i);
        }
        for (int i = 0; i < drivers.size(); i++) {
            switch (i) {
                case 0: // winning 1st position
                    drivers.get(driverIndex.get(i)).setFirstPosCount(1);
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(25);
                    break;
                case 1: // winning 2nd position
                    drivers.get(driverIndex.get(i)).setSecondPosCount(1);
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(18);
                    break;
                case 2: // winning 3rd position
                    drivers.get(driverIndex.get(i)).setThirdPosCount(1);
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(15);
                    break;
                case 3: // winning 4th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(12);
                    break;
                case 4: // winning 5th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(10);
                    break;
                case 5: // winning 6th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(8);
                    break;
                case 6: // winning 7th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(6);
                    break;
                case 7: // winning 8th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(4);
                    break;
                case 8: // winning 9th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(2);
                    break;
                case 9: // winning 10th position
                    drivers.get(driverIndex.get(i)).setRacesCount(1);
                    drivers.get(driverIndex.get(i)).setTotalPoints(1);
                    break;


            }
        }
        try {
            saveData();
//            orderTableDesc(table);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//            raceRound++;

    }

    @Override
    public void displayStats() { // text field for selected driver?

    }

    @Override
    public void saveData() throws FileNotFoundException {
        try {
            // create the writer to print to .txt file
            BufferedWriter driverWriter = new BufferedWriter(new FileWriter(driverData));
//            BufferedWriter raceRoundWriter = new BufferedWriter(new FileWriter(raceRound));
//            raceRoundWriter.write(Integer.toString(raceRound));
            //loop through array. Write the elements to the file
            for (int i = 0; i < drivers.size(); i++) {
                if (drivers.get(i) != null) {
                    driverWriter.write(drivers.get(i).getFName() + "\n");
                    driverWriter.write(drivers.get(i).getLName() + "\n");
                    driverWriter.write(drivers.get(i).getCountry() + "\n");
                    driverWriter.write(drivers.get(i).getTeam() + "\n");
                    driverWriter.write(drivers.get(i).getTotalPoints() + "\n");
                    driverWriter.write(drivers.get(i).getFirstPosCount() + "\n");
                    driverWriter.write(drivers.get(i).getSecondPosCount() + "\n");
                    driverWriter.write(drivers.get(i).getThirdPosCount() + "\n");
                    driverWriter.write(drivers.get(i).getRacesCount() + "\n");


                }
            }
            // closing file connection
            driverWriter.close();
//            raceRoundWriter.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void loadData() throws FileNotFoundException { // for loading data into ArrayList
        try {

            Scanner driverScan = new Scanner(driverData);
//            Scanner raceRoundScan = new Scanner(savedRaceRound);
//            Scanner raceDateScan = new Scanner(dates);
//            while (raceRoundScan.hasNext()) { // own while loop for getting raceRound int from .txt
//                String raceRoundTemp = raceRoundScan.nextLine();
//                raceRound = Integer.parseInt(raceRoundTemp);
//            }

            while (driverScan.hasNext()) {
                String fileLine1 = driverScan.nextLine();
                String fileLine2 = driverScan.nextLine();
                String fileLine3 = driverScan.nextLine();
                String fileLine4 = driverScan.nextLine();
                String fileLine5 = driverScan.nextLine();
                String fileLine6 = driverScan.nextLine();
                String fileLine7 = driverScan.nextLine();
                String fileLine8 = driverScan.nextLine();
                String fileLine9 = driverScan.nextLine();

                drivers.add(new Formula1Driver(fileLine1, fileLine2, fileLine3, fileLine4, Integer.parseInt(fileLine5), Integer.parseInt(fileLine6), Integer.parseInt(fileLine7), Integer.parseInt(fileLine8), Integer.parseInt(fileLine9)));

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

// potential reference for comparator for displayTable: https://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
class TotalPointsComparatorDescending implements Comparator<Formula1Driver> {
    public int compare(Formula1Driver d1, Formula1Driver d2) {
        return d1.getTotalPoints() == d2.getTotalPoints() ? d2.getFirstPosCount() - d1.getFirstPosCount() : d2.getTotalPoints()
                - d1.getTotalPoints();
    }
}

class TotalPointsComparatorAscending implements Comparator<Formula1Driver> {
    public int compare(Formula1Driver d1, Formula1Driver d2) {
        return d1.getTotalPoints() == d2.getTotalPoints() ? d2.getFirstPosCount() - d1.getFirstPosCount() : d1.getTotalPoints()
                - d2.getTotalPoints();
    }
}

class FirstPosWinsComparatorDescending implements Comparator<Formula1Driver> {
    public int compare(Formula1Driver d1, Formula1Driver d2) {
        return d2.getFirstPosCount() - d1.getFirstPosCount();
    }
}

//    File savedDriverData = new File("savedData.txt");
//    File savedRaceRound = new File("savedRaceRound.txt");
//    private ArrayList<Formula1Driver> drivers = new ArrayList<>();
//    private final int MAX_DRIVERS_COUNT = 10;
//    private int raceRound = 0;
//
//    public static void main(String[] args) {
//        boolean loop = true;
//        Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
//        try {
//            manager.loadData(); // reads all information saved in the previous file (resume/recover previous state)
//
//            while (loop) {
//                Scanner scanner = new Scanner(System.in);
//                System.out.println("Welcome to the F1 Championship menu!");
//                manager.menu();
//
//                switch (Integer.parseInt(scanner.next())) {
//                    case 1:
//                        manager.createDriver();
//                        break;
//                    case 2:
//                        manager.deleteDriver();
//                        break;
//                    case 3:
//                        manager.changeDriver();
//                        break;
//                    case 4:
//                        manager.displayStats();
//                        break;
//                    case 5:
//                        manager.displayTable();
//                        break;
//                    case 6:
//                        manager.race();
//                        break;
//                    case 7:
//                        manager.saveData();
//                        break;
//                    case 8:
//                        System.out.println("Goodbye!");
//                        loop = false;
//                        break;
//
//                    default:
//                        System.out.println("Please select a value from 1-9");
//                        break;
//                }
//            }
//        } catch (NumberFormatException | FileNotFoundException fnf) {
//            System.out.println("Invalid input entered, please enter an integer.");
//        }
//    }
//
//
//    @Override
//    public void createDriver() {
//        Scanner input = new Scanner(System.in);
//        if (drivers.size() < MAX_DRIVERS_COUNT) {
//
//            System.out.println("Enter first name of driver: ");
//            String fName = input.nextLine();
//            System.out.println("Enter last name of driver: ");
//            String lName = input.nextLine();
//            System.out.println("Enter country where driver is from: ");
//            String country = input.nextLine();
//            System.out.println("Enter driver's team: ");
//            String team = input.nextLine(); // if statement to check if trying to add someone on a team that already exists?
//            System.out.println("Enter total points : ");
//
//            drivers.add(new Formula1Driver(fName, lName, country, team));
//
//            System.out.println(fName + " " + lName + " from " + country + " on team " + team +
//                    " has been added to the championship!");
//
//        } else {
//            System.out.println("Reached maximum no. of drivers allowed in the championship! " +
//                    "You may remove a driver to free up space.");
//        }
//
//
//    }
//
//    @Override
//    public void deleteDriver() {
//        Scanner input = new Scanner(System.in);
//        try {
//            displayTable();
//            System.out.println("Select a Driver by their Driver Number to remove them from the championship: ");
//            int user_choice = input.nextInt();
//            if (!(user_choice > 0 && user_choice <= 10)) {
//                System.out.println("Incorrect range entered. Only 1-10");
//            } else {
//                System.out.println("F1 Driver, " + drivers.get(user_choice - 1).getName() + ", has been removed from the" +
//                        " championship.");
//                drivers.remove(user_choice - 1);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Could not find existing Driver Number. Please enter an integer to identify a driver in the list.");
//            input.nextLine();
//        }
//    }
//
//    @Override
//    public void changeDriver() { // this only swaps their position in the list.. doesnt actually swap teams?
//        // done that so they basicaly only swap teams... now, also swap points too? or leave it?
//        // NEED TO FIX: Driver should not be able to swap with themselves
//        Scanner input = new Scanner(System.in);
//        try {
//            displayTable();
//            System.out.println("Select first Driver Number to swap: ");
//            int driver1 = input.nextInt();
//            System.out.println("Select second Driver Number to swap: ");
//            int driver2 = input.nextInt();
//            if (!(driver1 > 0 && driver1 <= 10) && !(driver2 > 0 && driver2 <= 10)) {
//                System.out.println("Incorrect range entered. Enter an integer from 1-10.");
//            } else {
//                //better way of assigning values to temp maybe? ask adam... this works tho for now
//                Formula1Driver temp = new Formula1Driver(drivers.get(driver1 - 1).getFName(), drivers.get(driver1 - 1).getLName(), drivers.get(driver1 - 1).getCountry(), drivers.get(driver1 - 1).getTeam());
//                drivers.get(driver1 - 1).setFName(drivers.get(driver2 - 1).getFName());
//                drivers.get(driver1 - 1).setLName(drivers.get(driver2 - 1).getLName());
//                drivers.get(driver1 - 1).setCountry(drivers.get(driver2 - 1).getCountry());
////                drivers.get(driver1 - 1).setTeam(drivers.get(driver2 - 1).getTeam());
//
//                drivers.get(driver2 - 1).setFName(temp.getFName());
//                drivers.get(driver2 - 1).setLName(temp.getLName());
//                drivers.get(driver2 - 1).setCountry(temp.getCountry());
////                drivers.get(driver2 - 1).setTeam(temp.getTeam());
//
//                System.out.println("F1 Driver, " + drivers.get(driver2 - 1).getName() + " , of team " +
//                        drivers.get(driver2 - 1).getTeam() + " has been SWAPPED with \nF1 Driver, " + drivers.get(driver1 - 1).getName() +
//                        " , of team " + drivers.get(driver1 - 1).getTeam() + "!");
//            }
//
//        } catch (Exception e) {
//            System.out.println("Could not find existing Driver Number. Please enter an integer to identify a driver in the list.");
//            input.nextLine();
//        }
//
//    }
//
//    @Override
//    public void displayStats() {
//        Scanner input = new Scanner(System.in);
//        System.out.println("\nSearch for a Driver by entering their first then last name.\n");
//        System.out.println("Please select a Driver by their first name: ");
//        String fName = input.nextLine();
//        System.out.println("Please select a Driver by their last name: ");
//        String lName = input.nextLine();
//        fName = fName.toLowerCase();
//        lName = lName.toLowerCase();
//        boolean isMatching = false;
//        for (Formula1Driver f : drivers) {
//            if (f.getFName().toLowerCase().equals(fName) && f.getLName().toLowerCase().equals(lName)) {
//                System.out.println("\n|=======+==================================+=======|\n");
//                System.out.println("Driver: " + f.getName() + "\nCountry: " + f.getCountry() + "\nTeam: "
//                        + f.getTeam() + "\nFirst position wins: " + f.getFirstPosCount() + "\nSecond position wins: " +
//                        f.getSecondPosCount() + "\nThird position wins: " + f.getThirdPosCount() + "\nTotal Points: "
//                        + f.getTotalPoints() + "\nRaces completed: " + f.getRacesCount());
//                isMatching = true;
//                System.out.println("\n|=======+==================================+=======|\n");
//            }
//        }
//        if (!isMatching) {
//            System.out.println("No Driver was found. First or last name may have been entered incorrectly.\n");
//
//        }
//    }
//
//    @Override
//    public void displayTable() {
//        System.out.println("\n|=======+==================================+=======|\n");
//        drivers.sort(new TotalPointsComparator());
//        for (int i = 0; i < drivers.size(); i++) {
//            System.out.println("Driver Number = " + (i + 1));
//            System.out.println("Driver: " + drivers.get(i).getName() + "\nCountry: " + drivers.get(i).getCountry() + "\n" +
//                    "Team: " + drivers.get(i).getTeam() + "\nTotal Points this season: " + drivers.get(i).getTotalPoints() +
//                    "\nFirst Position wins: " + drivers.get(i).getFirstPosCount() + "\nSecond Position wins: " + drivers.get(i).getSecondPosCount()
//                    + "\nThird Position wins: " + drivers.get(i).getThirdPosCount()
//                    + "\nRaces completed: " + drivers.get(i).getRacesCount() + "\n");
//        }
//        System.out.println("|=======+==================================+=======|\n");
//    }
//
//    @Override
//    public void race() { // need to display positions of all drivers, and the current date of the race, current round?
//        int difference = MAX_DRIVERS_COUNT - drivers.size();
//
//        if (drivers.size() < MAX_DRIVERS_COUNT) {
//            System.out.println("\n|=======+==================================+=======|\n");
//            System.out.println("Cannot race yet --- Please add " + difference + " more driver(s)!\n");
//            System.out.println("|=======+==================================+=======|\n");
//        } else {
//            ArrayList<Integer> driverIndex = new ArrayList<>();
//            Random rand = new Random();
//            for (int i; driverIndex.size() != 10; ) { // populating driverIndex with uniquely random ints 0-9
//                i = rand.nextInt(drivers.size());
//                if (driverIndex.contains(i)) {
//                    while (driverIndex.contains(i)) {
//                        i = rand.nextInt(drivers.size());
//                    }
//                    //while loop breaks, then we add driver to driverIndex arrayList
//                }
//                driverIndex.add(i);
//            }
//            for (int i = 0; i < drivers.size(); i++) {
//                System.out.println(driverIndex);
//                switch (i) {
//                    case 0: // winning 1st position
//                        drivers.get(driverIndex.get(i)).setFirstPosCount(1);
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(25);
//                        break;
//                    case 1: // winning 2nd position
//                        drivers.get(driverIndex.get(i)).setSecondPosCount(1);
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(18);
//                        break;
//                    case 2: // winning 3rd position
//                        drivers.get(driverIndex.get(i)).setThirdPosCount(1);
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(15);
//                        break;
//                    case 3: // winning 4th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(12);
//                        break;
//                    case 4: // winning 5th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(10);
//                        break;
//                    case 5: // winning 6th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(8);
//                        break;
//                    case 6: // winning 7th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(6);
//                        break;
//                    case 7: // winning 8th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(4);
//                        break;
//                    case 8: // winning 9th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(2);
//                        break;
//                    case 9: // winning 10th position
//                        drivers.get(driverIndex.get(i)).setRacesCount(1);
//                        drivers.get(driverIndex.get(i)).setTotalPoints(1);
//                        break;
//
//
//                }
//            }
//            raceRound++;
//        }
//    }
//
//    @Override
//    public void saveData() throws FileNotFoundException { // also save round count? which race its currently on
//        try {
//
//            // create the writer to print to .txt file
//            BufferedWriter driverWriter = new BufferedWriter(new FileWriter(savedDriverData));
//            BufferedWriter raceRoundWriter = new BufferedWriter(new FileWriter(savedRaceRound));
//            raceRoundWriter.write(Integer.toString(raceRound));
//            //loop through array. Write the elements to the file
//            for (int i = 0; i < drivers.size(); i++) {
//                if (drivers.get(i) != null) {
//                    driverWriter.write(drivers.get(i).getFName() + "\n");
//                    driverWriter.write(drivers.get(i).getLName() + "\n");
//                    driverWriter.write(drivers.get(i).getCountry() + "\n");
//                    driverWriter.write(drivers.get(i).getTeam() + "\n");
//                    driverWriter.write(drivers.get(i).getFirstPosCount() + "\n");
//                    driverWriter.write(drivers.get(i).getSecondPosCount() + "\n");
//                    driverWriter.write(drivers.get(i).getThirdPosCount() + "\n");
//                    driverWriter.write(drivers.get(i).getTotalPoints() + "\n");
//                    driverWriter.write(drivers.get(i).getRacesCount() + "\n");
//
//
//                }
//            }
//            // closing file connection
//            driverWriter.close();
//            raceRoundWriter.close();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public void loadData() throws FileNotFoundException {
//        try {
//            if (drivers.size() > 0) { // if current state of data contains drivers, delete them
//                drivers.clear();
//            }
//
//            Scanner driverScan = new Scanner(savedDriverData);
//            Scanner raceRoundScan = new Scanner(savedRaceRound);
//            while (raceRoundScan.hasNext()) {
//                String raceRoundTemp = raceRoundScan.nextLine();
//                raceRound = Integer.parseInt(raceRoundTemp);
//            }
//
//            while (driverScan.hasNext()) {
//                String fileLine1 = driverScan.nextLine();
//                String fileLine2 = driverScan.nextLine();
//                String fileLine3 = driverScan.nextLine();
//                String fileLine4 = driverScan.nextLine();
//                String fileLine5 = driverScan.nextLine();
//                String fileLine6 = driverScan.nextLine();
//                String fileLine7 = driverScan.nextLine();
//                String fileLine8 = driverScan.nextLine();
//                String fileLine9 = driverScan.nextLine();
//
//                drivers.add(new Formula1Driver(fileLine1, fileLine2, fileLine3, fileLine4, Integer.parseInt(fileLine5), Integer.parseInt(fileLine6), Integer.parseInt(fileLine7), Integer.parseInt(fileLine8), Integer.parseInt(fileLine9)));
//
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public void menu() {
//        System.out.println("--------------------------------------------");
//        System.out.println("[1]: Create a new driver");
//        System.out.println("[2]: Delete a driver");
//        System.out.println("[3]: Change an existing driver");
//        System.out.println("[4]: Display statistics for a selected driver");
//        System.out.println("[5]: Display table of statistics for entire competition");
//        System.out.println("[6]: Launch a race");
//        System.out.println("[7]: Save current state of competition to memory");
//        System.out.println("[8]: Exit program");
//        System.out.println("--------------------------------------------");
//        System.out.print("Please enter a number: ");
//    }
