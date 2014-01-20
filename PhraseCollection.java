/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbotexample2;
import java.util.*;
import java.io.*;
import javax.swing.*;
/**
 *
 * @author gregoryhanford
 */
public class PhraseCollection implements Serializable {
    //Declare the key for the collection of phrases
    //If key = "hello" then each phrase in dlmPhrases is a possible response
    private String key;
    //Declare the DefaultListModel
    private DefaultListModel dlmPhrases;
    //Declare the JList
    private JList jlstPhrases;
    
    //The constructor for PhraseCollection - Passes in the key
    public PhraseCollection(String key) {
        //Set the key to the key that is passed in from the constructor
        this.key = key;
        
        //Create a new DefaultListModel
        dlmPhrases = new DefaultListModel();
        
        //Create a new JList
        jlstPhrases = new JList(dlmPhrases);
    }
    
    //The getPhrase() method - selects a random phrase based on the key
    public String getPhrase() {
        Random r = new Random();
        if (dlmPhrases.size() != 0) {
                return (String)dlmPhrases.get(r.nextInt(dlmPhrases.size()));
        } else {
            return "";
        }
    }
    
    //getJListPhrases returns the JList
    public JList getJListPhrases() {
        return jlstPhrases;
    }
    
    //Add phrase to the DefaultListModel method
    public void addPhrase(String phrase) {
        dlmPhrases.addElement(phrase);
    }
    
    //Removes a phrase from the DefaultListModel based on what is selected in the JList
    public void removePhrase() {
        int index = jlstPhrases.getSelectedIndex();
        dlmPhrases.remove(index);
    }
    
    //Returns the key for this PhraseCollection
    public String getKey() {
        return key;
    }
}
