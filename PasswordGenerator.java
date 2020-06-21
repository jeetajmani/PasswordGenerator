/**
 *      name:       Password Generator
 *      author:     Jeet Ajmani - University of Victoria
 *      date:       June 20th, 2020 - VikeHacks Hackathon
 *      purpose:    To generate a random password based on the user's input in a GUI dialog
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class PasswordGenerator {
    public static void main(String[] args) {

        // Check box for whether to include uppercase letters, defaults to true
        JCheckBox incUprCs = new JCheckBox("Include uppercase letters? (ex: ABDEF...XYZ)", true);

        // Check box for whether to include numbers, defaults to true
        JCheckBox incNum = new JCheckBox("Include numbers? (ex: 1234...890)", true);

        // Check box for whether to include symbols, defaults to false
        JCheckBox incSym = new JCheckBox("Include symbols? (ex: !@#$%^ etc.)", false);

        // Check box for whether to include special characters, defaults to false
        JCheckBox incSpCh = new JCheckBox("Include special characters? (ex: |{[(:'</ etc.)", false);

        // Check box for whether to write password to file, defaults to false
        JCheckBox writeToFile = new JCheckBox("Write password to passwordList.txt?", false);

        // Message that shows if password is to be written to file
        JLabel msg = new JLabel("What is your password for? (ex: Twitter, YouTube, etc.)");
        msg.setVisible(false);

        // Text field where user can enter the usage of their password
        JTextField passUse = new JTextField("");
        passUse.setVisible(false);
        passUse.setPreferredSize(new Dimension(320, 20));

        // Dynamic label that asks for number of character and shows current number selected, default: 12
        JLabel msg2 = new JLabel("How many characters?   12", JLabel.CENTER);
        msg2.setPreferredSize(new Dimension(350, 20));

        // simple blank label
        JLabel blank1 = new JLabel(" ");

        // simple blank label
        JLabel blank2 = new JLabel(" ");

        // simple blank label
        JLabel blank3 = new JLabel(" ");
        blank3.setVisible(false);

        // blank label at the end of the JOptionPane to maintain window size
        JLabel blank4 = new JLabel(" ");
        blank4.setPreferredSize(new Dimension(350, 80));

        // slider to choose length of password; to avoid parse issues
        JSlider charNum = new JSlider(6, 26, 12);

        charNum.setMinorTickSpacing(1); // minor tick every 1 space
        charNum.setMajorTickSpacing(4); // major tick every 4 spaces
        charNum.setPaintTicks(true);    // show ticks
        charNum.setPaintLabels(true);   // show labels

        // table to store labels
        Hashtable labels = new Hashtable();
        labels.put(6, new JLabel("6"));     // min value
        labels.put(26, new JLabel("26"));   // max value
        charNum.setLabelTable(labels);  // set labels to slider
        charNum.setSnapToTicks(true);   // slider snaps to ticks

        // a ChangeListener that changes the current password length based on slider
        charNum.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                msg2.setText("How many characters?   " + ((JSlider)e.getSource()).getValue());
            }
        });

        // a ChangeListener that shows the elements related to storing the password in a file
        writeToFile.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                boolean w = writeToFile.isSelected();

                msg.setVisible(w);
                blank3.setVisible(w);
                passUse.setVisible(w);
            }
        });

        // Object array, specifically ordered for output on the dialog
        Object[] content = {incUprCs, incNum, incSym, incSpCh, blank1, msg2, charNum, blank2, writeToFile, blank3, msg, passUse, blank4};

        // the GUI dialog that allows the user to make specifications for their password
        int x = JOptionPane.showConfirmDialog(null, content, "Password Generator", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        // do nothing if the user clicks cancel
        if (x == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }

        // what the user inputs in the password usage field
        String passUse_text = passUse.getText();

        // if the uppercase characters box is checked
        boolean if_incUprCs = incUprCs.isSelected();

        // if the numbers box is checked
        boolean if_incNum = incNum.isSelected();

        // if the symbols box is checked
        boolean if_incSym = incSym.isSelected();

        // if the special characters box is checked
        boolean if_incSpCh = incSpCh.isSelected();

        // the length of the password chpsen by the user
        int chosenLength = charNum.getValue();

        // if the password is to be written to the file
        boolean wToFile = writeToFile.isSelected();

        // the password String, generated by generatePass()
        String password = generatePass(if_incNum, if_incSpCh, if_incSym, if_incUprCs, chosenLength);

        // writes to file
        if (wToFile) {
            createAndWriteFile(passUse_text, password);
        }

        // copies the password to the clipboard
        // source: https://stackoverflow.com/questions/11596368/set-clipboard-contents
        StringSelection selection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        // finish
        System.exit(0);
    }

    /**
     * Generates a password based on the user's choices and returns it, to then be appended to a txt file.
     *
     * @param num   if numbers are to be included
     * @param spChr if special characters are to be included
     * @param sym   if symbols are to be included
     * @param uprCs if uppercase characters are to be included
     * @param sz    the length of the password, as specified
     * @return      the generated password
     */
    public static String generatePass(boolean num, boolean spChr, boolean sym, boolean uprCs, int sz) {

        // the string to be made the password
        String s = "";

        // the character to be concatenated to the string
        char ch;

        // random int
        int rand = 0;

        //
        ArrayList<Integer> totalASCII = new ArrayList<Integer>();

        // adds all lowercase letter ASCII values to the arraylist
        for (int i = 97; i <= 122; i++) {
            totalASCII.add(i);
        }

        // adds all number ASCII values to the arraylist, if specified
        if (num) {
            for (int i = 48; i <= 57; i++) {
                totalASCII.add(i);
            }
        }

        // adds all special character ASCII values to the arraylist, if specified
        if (spChr) {
            for (int i = 58; i <= 63; i++) {
                totalASCII.add(i);
            }

            for (int i = 91; i <= 95; i++) {
                totalASCII.add(i);
            }

            totalASCII.add(40);
            totalASCII.add(41);
            totalASCII.add(39);
            totalASCII.add(43);
            totalASCII.add(44);
            totalASCII.add(45);
            totalASCII.add(46);
            totalASCII.add(47);
            totalASCII.add(123);
            totalASCII.add(124);
            totalASCII.add(125);
        }

        // adds all symbol ASCII values to the arraylist, if specified
        if (sym) {
            for (int i = 33; i <= 38; i++) {
                totalASCII.add(i);
            }

            totalASCII.add(42);
            totalASCII.add(43);
            totalASCII.add(94);
            totalASCII.add(64);
        }

        // adds all uppercase letter ASCII values to the arraylist, if specified
        if (uprCs) {
            for (int i = 65; i <= 90; i++) {
                totalASCII.add(i);
            }
        }

        // the range of the random values (from 0 to size of arraylist)
        int range = totalASCII.size();

        // generates a random number from within the range, casts it to a char, and concatenates it to the string
        for (int i = 0; i < sz; i++) {
            rand = (int)(Math.random() * range);

            ch = (char)(totalASCII.get(rand).intValue());
            s += ch;
        }

        // returns the finished password
        return s;
    }

    /**
     * Creates a file called passwordList.txt and writes the password to it.
     * Inspiration from https://www.w3schools.com/java/java_files_create.asp
     *
     * @param n what the password is being used for, if it is blank, writes current date + time instead
     * @param p the password
     */
    public static void createAndWriteFile(String n, String p) {
        try {
            File f = new File("./passwordList.txt");
            FileWriter w = new FileWriter("./passwordList.txt", true);

            java.util.Date date = new java.util.Date();

            if (n.equals("")) {
                n = "" + date;
            }

            w.write(n + ": " + p + "\n");
            w.close();

        } catch (Exception e) {}
    }

}
