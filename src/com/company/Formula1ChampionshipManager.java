package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Formula1ChampionshipManager extends JPanel implements ChampionshipManager, ActionListener {

    final int MAX_NO_DRIVERS = 10;
    final int MAX_NO_ROUNDS = 15;
    ArrayList<Formula1Driver> drivers = new ArrayList<>();
    // defining file names for easy use in methods at later stages
    File driverData = new File("driverData.txt");
    File driverDataBackup = new File("driverDataBackup.txt");
    File raceRoundSaved = new File("raceRound.txt");
    // Specific dates for when the next race occurs is found using raceRound tracker.
    String[][] dates = {{"03", "04", "2021"}, {"10", "04", "2021"}, {"17", "04", "2021"}, {"24", "04", "2021"}, {"31", "04", "2021"},
            {"07", "05", "2021"}, {"07", "05", "2021"}, {"14", "05", "2021"}, {"21", "05", "2021"}, {"28", "05", "2021"},
            {"04", "06", "2021"}, {"11", "06", "2021"}, {"18", "06", "2021"}, {"25", "06", "2021"}, {"01", "07", "2021"}};
    private int raceRound = 0;
    private int day;
    private int month;
    private int year;

    JFrame frame;
    JTable table;

    JMenuBar menuBar;
    JMenu resetData;
    JMenuItem reset;
    // initialise all buttons
    JButton buttonOrderTableDesc = new JButton("Order Table by Points (Desc)");
    JButton buttonOrderTableAsc = new JButton("Order Table by Points (Asc)");
    JButton buttonOrderTableFirstWinsDesc = new JButton("Order Table by First Wins");
    JButton buttonRace = new JButton("Lets race!");
    JButton buttonBoostedRace = new JButton("Boosted race!");
    JButton buttonRaceHistory = new JButton("View Race History!");
    JButton buttonSearchDriver = new JButton("Search");
    JTextField searchDriver = new JTextField();
    //initialise all main panels and labels in GUI.
    JPanel tablePanel = new JPanel();
    JPanel menuPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel raceDisplayPanel = new JPanel();
    JLabel titleLabel = new JLabel("Formula 1 Championship");
    JLabel dateLabel = new JLabel();
    JLabel[] raceLabel = new JLabel[MAX_NO_ROUNDS];

    // run GUI in main method.
    public static void main(String[] args) throws FileNotFoundException {
        new Formula1ChampionshipManager();
    }

    // run main GUI using constructor.
    public Formula1ChampionshipManager() throws FileNotFoundException {
        // creating main frame window to add all components
        frame = new JFrame("Formula 1 Championship Manager 2021");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setting up menuBar.
        menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        resetData = new JMenu("Reset Data");
        menuBar.add(resetData);
        reset = new JMenuItem("Reset all values");
        resetData.add(reset);
        reset.addActionListener(this);

        // setting cosmetics for title panel.
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEtchedBorder());

        // adding menu buttons in vertical axis to menuPanel and adding action listeners to register events
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(buttonOrderTableDesc);
        buttonOrderTableDesc.addActionListener(this);
        menuPanel.add(buttonOrderTableAsc);
        buttonOrderTableAsc.addActionListener(this);
        menuPanel.add(buttonOrderTableFirstWinsDesc);
        buttonOrderTableFirstWinsDesc.addActionListener(this);
        menuPanel.add(buttonRace);
        buttonRace.addActionListener(this);
        menuPanel.add((buttonRaceHistory));
        buttonRaceHistory.addActionListener(this);
        menuPanel.add(buttonBoostedRace);
        buttonBoostedRace.addActionListener(this);
        //title panel placed north of the frame
        titlePanel.add(buttonSearchDriver);
        buttonSearchDriver.addActionListener(this);
        searchDriver.setPreferredSize(new Dimension(250, 30));
        titlePanel.add(searchDriver);

        // design for menu button panel
        menuPanel.setBackground(Color.lightGray);
        menuPanel.setBorder(BorderFactory.createMatteBorder(270, 30, 200, 30, Color.lightGray));
        //creating JTable contents, currently empty values but is then loaded in from a file to fill in table as GUI is launched.
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
        tablePanel.setBackground(Color.lightGray);


        // assigning panels to top, bottom, left etc. areas of the frame container
        frame.getContentPane().add(raceDisplayPanel, "Center");
        frame.getContentPane().add(tablePanel, "South");
        frame.getContentPane().add(titlePanel, "North");
        frame.getContentPane().add(menuPanel, "West");


        dateLabel.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        raceDisplayPanel.add(dateLabel);
        // initialise JLabels used to display race results to raceDisplayPanel.
        for (int i = 0; i < MAX_NO_ROUNDS; i++) {
            raceLabel[i] = new JLabel();
            raceLabel[i].setFont(new Font("Sans-Serif", Font.BOLD, 14));
            raceDisplayPanel.add(raceLabel[i]);
        }
        // NOTE:
        // load in data from driverData.txt into drivers ArrayList to be used in races and in cases of previous records.
        // i.e., any previous runs of the program will be saved, and if the program is launched again, those records are
        // automatically fed into the program. If reset is desired, use menu function in top left of GUI.
        loadDataToDrivers(driverData);
        updateTableAtRun();

        // Adding panel to display result of race
        raceDisplayPanel.setLayout(new BoxLayout(raceDisplayPanel, BoxLayout.Y_AXIS));
        raceDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // setting to disable editing cells in table
        table.setDefaultEditor(Object.class, null);
        frame.setResizable(false);
        frame.setSize(1280, 900);
        frame.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == buttonOrderTableDesc) {
                orderTableDesc();
            } else if (e.getSource() == buttonOrderTableAsc) {
                orderTableAsc(table);
            } else if (e.getSource() == buttonOrderTableFirstWinsDesc) {
                orderTableFirstWinsDesc(table);
            } else if (e.getSource() == buttonRace) {
                race();
            } else if (e.getSource() == reset) {
                resetValues();
                orderTableDesc();
            } else if (e.getSource() == buttonBoostedRace) {
                boostedRace();
                orderTableDesc();
            } else if (e.getSource() == buttonSearchDriver) {

            } else if (e.getSource() == buttonRaceHistory) {
                displayRaceHistory();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void displayRaceHistory() {
        if (raceRound == 0) {
            JOptionPane.showMessageDialog(null, "No races have taken place yet. No history to show!"
                    , "No races available", JOptionPane.WARNING_MESSAGE);
        } else {
            dateLabel.setText("");
            // clearing panel of any positions from races, clean page
            for (int i = 0; i < MAX_NO_DRIVERS; i++) {
                raceLabel[i].setText("");
            }
            //displaying all the current dates in ascending order using raceRound tracker
            for (int i = 0; i < raceRound; i++) {
                raceLabel[i].setText("<html>" + "Race round: " + (i + 1) + " of the season" + "<br>" +
                        "Date race took place: " + dates[i][0] + "/" + dates[i][1] + "/" + dates[i][2] + "<br>");
            }

        }
    }


    public void updateTableAtRun() throws FileNotFoundException {
        //populates the table with empty values for statistics, but fills up drivers, ready to take races.
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < 10; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }
        // used to update table state
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    public void resetValues() {
        // drivers array list (Formula1Driver objects) are cleared and re-populated again with set names and empty values.
        drivers.clear();
        loadDataToDrivers(driverDataBackup);
        // clearing page of any remaining text labels
        for (int i = 0; i < MAX_NO_ROUNDS; i++) {
            raceLabel[i].setText("");
        }
        dateLabel.setText("");
        raceRound = 0;
        JOptionPane.showMessageDialog(null, "All values for statistics in the season have been reset.", "Successful reset",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void orderTableDesc() throws FileNotFoundException {
        // sort objects in regard to points in descending order then write sorted data to text file
        drivers.sort(new TotalPointsComparatorDescending());
        saveDataToTextFile();
        // read in sorted data from text file and re-populate table with updated values
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < MAX_NO_DRIVERS; i++) { // 10 rows (drivers)
            for (int j = 0; j < 9; j++) { // 9 columns (values)
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();

    }

    @Override
    public void orderTableAsc(JTable table) throws FileNotFoundException {
        // sort objects regarding points in ascending order then write sorted data to text file
        drivers.sort(new TotalPointsComparatorAscending());
        saveDataToTextFile();
        // read in sorted data from text file and re-populate table with updated values
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < MAX_NO_DRIVERS; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    @Override
    public void orderTableFirstWinsDesc(JTable table) throws FileNotFoundException {
        // sort objects in regards to first wins count in descending order then write sorted data to text file
        drivers.sort(new FirstPosWinsComparatorDescending());
        saveDataToTextFile();
        // read in sorted data from text file and re-populate table with updated values
        Scanner driverScan = new Scanner(driverData);
        for (int i = 0; i < MAX_NO_DRIVERS; i++) { // 10 rows
            for (int j = 0; j < 9; j++) { // 9 columns
                table.setValueAt(driverScan.nextLine(), i, j);
            }

        }

        tablePanel.revalidate();
        tablePanel.repaint();

    }

    @Override
    public void race() {

        if (raceRound < MAX_NO_ROUNDS) {
            // clearing panel of any positions from races, clean page
            for (int i = 0; i < MAX_NO_ROUNDS; i++) {
                raceLabel[i].setText("");
            }
            // creating array to hold random numbers which will pick a random driver from main arraylist of objects
            // to be placed 1st, 2nd , 3rd etc.
            ArrayList<Integer> driverIndex = new ArrayList<>();
            Random rand = new Random();

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
            // assignment of respective points using index of a random integer from driverIndex to choose which driver
            // from "drivers" array list of objects will be 1st, 2nd, 3rd, etc...
            for (int i = 0; i < drivers.size(); i++) {
                switch (i) {
                    case 0: // finishing 1st position
                        drivers.get(driverIndex.get(i)).setFirstPosCount(1);
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(25);
                        break;
                    case 1: // finishing 2nd position
                        drivers.get(driverIndex.get(i)).setSecondPosCount(1);
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(18);
                        break;
                    case 2: // finishing 3rd position
                        drivers.get(driverIndex.get(i)).setThirdPosCount(1);
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(15);
                        break;
                    case 3: // finishing 4th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(12);
                        break;
                    case 4: // finishing 5th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(10);
                        break;
                    case 5: // finishing 6th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(8);
                        break;
                    case 6: // finishing 7th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(6);
                        break;
                    case 7: // finishing 8th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(4);
                        break;
                    case 8: // finishing 9th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(2);
                        break;
                    case 9: // finishing 10th position
                        drivers.get(driverIndex.get(i)).setRacesCount(1);
                        drivers.get(driverIndex.get(i)).setTotalPoints(1);
                        break;


                }
                // display results of positions and drivers in the raceDisplayPanel
                raceLabel[i].setText("<html>" + "<br>" + drivers.get(driverIndex.get(i)).getName() + "        [" +
                        drivers.get(driverIndex.get(i)).getTeam() + "]"
                        + "<br>Position: " + (i + 1));
            }
            // finding which date the race is currently it on and displaying it in the raceDisplayPanel
            day = Integer.parseInt(dates[raceRound][0]);
            month = Integer.parseInt(dates[raceRound][1]);
            year = Integer.parseInt(dates[raceRound][2]);

            raceRound++;
            dateLabel.setText("Round " + String.valueOf(raceRound) + " of the season. Date: --- " + day + "/" + month + "/" +
                    year + " ---");

            try {
                // update driverData.txt file with new statistics from drivers array-list Formula1Driver objects
                saveDataToTextFile();
                // by default, update the table ordered by descending order for viewer to verify points
                orderTableDesc();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "The season has ended! Cannot race anymore."
                    , "Cannot race!", JOptionPane.WARNING_MESSAGE);
        }

    }

    public void boostedRace() {

        if (raceRound < MAX_NO_ROUNDS) {
            // clearing panel of any positions from races, clean page
            for (int i = 0; i < MAX_NO_ROUNDS; i++) {
                raceLabel[i].setText("");
            }
            // arraylist to assign randomly picked drivers in 1st pos, 2nd pos, 3rd pos, etc...
            ArrayList<Formula1Driver> randomlySetDriverPositions = new ArrayList<>();
            ArrayList<Integer> driverIndex = new ArrayList<>();
            Random rand = new Random();
            // defining main random number generator used to calculate who will win first position based on chance
            double rng = Math.random();
            rng = Math.round(rng * 100.0) / 100.0;

            for (int i; driverIndex.size() != 10; ) { // populating driverIndex with uniquely random ints 0-9
                i = rand.nextInt(drivers.size());
                if (driverIndex.contains(i)) {
                    while (driverIndex.contains(i)) {
                        i = rand.nextInt(drivers.size());
                    }
                    //while loop breaks as it is a unique int, so we add driver to driverIndex arrayList as seen below
                }
                driverIndex.add(i);
            }
            //populating array of random driver starting positions of all 10 drivers, although driver 10 has 0% chance.
            // Driver in 0th index will be in first position (40% chance to win), 1st index driver will be starting in second position
            // (30% chance to win), etc...
            for (int i = 0; i < drivers.size(); i++) {
                randomlySetDriverPositions.add(drivers.get(driverIndex.get(i)));
            }
            // ease of access to view results correlate correctly in console (positions still displayed in GUI).
            System.out.println(rng);
            System.out.println(driverIndex);
            for (Formula1Driver f : randomlySetDriverPositions) {
                System.out.println(f.getName());
            }
            // Conditions to check who will win based on RNG.
            if (rng >= 0.6 && rng <= 1.0) { // 40% chance: driver in 1st pos (STARTING POSITION etc.)
                randomlySetDriverPositions.get(0).setTotalPoints(25);
                randomlySetDriverPositions.get(0).setFirstPosCount(1);
                randomlySetDriverPositions.get(0).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(0).getName() + "        [" +
                        randomlySetDriverPositions.get(0).getTeam() + "]"
                        + "<br>Position: " + (1));
                // removing winning driver as the rest of the drivers must be randomised, do not want to include winner.
                randomlySetDriverPositions.remove(0);
            }
            if (rng >= 0.3 && rng <= 0.6) { // 30% chance: driver in 2nd pos.
                randomlySetDriverPositions.get(1).setTotalPoints(25);
                randomlySetDriverPositions.get(1).setFirstPosCount(1);
                randomlySetDriverPositions.get(1).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(1).getName() + "        [" +
                        randomlySetDriverPositions.get(1).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(1);
            }
            if (rng >= 0.2 && rng <= 0.3) { // 10% chance: driver in 3rd pos.
                randomlySetDriverPositions.get(2).setTotalPoints(25);
                randomlySetDriverPositions.get(2).setFirstPosCount(1);
                randomlySetDriverPositions.get(2).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(2).getName() + "        [" +
                        randomlySetDriverPositions.get(2).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(2);
            }
            if (rng >= 0.1 && rng <= 0.2) { // 10% chance: driver in 4th pos.
                randomlySetDriverPositions.get(3).setTotalPoints(25);
                randomlySetDriverPositions.get(3).setFirstPosCount(1);
                randomlySetDriverPositions.get(3).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(3).getName() + "        [" +
                        randomlySetDriverPositions.get(3).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(3);
            }

            if (rng >= 0.08 && rng <= 0.1) { // 2% chance: driver in 5th pos.
                randomlySetDriverPositions.get(4).setTotalPoints(25);
                randomlySetDriverPositions.get(4).setFirstPosCount(1);
                randomlySetDriverPositions.get(4).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(4).getName() + "        [" +
                        randomlySetDriverPositions.get(4).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(4);
            }
            if (rng >= 0.06 && rng <= 0.08) { // 2% chance: driver in 6th pos.
                randomlySetDriverPositions.get(5).setTotalPoints(25);
                randomlySetDriverPositions.get(5).setFirstPosCount(1);
                randomlySetDriverPositions.get(5).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(5).getName() + "        [" +
                        randomlySetDriverPositions.get(5).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(5);
            }

            if (rng >= 0.04 && rng <= 0.06) { // 2% chance: driver in 7th pos.
                randomlySetDriverPositions.get(6).setTotalPoints(25);
                randomlySetDriverPositions.get(6).setFirstPosCount(1);
                randomlySetDriverPositions.get(6).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(6).getName() + "        [" +
                        randomlySetDriverPositions.get(6).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(6);
            }

            if (rng >= 0.02 && rng <= 0.04) { // 2% chance: driver in 8th pos.
                randomlySetDriverPositions.get(7).setTotalPoints(25);
                randomlySetDriverPositions.get(7).setFirstPosCount(1);
                randomlySetDriverPositions.get(7).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(7).getName() + "        [" +
                        randomlySetDriverPositions.get(7).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(7);
            }
            if (rng >= 0.00 && rng <= 0.02) { // 2% chance: driver in 9th pos.
                randomlySetDriverPositions.get(8).setTotalPoints(25);
                randomlySetDriverPositions.get(8).setFirstPosCount(1);
                randomlySetDriverPositions.get(8).setRacesCount(1);
                raceLabel[0].setText("<html>" + "<br>" + randomlySetDriverPositions.get(8).getName() + "        [" +
                        randomlySetDriverPositions.get(8).getTeam() + "]"
                        + "<br>Position: " + (1));
                randomlySetDriverPositions.remove(8);
            }   //0% chance: driver in 10th position

            // assigning the rest of the drivers their respective points for the race
            // Shuffle means to now say that the rest of the drivers have raced, here are their finishing positions for this
            // race.
            Collections.shuffle(randomlySetDriverPositions);
            // ease of access for testing purposes in console (drivers still display and table is updated)
            System.out.println("--- randomlySetDriverPositions---");
            for (Formula1Driver f : randomlySetDriverPositions) {
                System.out.println(f.getName());
            }
            // as the array was shuffled as seen above, the drivers are no longer in chronological order; they are in
            // random finishing positions. Points are now assigned to respective finishing positions
            for (int i = 0; i < randomlySetDriverPositions.size(); i++) {
                switch (i) {
                    case 0: // finishing in 2nd position
                        randomlySetDriverPositions.get(i).setTotalPoints(18);
                        randomlySetDriverPositions.get(i).setSecondPosCount(1);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 1: // finishing in 3rd position
                        randomlySetDriverPositions.get(i).setTotalPoints(15);
                        randomlySetDriverPositions.get(i).setThirdPosCount(1);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 2: // finishing in 4th position
                        randomlySetDriverPositions.get(i).setTotalPoints(12);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 3: // finishing in 5th position
                        randomlySetDriverPositions.get(i).setTotalPoints(10);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 4: // finishing in 6th position
                        randomlySetDriverPositions.get(i).setTotalPoints(8);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 5: // finishing in 7th position
                        randomlySetDriverPositions.get(i).setTotalPoints(6);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 6: // finishing in 8th position
                        randomlySetDriverPositions.get(i).setTotalPoints(4);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 7: // finishing in 9th position
                        randomlySetDriverPositions.get(i).setTotalPoints(2);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                    case 8: // finishing in 10th position
                        randomlySetDriverPositions.get(i).setTotalPoints(1);
                        randomlySetDriverPositions.get(i).setRacesCount(1);
                        break;
                }
                raceLabel[i + 1].setText("<html>" + "<br>" + randomlySetDriverPositions.get(i).getName() + "        [" +
                        randomlySetDriverPositions.get(i).getTeam() + "]"
                        + "<br>Position: " + (i + 2));
            }
            try {
                // update driverData.txt file with new statistics from "drivers" array-list Formula1Driver objects
                saveDataToTextFile();
                // by default, update the table ordered by descending order for viewer to verify points
                orderTableDesc();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            day = Integer.parseInt(dates[raceRound][0]);
            month = Integer.parseInt(dates[raceRound][1]);
            year = Integer.parseInt(dates[raceRound][2]);

            raceRound++;
            dateLabel.setText("Round " + (raceRound) + " of the season. Date: --- " + day + "/" + month + "/" +
                    year + " ---");

        } else {
            JOptionPane.showMessageDialog(null, "The season has ended! Cannot race anymore."
                    , "Cannot race!", JOptionPane.WARNING_MESSAGE);
        }

    }

    @Override
    public void saveDataToTextFile() throws FileNotFoundException {
        try {
            BufferedWriter raceRoundWriter = new BufferedWriter(new FileWriter(raceRoundSaved));
            raceRoundWriter.write(Integer.toString(raceRound));

            BufferedWriter driverWriter = new BufferedWriter(new FileWriter(driverData));
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
            driverWriter.close();
            raceRoundWriter.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void loadDataToDrivers(File file) { // for loading data into ArrayList, "drivers". Used when populating the
        // table with default values (first time run).
        try {

            Scanner driverScan = new Scanner(file);
            Scanner raceRoundScan = new Scanner(raceRoundSaved);

            while (raceRoundScan.hasNext()) {
                raceRound = Integer.parseInt(raceRoundScan.nextLine());
            }

            while (driverScan.hasNext()) {
                String fName = driverScan.nextLine();
                String lName = driverScan.nextLine();
                String team = driverScan.nextLine();
                String country = driverScan.nextLine();
                String totalPoints = driverScan.nextLine();
                String firstPosWins = driverScan.nextLine();
                String secondPosWins = driverScan.nextLine();
                String thirdPosWins = driverScan.nextLine();
                String racesCompleted = driverScan.nextLine();
                // "drivers" arraylist initialised with pre-set data of driver names , country etc., and empty values for points etc...
                drivers.add(new Formula1Driver(fName, lName, team, country, Integer.parseInt(totalPoints), Integer.parseInt(firstPosWins), Integer.parseInt(secondPosWins), Integer.parseInt(thirdPosWins), Integer.parseInt(racesCompleted)));

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}

// 3 separate classes used for sorting the table using Comparator interface.
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
        return d1.getFirstPosCount() == d2.getFirstPosCount() ? d2.getTotalPoints() - d1.getTotalPoints() : d2.getFirstPosCount() - d1.getFirstPosCount();
    }
}
