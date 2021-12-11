package com.company;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;

public interface ChampionshipManager {


    void displayStats();

    void orderTableDesc() throws FileNotFoundException;

    void orderTableAsc(JTable table) throws FileNotFoundException;

    void orderTableFirstWinsDesc(JTable table) throws FileNotFoundException;

    void race();

    void saveDataToTextFile() throws FileNotFoundException;

    void loadDataToDrivers(File file) throws FileNotFoundException;

}
