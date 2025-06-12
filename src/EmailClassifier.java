import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EmailClassifier {
    private Set<String> spamKeywords;
    private Set<String> spamSubjectPhrases;
    
    public EmailClassifier(String trainingDataPath) throws Exception {
        System.out.println("Initializing content-based classifier...");
        
        // Initialize collections
        spamKeywords = new HashSet<>();
        spamSubjectPhrases = new HashSet<>();
        
        // Add common spam keywords
        addDefaultSpamKeywords();
        
        // Load training data
        loadTrainingData(trainingDataPath);
        
        System.out.println("Classifier initialized with:");
        System.out.println("- " + spamKeywords.size() + " spam keywords");
        System.out.println("- " + spamSubjectPhrases.size() + " spam subject phrases");
    }
    
    private void addDefaultSpamKeywords() {
        // Money-related terms
        spamKeywords.add("cash");
        spamKeywords.add("money");
        spamKeywords.add("price");
        spamKeywords.add("credit");
        spamKeywords.add("loan");
        spamKeywords.add("income");
        spamKeywords.add("dollars");
        spamKeywords.add("financial");
        spamKeywords.add("wealth");
        spamKeywords.add("bank");
        spamKeywords.add("investment");
        
        // Urgency terms
        spamKeywords.add("urgent");
        spamKeywords.add("immediately");
        spamKeywords.add("fast");
        spamKeywords.add("quick");
        spamKeywords.add("now");
        spamKeywords.add("expires");
        spamKeywords.add("limited");
        spamKeywords.add("today");
        spamKeywords.add("last chance");
        spamKeywords.add("final notice");
        
        // Offer terms
        spamKeywords.add("free");
        spamKeywords.add("discount");
        spamKeywords.add("save");
        spamKeywords.add("offer");
        spamKeywords.add("deal");
        spamKeywords.add("bargain");
        spamKeywords.add("guarantee");
        spamKeywords.add("click");
        spamKeywords.add("claim");
        
        // Prize terms
        spamKeywords.add("winner");
        spamKeywords.add("won");
        spamKeywords.add("prize");
        spamKeywords.add("congratulations");
        spamKeywords.add("selected");
        spamKeywords.add("lottery");
        spamKeywords.add("lucky");
        
        // Health terms
        spamKeywords.add("weight loss");
        spamKeywords.add("diet");
        spamKeywords.add("pill");
        spamKeywords.add("enlarge");
        spamKeywords.add("viagra");
        spamKeywords.add("medication");
        
        // Subject phrases
        spamSubjectPhrases.add("free");
        spamSubjectPhrases.add("urgent");
        spamSubjectPhrases.add("winner");
        spamSubjectPhrases.add("discount");
        spamSubjectPhrases.add("offer");
        spamSubjectPhrases.add("limited time");
        spamSubjectPhrases.add("congratulations");
        spamSubjectPhrases.add("alert");
        spamSubjectPhrases.add("account");
        spamSubjectPhrases.add("password");
        spamSubjectPhrases.add("security");
        spamSubjectPhrases.add("verify");
        spamSubjectPhrases.add("bank");
        spamSubjectPhrases.add("credit");
        spamSubjectPhrases.add("win");
        spamSubjectPhrases.add("claim");
        spamSubjectPhrases.add("opportunity");
        spamSubjectPhrases.add("buy now");
    }
    
    private void loadTrainingData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            int spamCount = 0;
            int hamCount = 0;
            
            while ((line = reader.readLine()) != null) {
                // Simple CSV parsing (doesn't handle all edge cases)
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (parts.length >= 4) {
                    // Remove quotes if present
                    String body = parts[0].replaceAll("^\"|\"$", "").toLowerCase();
                    String subject = parts[2].replaceAll("^\"|\"$", "").toLowerCase();
                    String classValue = parts[3].trim();
                    
                    if ("spam".equals(classValue)) {
                        spamCount++;
                        // Extract potential keywords from spam emails
                        extractKeywords(body, 3);
                        
                        // Extract subject phrases
                        extractSubjectPhrases(subject);
                    } else {
                        hamCount++;
                    }
                }
            }
            
            System.out.println("Loaded " + spamCount + " spam and " + hamCount + " ham examples from training data");
            
        } catch (IOException e) {
            System.out.println("Warning: Could not fully process training data: " + e.getMessage());
        }
    }
    
    private void extractKeywords(String text, int minLength) {
        // Very simple keyword extraction - split by spaces and punctuation
        String[] words = text.split("\\s+|\\p{Punct}");
        for (String word : words) {
            word = word.trim().toLowerCase();
            if (word.length() >= minLength && !word.matches(".*\\d.*")) {
                spamKeywords.add(word);
            }
        }
        
        // Also extract phrases (2-3 word combinations)
        String[] textWords = text.split("\\s+");
        for (int i = 0; i < textWords.length - 1; i++) {
            String twoWordPhrase = textWords[i].toLowerCase() + " " + textWords[i+1].toLowerCase();
            twoWordPhrase = twoWordPhrase.replaceAll("[^a-z\\s]", "").trim();
            if (twoWordPhrase.length() >= 5) {
                spamKeywords.add(twoWordPhrase);
            }
            
            if (i < textWords.length - 2) {
                String threeWordPhrase = twoWordPhrase + " " + textWords[i+2].toLowerCase();
                threeWordPhrase = threeWordPhrase.replaceAll("[^a-z\\s]", "").trim();
                if (threeWordPhrase.length() >= 8) {
                    spamKeywords.add(threeWordPhrase);
                }
            }
        }
    }
    
    private void extractSubjectPhrases(String subject) {
        // Extract individual words
        String[] words = subject.split("\\s+");
        for (String word : words) {
            word = word.trim().toLowerCase().replaceAll("[^a-z]", "");
            if (word.length() >= 4) {
                spamSubjectPhrases.add(word);
            }
        }
        
        // Extract short phrases
        if (subject.length() < 30) {
            spamSubjectPhrases.add(subject.toLowerCase());
        }
    }
    
    public double classifyEmail(Email email) {
        // Check subject for spam phrases
        String subject = email.getSubject().toLowerCase();
        int subjectSpamScore = 0;
        for (String phrase : spamSubjectPhrases) {
            if (subject.contains(phrase)) {
                subjectSpamScore += 2;
            }
        }
        
        // Check body for spam keywords
        String body = email.getBody().toLowerCase();
        int bodySpamScore = 0;
        for (String keyword : spamKeywords) {
            if (body.contains(keyword)) {
                bodySpamScore++;
            }
        }
        
        // Additional checks for common spam patterns
        if (body.matches(".*\\$\\d+.*")) {  // Contains dollar amounts
            bodySpamScore += 2;
        }
        
        if (subject.matches(".*[!]{2,}.*")) {  // Multiple exclamation marks
            subjectSpamScore += 1;
        }
        
        if (subject.toUpperCase().equals(subject) && subject.length() > 10) {  // ALL CAPS subject
            subjectSpamScore += 2;
        }
        
        // Calculate final spam score with higher weight on subject
        int totalScore = bodySpamScore + (subjectSpamScore * 2);
        
        // Classify based on threshold
        return (totalScore >= 6) ? 1.0 : 0.0;
    }
}