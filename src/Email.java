import java.io.Serializable;

public class Email implements Serializable {
    private String sender;
    private String subject;
    private String body;

    public Email(String sender, String subject, String body) {
        if (sender == null || subject == null || body == null) {
            throw new IllegalArgumentException("Sender, subject, and body cannot be null");
        }
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "sender='" + sender + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
