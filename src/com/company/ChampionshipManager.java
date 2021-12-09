package com.company;

import javax.swing.*;
import java.io.FileNotFoundException;

public interface ChampionshipManager {


    void displayStats();

    void orderTableDesc(JTable table) throws FileNotFoundException;

    void orderTableAsc(JTable table) throws FileNotFoundException;

    void orderTableFirstWinsDesc(JTable table) throws FileNotFoundException;

    void race();

    void saveData() throws FileNotFoundException;

    void loadData() throws FileNotFoundException;

}
