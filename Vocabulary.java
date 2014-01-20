/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbotexample2;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author gregoryhanford
 */
public class Vocabulary implements ActionListener{
    //Declare the JTabbedPane that stores the vocabulary phrases and keys
    private JTabbedPane jtp;
    //Declare the JFrame
    private JFrame jfrm;
    //Create an ArrayList called phrases to store the PhraseCollections
    private ArrayList <PhraseCollection> phrases;
    //Create a PhraseCollection called pc to store the current phrase collection
    private PhraseCollection pc;
    
    private JMenu jmFile;
    private JMenuItem jmiSave;
    private JMenuItem jmiLoad;
    
    private JMenuBar jmb;
    private JMenu jmKeyOptions;
    private JMenuItem jmiAddKey;
    private JMenuItem jmiRemoveKey;
    
    private JMenu jmPhraseOptions;
    private JMenuItem jmiAddPhrase;
    private JMenuItem jmiRemovePhrase;
    
    private enum EditModes {
        EDIT_OFF, EDIT_ON, EDIT_KEY, EDIT_PHRASE, EDIT_FINAL
    }
    private EditModes editMode = EditModes.EDIT_OFF;
    private String lastUserInput = "";
    private String key;
    private String phrase;
    private String nameOfUser;
    
    public Vocabulary() {
        //Initialize the JFrame
        jfrm = new JFrame("Chat Bot Vocabulary");
        jfrm.setBounds(300, 300, 400, 400);
        jfrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfrm.setLayout(new FlowLayout());
        
        //Initialize the phrases ArrayList
        phrases = new ArrayList();
        
        //Initialize the JMenuBar;
        jmb = new JMenuBar();
        
        //Set the JMenuBar to the Vocabulary JFrame
        jfrm.setJMenuBar(jmb);
        
        jmFile = new JMenu("File");
        jmiSave = new JMenuItem("Save");
        jmiSave.addActionListener(this);
        jmiLoad = new JMenuItem("Load");
        jmiLoad.addActionListener(this);
        jmFile.add(jmiSave);
        jmFile.add(jmiLoad);
        jmb.add(jmFile);
        
        //Create the Key Options menu
        jmKeyOptions = new JMenu("Key Options");
        jmiAddKey = new JMenuItem("Add Key");
        jmiAddKey.addActionListener(this);
        jmiRemoveKey = new JMenuItem("Remove Key");
        jmiRemoveKey.addActionListener(this);
        jmKeyOptions.add(jmiAddKey);
        jmKeyOptions.add(jmiRemoveKey);
        jmb.add(jmKeyOptions);
        
        //Create the Phrase options menu
        jmPhraseOptions = new JMenu("Phrase Options");
        jmiAddPhrase = new JMenuItem("Add Phrase");
        jmiAddPhrase.addActionListener(this);
        jmiRemovePhrase = new JMenuItem("Remove Phrase");
        jmiRemovePhrase.addActionListener(this);
        jmPhraseOptions.add(jmiAddPhrase);
        jmPhraseOptions.add(jmiRemovePhrase);
        jmb.add(jmPhraseOptions);
        
        
        //Initialize the JTabbedPane
        jtp = new JTabbedPane();
        jtp.setPreferredSize(new Dimension(400, 400));
        
        //Add the JTabbedPane to the JFrame
        jfrm.add(jtp);
    }
    
    //Create the actionPerformed method
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equalsIgnoreCase("Add Key")) {
            String key = JOptionPane.showInputDialog(jfrm, "Please enter the key: ");
            addKey(key);
        } else if (ae.getActionCommand().equalsIgnoreCase("Remove Key")) {
            removeKey();
        } else if (ae.getActionCommand().equalsIgnoreCase("Add Phrase")) {
            String phrase = JOptionPane.showInputDialog(jfrm, "Please enter the phrase: ");
            addPhrase(phrase);
        } else if (ae.getActionCommand().equalsIgnoreCase("Remove Phrase")) {
            removePhrase();
        } else if (ae.getActionCommand().equalsIgnoreCase("Save")) {
            save();
        } else if (ae.getActionCommand().equalsIgnoreCase("Load")) {
            load();
        }
    }
    
    public void reset() {
        jtp.removeAll();
        for (int i = 0; i < phrases.size(); i++) {
            jtp.addTab(phrases.get(i).getKey(),phrases.get(i).getJListPhrases());
        }
    }
    //Create a show method to show the JFrame
    public void show(boolean showMe) {
       if (showMe) jfrm.setVisible(true);
       else jfrm.setVisible(false);
    }
    
    //Create the getPhrase method - takes in the userInput and searches the PhraseCollection
    //for the key
    public String getPhrase(String userInput) {
        if (containsIgnoreCase(userInput, "my name is")) {
            int index = userInput.toLowerCase().indexOf("my");
            userInput = (String)userInput.subSequence(index, userInput.length()-1);
            String splitUI[] = userInput.split(" ");
            nameOfUser = splitUI[3];
            //System.out.println(nameOfUser);
        }
        if (editMode == EditModes.EDIT_OFF) {
            if (!userInput.toLowerCase().equals("edit"))
                lastUserInput = userInput;
        }
        
        if (userInput.toLowerCase().equals("edit")) {
            editMode = EditModes.EDIT_ON;
        }
        if (editMode == EditModes.EDIT_OFF) {
            String output = searchPhrases(userInput);
            if (containsIgnoreCase(output, "*name*")) {
                output = output.replace("*name*", nameOfUser);
            }
            if (output.equals("")) {
                
                return "I didn't understand.  Enter \"edit\" to go into edit mode.";
            } else {
                return output;
            }
        } else {
            return edit(userInput);
        }
    }
    public String edit(String userInput) {
        if (editMode == EditModes.EDIT_ON) {
            editMode = EditModes.EDIT_KEY;
            return "What is the key for\n \"" + lastUserInput + "\" ?";
        } else if (editMode == EditModes.EDIT_KEY) {
            editMode = EditModes.EDIT_PHRASE;
            key = userInput.toLowerCase();
            return "What is the phrase for\n \"" + lastUserInput + "\" ?";
        } else if (editMode == EditModes.EDIT_PHRASE) {
            editMode = EditModes.EDIT_OFF;
            phrase = userInput;
            addKey(key);
            addPhrase(key, phrase);
            return "Editing done.";
        } else {
            return "I didn't understand. Error in Edit function.";
        }
    }
    //The searchPhrases method - searches the PhraseCollections for the userInput
     public String searchPhrases(String userInput) {
         //Convert the userInput to lower case since the keys are lower case
        userInput = userInput.toLowerCase();
        //Create a new Random object - we'll be using this later in the method
        Random r = new Random();
        //Create an ArrayList of PhraseCollections to hold the matching keys for the userInput
        ArrayList <PhraseCollection> matchingKeys = new ArrayList();
        //If there are entries in the phrases variable
        if (phrases.size() != 0) {
            //Create a for loop to cycle through the phrases variable
            for (int i = 0; i < phrases.size(); i++) {
                //If there is a key that matches the userInput, add the corresponding phraseCollection to matchingKeys
                if (userInput.contains(phrases.get(i).getKey())) {
                    matchingKeys.add(phrases.get(i));
                }
            }
            //If there were matches, return a random match.  Else, return ""
            if (matchingKeys.size() > 0) {
                return matchingKeys.get(r.nextInt(matchingKeys.size())).getPhrase();
            } else {
                return "";
            }
        }
        //Make sure the method returns something
        return "";
    }
     //The add key method
     public void addKey(String key) {
        //Convert the key to lower case
        key = key.toLowerCase();
        //If there were no matching keys already, add a new phrase collection
        //and add a new tab with the key and the corresponding JList
        if (searchForKey(key) == -1) {
            pc = new PhraseCollection(key);
            jtp.addTab(key, pc.getJListPhrases());
            //Add the PhraseCollection to the phrases ArrayList
            phrases.add(pc);
        }
    }
     public void removeKey() {
        String key = getSelectedKey();
        int index = searchForKey(key);
        phrases.remove(index);
        jtp.remove(jtp.getSelectedIndex());
    }
    public void addPhrase(String phrase) {
         String key = getSelectedKey();
         int index = searchForKey(key);
         phrases.get(index).addPhrase(phrase);
    }
     //The add phrase method
    public void addPhrase(String key, String phrase) {
        //Convert the key to lower case
        key = key.toLowerCase();
        //if searchForKey returns -1, meaning it was not found, then add the new phrase
        int index = searchForKey(key);
        if (index != -1) {
            phrases.get(index).addPhrase(phrase);
        }
    }
    public void removePhrase() {
        String key = getSelectedKey();
        int index = searchForKey(key);
        phrases.get(index).removePhrase();
    }
    //The searchForKey method
    public int searchForKey(String key) {
        //Cycle through the phrases variable and search for a corresponding key
        for (int i = 0; i < phrases.size(); i++) {
            //If the key is found, return the index it was found at
            if (phrases.get(i).getKey().equalsIgnoreCase(key)) {
                return i;
            }
        }
        //If the key was not found, return -1
        return -1;
    }
    //The getSelectedKey method
    public String getSelectedKey() {
        //Returns the JTabPane's selected key based on the selected index
        return jtp.getTitleAt(jtp.getSelectedIndex());
    }
    public boolean save() {
        try {
            File f = new File("vocab.chatbot"); 
            
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(phrases);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            System.out.println("IOException while writing vocabulary.");
            e.printStackTrace();
            return false;
        }
    }public boolean load() {
        try {
            File f = new File("vocab.chatbot");
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            phrases = (ArrayList)ois.readObject();
            reset();
            
            ois.close();
            fis.close();
            return true;
        } catch (FileNotFoundException e){
            return false;
        } catch (IOException e) {
            System.out.println("IOException while loading vocabulary.");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception while loading vocabulary.");
            return false;
        }
    }
    public boolean containsIgnoreCase(String outer, String inner) {
        outer = outer.toLowerCase();
        inner = inner.toLowerCase();
        if (outer.contains(inner)) {
            return true;
        } else return false;
    }
}

