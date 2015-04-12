package net.sanish.sms;



import java.io.Serializable;

/**
   Class to represent a Message
*/
public class Message implements Serializable{
    
    private String messageBody;         // The message body, can be encrypted or decrypted
    private String sender;              // The message sender
    private boolean isEncrypted;        // Flag to indicate if messageBody is encrypted or not
    private int encryptionState;        // encryption state, 0 for default key, 1 for user key
    private int id;                     // ID of message
    private String date;                // Date Message was received
    private String senderDisplayName;   // Number or (Name where available)
    private String recipient;           // Receiver of message
    private String receiverDisplayName; // Number or (Name where available)

    /**
     *
     * @param messageBody       - main body text of message
     * @param sender            - sender of message
     * @param encryptionState   - encryption state of message body 0 for default key, 1 for user key
     * @param id                - id of message
     * @param date              - date Message was received
     */
    public Message(String messageBody, String sender, int encryptionState, int id, String date, String recipient) {
        this.messageBody = messageBody;
        this.sender = sender;
        this.isEncrypted = true;
        this.encryptionState = encryptionState;
        this.id = id;
        this.date = date;
        this.recipient = recipient;
    }

    /**
     * Returns the message recipient display name number
     * @return - the message recipient display name number
     */
    public String getRecipient() {
        return recipient;
    }


    /**
     * sets the message sender display name string
     * @param r -  the message sender display name string
     */

    public void setSenderDisplayName(String sender) {
        this.senderDisplayName = sender;
    }

    /**
     * sets the message recipient display name string
     * @param r -  the message recipient display name string
     */
    public void setReceiverDisplayName(String r) {
        this.receiverDisplayName = r;
    }

    /**
     * Returns the message recipient display name string
     * @return the message recipient display name string
     */

    public String getRecipientDisplayName() {
        return receiverDisplayName;
    }


    /**
     * Returns the message sender display name string
     * @return the message sender display name string
     */
    public String getSenderDisplayName() {
        return this.senderDisplayName;
    }


    /**
     * Returns the message date string
     * @return the message date string
     */
    public String getDate() {
        return date;
    }

    /**
     * @return a boolean stating whether or not the messsageBody is encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Returns the encryption state of message
     * @return an int representing the encryption state of the message,  0 for default key, 1 for user key
     */
    public int getEncryptionState() {
        return encryptionState;
    }

    /**
     * Changes to the messageBody to the provided decrypted text and sets encryption state to false
     * @param message - the decrypted message
     */
    public void setDecryptedMessage(String message) {
        this.messageBody = message;
        this.isEncrypted = false;
    }

    /**
     * returns id of message
     * @return the ID of the message
     */
    public int getId() {
        return id;
    }

   /*
    * returns messages body string, will be encrypted if decrypted text not set
    * @return the messageBody
   */
    public String getMessageBody() {
        return this.messageBody;
    }


    /**
     * returns the sender of the message
     * @return the message sender
     */
    public String getSender() {
        return this.sender;
    }
}
