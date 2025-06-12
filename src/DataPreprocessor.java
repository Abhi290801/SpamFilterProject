import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataPreprocessor {
    private List<String> stopWords = Arrays.asList("and", "the", "is", "in", "at", "of", "a", "to");

    public String cleanText(String text) {
        // Basic text cleaning logic
        String cleanedText = text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();

        // Remove stop words
        cleanedText = Arrays.stream(cleanedText.split("\\s+"))
                .filter(word -> !stopWords.contains(word))
                .collect(Collectors.joining(" "));

        return cleanedText.trim();
    }
}