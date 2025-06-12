# Spam Filter Project

A Java application that classifies emails as spam or not spam (ham) based on content analysis.

## Overview

This project implements a rule-based spam filter that analyzes email content and subject to determine if an email is likely to be spam. The application features a modern graphical user interface for easy interaction.

## Demo

https://github.com/user-attachments/assets/33387abb-540a-4b78-b968-d2fad507b0d4

## Features

- Email classification based on content analysis
- Advanced rule-based filtering algorithm
- Modern, visually appealing graphical user interface
- Comprehensive training data set
- Real-time classification feedback with visual indicators
- Interactive UI elements with hover effects

## Project Structure

```
SpamFilterProject/
├── data/
│   └── training_data.csv       # Comprehensive training data
├── lib/                        # External libraries
├── src/
│   ├── Email.java              # Email data model
│   ├── EmailClassifier.java    # Spam classification logic
│   ├── GUI.java                # Graphical user interface
│   ├── Main.java               # Application entry point
│   └── ConsoleUI.java          # Alternative console interface
├── run.sh                      # Script to run the application
└── README.md                   # This file
```

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher

### Compilation

```bash
javac -cp "." src/*.java -d .
```

### Execution

Using the provided script:
```bash
./run.sh
```

Or manually:
```bash
java -cp "." Main
```

## GUI Usage Instructions

1. Enter the sender's email address in the "From" field
2. Enter the email subject in the "Subject" field
3. Type the email body in the text area
4. Click "Classify Email" to analyze the message
5. The result will appear in the classification result panel with color coding:
   - "SPAM DETECTED" (in red with pink background) indicates a spam message
   - "NOT SPAM (HAM)" (in green with light green background) indicates a legitimate message
6. Use the "Clear" button to reset all fields

## UI Features

- Gradient background for a modern look
- Custom mail icon in the application header
- Color-coded results for intuitive feedback
- Progress indicator during classification
- Hover effects on interactive elements
- Consistent styling across all input fields
- Responsive layout that adapts to window size

## Classification Logic

The current implementation uses an advanced rule-based approach:

- Analyzes email subject and body content (sender is not used for classification)
- Extracts and matches keywords and phrases from a comprehensive training dataset
- Assigns higher weight to spam indicators found in the subject line
- Detects common spam patterns like dollar amounts and excessive punctuation
- Calculates a weighted spam score and classifies based on a threshold

## Training Data

The application includes a comprehensive training dataset with 70 examples (35 spam and 35 ham) covering a wide range of email types:

- Spam examples include phishing attempts, promotional offers, scams, and other unwanted content
- Ham examples include business communications, notifications, reminders, and other legitimate messages

## Future Improvements

- Add machine learning-based classification
- Implement Bayesian filtering
- Add ability to train the filter with user feedback
- Support email attachments analysis
- Add export/import functionality for classification results
- Implement dark mode theme option