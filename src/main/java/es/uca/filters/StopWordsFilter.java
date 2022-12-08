package es.uca.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopWordsFilter implements TermsFilter{
    // Filter all the words in the stopwords file (and words with size < 2)
    public List<String> filter(List<String> vTerms) {
        String texto;
        List<String> result = new ArrayList<>();
        try {
            File stopwords = new File("stopwords.txt");
            texto = new String(Files.readAllBytes(Paths.get(stopwords.getAbsolutePath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> words = new ArrayList<>(Arrays.asList(texto.split("\\s")));

        for (String term : vTerms) {
            if (!words.contains(term) && term.length() > 1) {
                result.add(term);
            }
        }
        return result;
    }
}
