/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbotexample2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author gregoryhanford
 */

public class ChatBotExample2 implements ActionListener {
    //Declare the JFrame components
    private JTextArea jtaDisplay;
    private JTextField jtfInput;
    private JButton jbtnSend;
    private JScrollPane jscrlp;
    private JFrame jfrm;
    
    private Brain myBrain;
    private Vocabulary myVocab;
    //Create the constructor
    public ChatBotExample2() {
        //Create the JFrame
        jfrm = new JFrame("Chat Bot Example 1");
        jfrm.setSize(300, 400);
        jfrm.setLayout(new FlowLayout());
        //Set the JFrame's default close operation to EXIT_ON_CLOSE
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Initialize the JTextArea
        jtaDisplay = new JTextArea(15, 15);
        
        //Set the JTextArea so it can't be edited
        jtaDisplay.setEditable(false);
        
        //Set the JTextArea to wrap lines
        jtaDisplay.setLineWrap(true);
        
        //Create a new JScrollPane based on the JTextArea
        jscrlp = new JScrollPane(jtaDisplay);
        
        //Initialize the JTextField
        jtfInput = new JTextField(15);
        
        //Initialize the JButton
        jbtnSend = new JButton("Send");
        
        //Set the JButton's Action Listener to this (ChatBotExample1 class)
        jbtnSend.addActionListener(this);
        
        //Add all components to the JFrame's content pane
        jfrm.getContentPane().add(jscrlp);
        jfrm.getContentPane().add(jtfInput);
        jfrm.getContentPane().add(jbtnSend);
        
        //Show the JFrame
        jfrm.setVisible(true);
        
        myVocab = new Vocabulary();
        myVocab.show(true);
        myVocab.addKey("hello");
        myVocab.addPhrase("hello", "hello, my name is ChatBot.");
        myVocab.addPhrase("hello", "hello, I'm ChatBot.");
        myVocab.addKey("goodbye");
        myVocab.addPhrase("goodbye", "It was nice talking to you.");
        myVocab.addPhrase("goodbye", "See you some other time!");
        myVocab.addKey("how are you");
        myVocab.addPhrase("how are you", "I'm doing fine, and you?");
        myVocab.addPhrase("how are you", "I'm doing great!");
        myVocab.addKey("i'm good");
        myVocab.addPhrase("i'm good", "That's good to hear.");
        myBrain = new Brain(myVocab);
    }
    public void actionPerformed(ActionEvent ae) {
        //If the user clicks the send button
        if (ae.getActionCommand().equals("Send")) {
            //Set the input to the JTextField's value
            String input = jtfInput.getText();
            
            //Set the output as "Hello World"
            String output = myBrain.generateOutput(input);
            
            //This is the tricky part - we want to keep the value that's currently in the JTextArea and append the values
            //from the input variable and the output variable
            jtaDisplay.setText(jtaDisplay.getText() + "You say: " + input + "\n");
            jtaDisplay.setText(jtaDisplay.getText() + "ChatBot says: " + output + "\n");
            
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //I don't expect you to understand this, but memorize this code!
        //If you're truly a geek, this invokes the chat bot on a separate thread from the main thread.  This makes it so there
        //is less chance of a conflict.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChatBotExample2();
            }
        });
    }
}
 