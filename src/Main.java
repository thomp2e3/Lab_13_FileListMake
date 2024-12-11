import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> arrList = new ArrayList<>();
        String ans = "";
        boolean run = true;
        boolean needsToBeSaved = false;
        String fileName = "";

        do {
            ans = printMenu(in, arrList);
            switch (ans) {
                case "A":
                    addToList(in, arrList);
                    needsToBeSaved = true;
                    break;
                case "C":
                    clearList(arrList);
                    needsToBeSaved = true;
                    break;
                case "D":
                    deleteFromList(in, arrList);
                    needsToBeSaved = true;
                    break;
                case "O":
                    fileName = openListFile(in, arrList, needsToBeSaved);
                    if (!fileName.isEmpty()) {
                        needsToBeSaved = false;
                    }
                    break;
                case "S":
                    if (fileName.isEmpty()) {
                        fileName = saveAsListFile(in, arrList);
                    } else {
                        saveCurrentFile(arrList, fileName);
                    }
                    needsToBeSaved = false;
                    break;
                case "V":
                    displayList(arrList);
                    break;
                case "Q":
                    if (needsToBeSaved) {
                        boolean saveBeforeExit = SafeInput.getYNConfirm(in, "You have unsaved changes. Would you like to save before exiting?");
                        if (saveBeforeExit) {
                            if (fileName.isEmpty()) {
                                fileName = saveAsListFile(in, arrList);
                            } else {
                                saveCurrentFile(arrList, fileName);
                            }
                        }
                    }
                    run = false;
                    break;
            }
        } while (run);
    }

    private static String printMenu(Scanner in, ArrayList<String> arrList) {
        if (arrList.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("The list looks like:");
            for (int i = 0; i < arrList.size(); i++) {
                System.out.printf("    %d. %s\n", i + 1 , arrList.get(i));
            }
        }
        return SafeInput.getRegExString(in, "Choose one of the following:\nA: Add\nC: Clear\nD: Delete\nO: Open\nS: Save\nV: View\nQ: Quit\n", "[AaCcDdOoSsVvQq]").toUpperCase();
    }

    public static void addToList(Scanner in, ArrayList<String> arrList) {
        String itemToAdd = SafeInput.getNonZeroLength(in, "What would you like to add to the list? ");
        arrList.add(itemToAdd);
    }

    public static void clearList(ArrayList<String> arrList) {
        arrList.clear();
    }

    public static void deleteFromList(Scanner in, ArrayList<String> arrList) {
        int itemToDelete = SafeInput.getRangedInt(in, "Which item do you want to delete? ", 1, arrList.size());
        arrList.remove(itemToDelete - 1);
        System.out.println(itemToDelete + " was removed");
    }

    public static void displayList(ArrayList<String> arrList) {
        for (int i = 0; i < arrList.size(); i++) {
            System.out.println(arrList.get(i));
        }
    }

    public static String openListFile(Scanner in, ArrayList<String> arrList, boolean needsToBeSaved) {
        if (needsToBeSaved) {
            String prompt = "If you open a new list, your current one will be lost. Are you sure? ";
            boolean deleteListYN = SafeInput.getYNConfirm(in, prompt);
            if (!deleteListYN) {
                return "";
            }
        }

        clearList(arrList);

        final JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a file");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));


        final File[] selectedFile = new File[1];

        try {

            SwingUtilities.invokeAndWait(() -> {
                int returnValue = chooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile[0] = chooser.getSelectedFile();
                }
            });

            if (selectedFile[0] != null) {
                System.out.println("File selected: " + selectedFile[0].getAbsolutePath());


                try (Scanner inFile = new Scanner(selectedFile[0])) {
                    while (inFile.hasNextLine()) {
                        arrList.add(inFile.nextLine());
                    }
                }

                return selectedFile[0].getAbsolutePath();
            } else {
                System.out.println("No file selected.");
                return "";
            }
        } catch (Exception e) {
            System.out.println("Error opening file dialog: " + e.getMessage());
            return "";
        }
    }


    private static String saveAsListFile(Scanner in, ArrayList<String> arrList) {
        String fileName = SafeInput.getNonZeroLength(in, "Enter the name for the new list (without extension): ") + ".txt";
        saveCurrentFile(arrList, fileName);
        return fileName;
    }

    public static void saveCurrentFile(ArrayList<String> arrList, String fileName) {
        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath().resolve(fileName);

        try {
            outFile = new PrintWriter(target.toString());
            for (String item : arrList) {
                outFile.println(item);
            }
            outFile.close();
            System.out.printf("File \"%s\" saved!\n", target.getFileName());
        } catch (IOException e) {
            System.out.println("IOException Error: " + e.getMessage());
        }
    }
}
