package com.example.hackathonback.gpt;

import java.util.ArrayList;
import java.util.List;

public class DiffUtil {

    public static String generateDiff(String original, String modified) {
        String[] originalLines = original.split("\\r?\\n");
        String[] modifiedLines = modified.split("\\r?\\n");

        List<String> diffResult = new ArrayList<>();

        int maxLength = Math.max(originalLines.length, modifiedLines.length);

        for (int i = 0; i < maxLength; i++) {
            String originalLine = i < originalLines.length ? originalLines[i] : "";
            String modifiedLine = i < modifiedLines.length ? modifiedLines[i] : "";

            if (!originalLine.equals(modifiedLine)) {
                diffResult.add("- " + originalLine);
                diffResult.add("+ " + modifiedLine);
            } else {
                diffResult.add("  " + originalLine);
            }
        }
        return String.join("\n", diffResult);
    }
}