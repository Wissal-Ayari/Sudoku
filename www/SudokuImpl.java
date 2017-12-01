import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SudokuImpl extends UnicastRemoteObject implements SudokuInt {

/**
 * The Sudoku game.
 * To solve the number puzzle, each row, each column, and each of the
 * nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
   // Name-constants for the game properties
   public static final int GRID_SIZE = 9;    // Size of the board
   public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

   public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
   public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0);  // RGB
   public static final Color OPEN_CELL_TEXT_NO = Color.RED;
   public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
   public static final Color CLOSED_CELL_TEXT = Color.BLACK;
   public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

   private int[][] puzzle =
   {{5, 3, 4, 6, 7, 8, 9, 1, 2},
   {6, 7, 2, 1, 9, 5, 3, 4, 8},
   {1, 9, 8, 3, 4, 2, 5, 6, 7},
   {8, 5, 9, 7, 6, 1, 4, 2, 3},
   {4, 2, 6, 8, 5, 3, 7, 9, 1},
   {7, 1, 3, 9, 2, 4, 8, 5, 6},
   {9, 6, 1, 5, 3, 7, 2, 8, 4},
   {2, 8, 7, 4, 1, 9, 6, 3, 5},
   {3, 4, 5, 2, 8, 6, 1, 7, 9}};

   private boolean[][] masks =
      {{true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true},
       {true, true, true, true, true, true, true, true, true}};
       // The value "false" in masks array means a
       // non empty initial cell

   // The game board composes of 9x9 JTextFields,
   // each containing String "1" to "9", or empty String
   private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

   JPanel mainPanel;
   JPanel puzzlePanel;
   JPanel buttonsPanel;
   JButton restartButton;
   JButton quitButton;
   JButton checkButton;
   JFrame jFrame;

   private static int clients;

   /**
    * Constructor to setup the game and the UI Components
    */
    public SudokuImpl() throws RemoteException{
      if(SudokuImpl.clients < 2) {

        clients++;

        mainPanel = new JPanel(new BorderLayout());
        puzzlePanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        buttonsPanel = new JPanel(new GridLayout(1, 3));
        checkButton = new JButton("CHECK");
        restartButton = new JButton("RESTART");
        quitButton = new JButton("Quit");
        jFrame = new JFrame();
      }
      else {
        System.out.println("Exceeded limit of clients.");
      }


    }

    public void setCheckButton() {
            checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean full = true; 
                for (int row = 0; row < GRID_SIZE; ++row) {
                    for (int col = 0; col < GRID_SIZE; ++col) {
                        String s = tfCells[row][col].getText();
                        if(!s.equals("")) {
                            int n = Integer.parseInt(s);
                            if(n == puzzle[row][col])
                                tfCells[row][col].setBackground(Color.GREEN);
							else
                            tfCells[row][col].setBackground(Color.RED);
                        }
                        else {
                            full = false;
                            tfCells[row][col].setBackground(Color.RED);
                        }

                    }
                }
                if(full ) { 
                     int selectedOption = JOptionPane.showConfirmDialog(
                             null,
                             "Congratulations ! Do you want to play again ?" ,
                             "Congratulations",
                             JOptionPane.YES_NO_CANCEL_OPTION);
                    if (selectedOption == JOptionPane.YES_OPTION) {
                      try {
                        jFrame.setVisible(false);
                        Registry reg = LocateRegistry.getRegistry("localhost", 1099);
                        IServer server = (IServer)reg.lookup("Serveur");
                        SudokuImpl su = server.callMeBack();
                        su.fillGrid();
                    }
                    catch(RemoteException r) {

                    }
                    catch(Exception n) {}
                  }
                    if (selectedOption == JOptionPane.CANCEL_OPTION) {
                        System.exit(0);
                    }
                }
              }
            });

    }

    public void setRestartButton() {
            restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              jFrame.setVisible(false);
              SudokuImpl.clients--;
              try {
              SudokuImpl su = new SudokuImpl();
              su.fillGrid();
            }
            catch(RemoteException r) {}
            }
          });
}

    public void setQuitButton() {
            quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              SudokuImpl.clients--;
              if(SudokuImpl.clients == 0)
                System.exit(0);
              else
                jFrame.setVisible(false);
            }
    });
  }

    public void fillGrid() {

        ArrayList<Integer> rows = new ArrayList<Integer>();
        ArrayList<Integer> cols = new ArrayList<Integer>();

        Random rand = new Random();

        // The two arraylists of rows and columns indexes are populated
          // with random values between 0 to 8
        for(int i = 0; i < 20; i++) {
            rows.add(rand.nextInt(9));
            cols.add(rand.nextInt(9));
        }

        for(int i = 0; i < 20; i++) {

            masks[rows.get(i)][cols.get(i)] = false;
              // The value "false" in masks array means a
              // non empty initial cell

        }

      // Construct 9x9 JTextFields and add to the content-pane
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                tfCells[row][col] = new JTextField(); // Allocate element of array
                puzzlePanel.add(tfCells[row][col]);

                if (masks[row][col]) {
                   tfCells[row][col].setText("");     // set to empty string
                   tfCells[row][col].setEditable(true);
                   tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

                }
                else {
                   tfCells[row][col].setText(puzzle[row][col] + "");
                   tfCells[row][col].setEditable(false);
                   tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
                   tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
                }

                final JTextField field = tfCells[row][col];

                field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    String s = field.getText();
                    int n = 0;
                    try {
                        n = Integer.parseInt(s);
                        if(n < 1 || n > 9) {
                          JOptionPane.showMessageDialog(null, "Value must be between 1 and 9");
                          System.out.println("Error : entered value is not between 1 and 9");
                        }

                    }
                    catch(NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Not a number , Try Again");
                        System.out.println("Error : entered value is not a number");
                    }

                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    String s = field.getText();
                    int n = 0;
                    try {
                        n = Integer.parseInt(s);
                        if(n < 1 || n > 9)
                          JOptionPane.showMessageDialog(null, "Value must be between 1 and 9");
                    }
                    catch(NumberFormatException ex) {
                         JOptionPane.showMessageDialog(null, "Not a number , Try Again");
                     }
                }
        });

        // Beautify all the cells
        tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
        tfCells[row][col].setFont(FONT_NUMBERS);
        }
    }


    mainPanel.add(puzzlePanel, BorderLayout.NORTH);
    buttonsPanel.add(checkButton);
    buttonsPanel.add(restartButton);
    buttonsPanel.add(quitButton);
    mainPanel.add(buttonsPanel, BorderLayout.CENTER);
    setCheckButton();
    setRestartButton();
    setQuitButton();
    jFrame.setSize(400,400);
    jFrame.add(mainPanel);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.setVisible(true);
}


    public boolean checkSudokuStatus(int[][] grid) {
        for (int i = 0; i < 9; i++) {

            int[] row = new int[9];
            int[] square = new int[9];
            int[] column = grid[i].clone();

            for (int j = 0; j < 9; j ++) {
                row[j] = grid[j][i];
                square[j] = grid[(i / 3) * 3 + j / 3][i * 3 % 9 + j % 3];
            }
            if (!(validate(column) && validate(row) && validate(square)))
                return false;
        }
        return true;
    }

    public boolean validate(int[] check) {
        int i = 0;
        Arrays.sort(check);
        for (int number : check) {
            if (number != ++i)
                return false;
        }
        return true;
    }
}
