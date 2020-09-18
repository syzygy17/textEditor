package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor extends JFrame {
    final int WIDTH = 800;
    final int HEIGHT = 800;
    private final JTextArea textArea;
    private final Container container;
    private JButton saveButton;
    private JButton loadButton;
    private JTextField filenameField;

    public TextEditor() {
        super("Text Editor");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);


        container = getContentPane();


        // Adding text field
        textArea = new JTextArea();
        textArea.setName("TextArea");


        // Doing text field scrolled
        JScrollPane scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setName("ScrollPane");
        // Indicate that the scroll will always be
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


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
        filenameField = new JTextField(30);
        filenameField.setName("FilenameField");

        // Create a SAVE button. Listener in a separate method.
        saveButton = new JButton("Save");
        saveButton.setName("SaveButton");

        // Create a LOAD button. Listener in a separate method.
        loadButton = new JButton("Load");
        loadButton.setName("LoadButton");

        // Collect the panel from the name input field and 2 buttons
        upArea.add(filenameField);
        upArea.add(saveButton);
        upArea.add(loadButton);

        return upArea;
    }


    // Putting all listeners in one place
    public void createListener() {

        // For operations SAVE
        saveButton.addActionListener(actionEvent -> {
            File file = new File(filenameField.getText());
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(textArea.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(container,
                        "ERROR!\nНевозможно создать файл:\n " + filenameField.getText());
            }
        });

        // For operations LOAD
        loadButton.addActionListener(actionEvent -> {
            try {
                textArea.setText(new String(Files.readAllBytes(Paths.get(filenameField.getText()))));
            } catch (IOException e) {
                textArea.setText(null);
            }
        });
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

        // Create a LOAD clause with a mnemonic and take the listener from the LOAD button
        JMenuItem loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setName("MenuLoad");
        loadMenuItem.addActionListener(loadButton.getActionListeners()[0]);
        loadMenuItem.setMnemonic(KeyEvent.VK_L);


        // Create a SAVE item with a mnemonic command and take a listener from the SAVE button
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setName("MenuSave");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.addActionListener(saveButton.getActionListeners()[0]);

        // Create an EXIT item with a mnemonic code and a safe exit
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setName("MenuExit");
        exitMenuItem.setMnemonic(KeyEvent.VK_E);
        exitMenuItem.addActionListener(actionEvent -> dispose());

        // Add items to the menu
        fileMenu.add(loadMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }
}
