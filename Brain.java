/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbotexample2;

/**
 *
 * @author gregoryhanford
 */
public class Brain {
    //Declare the Vocabulary
    private Vocabulary myVocab;
    //The Brain's constructor takes in the Vocabulary as input, sets its own version of the Vocabulary to the input
    public Brain(Vocabulary myVocab) {
        this.myVocab = myVocab;
    }
    
    //The generateOutput method takes in the userInput as input and returns the vocabulary's output
    public String generateOutput(String userInput) {
        return myVocab.getPhrase(userInput);
    }
}
