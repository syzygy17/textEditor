package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    final int WIDTH = 800;
    final int HEIGHT = 800;
    final int KEY_SIZE = 30;
    final String OPEN_ICON_FILE_BIG = "images/load.png";
    final String OPEN_ICON_FILE_SMALL = "images/load1.png";
    final String SAVE_ICON_FILE_BIG = "images/save.png";
    final String SAVE_ICON_FILE_SMALL = "images/save1.png";
    final String EXIT_ICON_FILE_SMALL = "images/exit.png";
    final String START_SEARCH_ICON_BIG = "images/search.png";
    final String PREV_SEARCH_ICON_BIG = "images/prev_big.png";
    final String NEXT_SEARCH_ICON_BIG = "images/next_big.png";
    final String START_SEARCH_ICON_SMALL = "images/search_small.png";
    final String PREV_SEARCH_SMALL = "images/prev_small.png";
    final String NEXT_SEARCH_SMALL = "images/next_small.png";


    private final JTextArea textArea;
    private JTextField findTextField;
    private JButton saveButton;
    private JButton loadButton;
    private JButton startSearchButton;
    private JButton prevSearchButton;
    private JButton nextSearchButton;
    private JCheckBox useRegExBox;
    private boolean isChecked = false;
    private JFileChooser jfc;
    private ArrayList<Integer> indexFound;
    private ArrayList<Integer> lengthFound;
    private int counter = 0;
    private int nextCounter = 0;


    public TextEditor() {
        super("Text Editor");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        // main container
        Container container = getContentPane();

        // Adding text field
        textArea = new JTextArea();
        textArea.setName("TextArea");


        // Doing text field scrolled
        JScrollPane scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setName("ScrollPane");
        // Indicate that the scroll will always be
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setName("FileChooser");
        add(jfc);

        // Collect all panels for display on the screen
        container.add(scrollableTextArea, BorderLayout.CENTER);
        container.add(upArea(), BorderLayout.NORTH);

        createListener();
        menuBar();

        // Display window on the screen
        setVisible(true);
    }


    // Top area with file name field and SAVE and LOAD buttons
    private JPanel upArea() {
        JPanel upArea = new JPanel();
        upArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        findTextField = new JTextField(20);
        findTextField.setName("SearchField");

        // Create a SAVE button. Listener in a separate method.
        saveButton = new JButton(new ImageIcon(SAVE_ICON_FILE_BIG));
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(KEY_SIZE, KEY_SIZE));

        // Create a OPEN button. Listener in a separate method.
        loadButton = new JButton(new ImageIcon(OPEN_ICON_FILE_BIG));
        loadButton.setName("OpenButton");
        loadButton.setPreferredSize(new Dimension(KEY_SIZE, KEY_SIZE));

        // Create a START SEARCH button. Listener in a separate method.
        startSearchButton = new JButton(new ImageIcon(START_SEARCH_ICON_BIG));
        startSearchButton.setName("StartSearchButton");
        startSearchButton.setPreferredSize(new Dimension(KEY_SIZE, KEY_SIZE));

        // Create a PREVIOUS SEARCH button. Listener in a separate method.
        prevSearchButton = new JButton(new ImageIcon(PREV_SEARCH_ICON_BIG));
        prevSearchButton.setName("PreviousMatchButton");
        prevSearchButton.setPreferredSize(new Dimension(KEY_SIZE, KEY_SIZE));

        // Create a NEXT SEARCH button. Listener in a separate method.
        nextSearchButton = new JButton(new ImageIcon(NEXT_SEARCH_ICON_BIG));
        nextSearchButton.setName("NextMatchButton");
        nextSearchButton.setPreferredSize(new Dimension(KEY_SIZE, KEY_SIZE));

        // Creating a box USE REGEX. Listener in a separate method.
        useRegExBox = new JCheckBox("Use Regex");
        useRegExBox.setName("UseRegExCheckbox");

        // Collect the panel
        upArea.add(loadButton);
        upArea.add(saveButton);
        upArea.add(findTextField);
        upArea.add(startSearchButton);
        upArea.add(prevSearchButton);
        upArea.add(nextSearchButton);
        upArea.add(useRegExBox);

        return upArea;
    }


    // Putting all listeners in one place
    public void createListener() {

        // For operations SAVE
        saveButton.addActionListener(actionEvent -> {
            int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try (FileWriter writer = new FileWriter(selectedFile)) {
                    writer.write(textArea.getText());
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        });

        // For operations OPEN
        loadButton.addActionListener(actionEvent -> {
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                try {
                    textArea.setText(new String(Files.readAllBytes(selectedFile.toPath())));
                } catch (IOException e) {
                    textArea.setText(null);
                }
            }
        });

        // For START SEARCH
        startSearchButton.addActionListener(actionEvent -> SearchEngine());

        // For NEXT SEARCH
        nextSearchButton.addActionListener(actionEvent -> NextSearch());

        // For PREVIOUS SEARCH
        prevSearchButton.addActionListener(actionEvent -> PrevSearch());

        // For USE REGEX
        useRegExBox.addActionListener(actionEvent -> isChecked = !isChecked);
    }

    public void menuBar() {
        // Creating menu's line
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Creating menu FILE
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        // Create an OPEN clause with a mnemonic command and take a listener from the OPEN button
        JMenuItem loadMenuItem = new JMenuItem("Open", new ImageIcon(OPEN_ICON_FILE_SMALL));
        loadMenuItem.setName("MenuOpen");
        loadMenuItem.addActionListener(loadButton.getActionListeners()[0]);
        loadMenuItem.setMnemonic(KeyEvent.VK_O);


        // Create an SAVE clause with a mnemonic command and take a listener from the SAVE button
        JMenuItem saveMenuItem = new JMenuItem("Save", new ImageIcon(SAVE_ICON_FILE_SMALL));
        saveMenuItem.setName("MenuSave");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.addActionListener(saveButton.getActionListeners()[0]);

        // Create an EXIT item with a mnemonic code and a safe exit
        JMenuItem exitMenuItem = new JMenuItem("Exit", new ImageIcon(EXIT_ICON_FILE_SMALL));
        exitMenuItem.setName("MenuExit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.addActionListener(actionEvent -> dispose());

        // Creating menu SEARCH
        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        searchMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(searchMenu);

        // Create a START SEARCH item with a mnemonic command and take the listener from the START SEARCH button
        JMenuItem startSearchMenuItem = new JMenuItem("Start Search", new ImageIcon(START_SEARCH_ICON_SMALL));
        startSearchMenuItem.setName("MenuStartSearch");
        startSearchMenuItem.setMnemonic(KeyEvent.VK_T);
        startSearchMenuItem.addActionListener(startSearchButton.getActionListeners()[0]);

        // Create the PREVIOUS SEARCH item and take the listener from the PREVIOUS SEARCH button
        JMenuItem previousSearchMenuItem = new JMenuItem("Previous Search", new ImageIcon(PREV_SEARCH_SMALL));
        previousSearchMenuItem.setName("MenuPreviousMatch");
        previousSearchMenuItem.addActionListener(prevSearchButton.getActionListeners()[0]);

        // Create a NEXT SEARCH item and take a listener from the NEXT SEARCH button
        JMenuItem nextSearchMenuItem = new JMenuItem("Next Search", new ImageIcon(NEXT_SEARCH_SMALL));
        nextSearchMenuItem.setName("MenuNextMatch");
        nextSearchMenuItem.addActionListener(nextSearchButton.getActionListeners()[0]);

        // Create a USE REG EXP item and use the listener to simulate pressing the USE REG EXP box
        JMenuItem useRegExpMenuItem = new JMenuItem("Use regex");
        useRegExpMenuItem.setName("MenuUseRegExp");
        useRegExpMenuItem.addActionListener(actionEvent -> useRegExBox.doClick());


        // Add items to the menu
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        searchMenu.add(startSearchMenuItem);
        searchMenu.add(previousSearchMenuItem);
        searchMenu.add(nextSearchMenuItem);
        searchMenu.addSeparator();
        searchMenu.add(useRegExpMenuItem);
    }

    // Getting text from a text box
    public String getTextFind() {
        return findTextField.getText();
    }

    // Get the text of the search query
    public String getAllText() {
        return textArea.getText();
    }

    // STARTING SEARCH
    public void SearchEngine() {
        indexFound = new ArrayList<>();
        lengthFound = new ArrayList<>();
        String findText = getTextFind();
        String allText = getAllText();
        int index = -1;
        int lengthFind = findText.length();

        if (isChecked) {       // If the UseReg checkbox is pressed - search by regular expressions

            Pattern pattern = Pattern.compile(findText);
            Matcher matcher = pattern.matcher(allText);
            while (matcher.find()) { // As long as there are matches,
                // we store in arrays the numbers of characters of the beginning of the match
                // and the length of the match
                index = matcher.start();
                lengthFind = matcher.end() - index;
                indexFound.add(index);
                lengthFound.add(lengthFind);
            }

        } else {                // If the UseReg checkbox is not pressed, we are just looking for matches

            while (true) {      // If there are matches,
                // store the number of characters of the beginning of matches and the length in the array
                index = allText.indexOf(findText, index + 1);
                if (index == -1) {
                    break;
                }
                indexFound.add(index);
                lengthFound.add(lengthFind);
                System.out.println("index=" + index);
                System.out.println("length=" + lengthFind);
            }
        }

        counter = indexFound.size();  // Number of matches = number of elements of the resulting array
        nextCounter = 0;

        if (counter > 0) {            // Select the first match and place the cursor at the end
            textArea.setCaretPosition(indexFound.get(0) + lengthFound.get(0));
            textArea.select(indexFound.get(0), indexFound.get(0) + lengthFound.get(0));
            textArea.grabFocus();
        }
    }

    // Implement SEARCH forward
    public void NextSearch() {
        if (counter > 0) {
            if (counter - 1 > nextCounter) {
                nextCounter++;
            } else {
                nextCounter = 0; // Loop if we have reached the end of the text
            }
            textArea.setCaretPosition(indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.select(indexFound.get(nextCounter), indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.grabFocus();
        }
    }

    // Implement SEARCH back
    public void PrevSearch() {
        if (counter > 0) {
            if (nextCounter != 0) {
                nextCounter--;
            } else {
                nextCounter = counter - 1; // Loop if we have reached the end of the text
            }
            textArea.setCaretPosition(indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.select(indexFound.get(nextCounter), indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.grabFocus();
        }
    }
}
