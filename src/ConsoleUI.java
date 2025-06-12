import java.util.Scanner;

public class ConsoleUI {
    private static EmailClassifier classifier;

    public static void main(String[] args) {
        try {
            System.out.println("Initializing spam filter...");
            classifier = new EmailClassifier("data/training_data.csv");
            System.out.println("Spam filter initialized successfully!");
            
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("\n===== Spam Filter =====");
                System.out.print("Enter sender email (or 'exit' to quit): ");
                String sender = scanner.nextLine();
                
                if (sender.equalsIgnoreCase("exit")) {
                    break;
                }
                
                System.out.print("Enter email subject: ");
                String subject = scanner.nextLine();
                
                System.out.println("Enter email body (type a single '.' on a new line to finish): ");
                StringBuilder bodyBuilder = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).equals(".")) {
                    bodyBuilder.append(line).append("\n");
                }
                String body = bodyBuilder.toString().trim();
                
                if (sender.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                    System.out.println("Error: All fields must be filled.");
                    continue;
                }
                
                try {
                    Email email = new Email(sender, subject, body);
                    double result = classifier.classifyEmail(email);
                    String classification = (result == 0.0) ? "Not Spam (Ham)" : "Spam";
                    System.out.println("\nClassification result: " + classification);
                } catch (Exception e) {
                    System.out.println("Error during classification: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            scanner.close();
            System.out.println("Goodbye!");
            
        } catch (Exception e) {
            System.out.println("Failed to initialize classifier: " + e.getMessage());
            e.printStackTrace();
        }
    }
}