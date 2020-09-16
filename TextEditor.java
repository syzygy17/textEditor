package editor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextEditor extends JFrame {
    final int WIDTH = 800;
    final int HEIGHT = 800;
    JTextArea textArea;
    Container container;

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


        // Display window on the screen
        setVisible(true);
    }


    // Top area with file name field and SAVE and LOAD buttons
    private JPanel upArea() {
        JPanel upArea = new JPanel();
        upArea.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextField fileNameField = new JTextField(30);
        fileNameField.setName("FilenameField");


        // Saving in a file
        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.addActionListener(actionEvent -> {
            File file = new File(fileNameField.getText());

            try (FileWriter fileWriter = new FileWriter(file)){
                fileWriter.write(textArea.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(container,
                        "ERROR!\nНевозможно создать файл:\n " + fileNameField.getText());
            }
        });


        // Loading a file with a name in a text field
        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.addActionListener(actionEvent -> {
            try {
                textArea.setText(new String(Files.readAllBytes(Paths.get(fileNameField.getText()))));
            } catch (IOException e) {
                textArea.setText(null);
            }
        });


        // Collect a panel from name's input field and 2 buttons
        upArea.add(fileNameField);
        upArea.add(saveButton);
        upArea.add(loadButton);


        return upArea;
    }
}
