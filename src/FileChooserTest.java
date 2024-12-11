import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileChooserTest {
    public static void main(String[] args) {
        ArrayList<String> arrList = new ArrayList<>();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));


        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            System.out.println("File chosen: " + selectedFile.getAbsolutePath());


            try (Scanner inFile = new Scanner(selectedFile)) {
                while (inFile.hasNextLine()) {
                    arrList.add(inFile.nextLine());
                }


                System.out.println("Contents of the file:");
                for (String line : arrList) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected.");
        }
    }
}
