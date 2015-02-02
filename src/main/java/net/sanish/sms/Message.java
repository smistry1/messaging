public class Message {
    
    private String messageBody;
    private String sender;
    private boolean isEncrypted;
    
    
    public Message(String messageBody, String sender) {
        this.messageBody = messageBody;
        this.sender = sender;
        this.encrypted = true;
    }   
    
    
    public boolean isEncrypted() {
        return isEncrypted;
    }
    
    public void setDecryptedMessage(String message) {
        this.messageBody = message;
        this.isEncrypted = false;
    } 
    
    public void getMessageBody() {
        return this.messageBody;
    }
    
    
    
    public void getSender() {
        return this.sender;
    }
}
