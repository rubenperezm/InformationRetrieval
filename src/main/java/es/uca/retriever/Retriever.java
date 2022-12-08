package es.uca.retriever;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import es.uca.filters.*;
import es.uca.indexer.InvertedIndexItem;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Retriever {
    private static JsonReader reader;
    private static Gson gson;

    private static Map<String, InvertedIndexItem> invertedIndexStruct;

    private static Map<String, Double> documentsLength;

    private static Map<String, Double> recuperados;

    private static List<String> getTerms(String texto){
        return new ArrayList<>(Arrays.asList(texto.split("\\s")));
    }

    private static void getDocs(List<String> vTerms) {
        recuperados = new HashMap<>();
        Map<String, Double> parejas;
        double idf, pond;
        String doc;

        for (String t : vTerms) {
            if (invertedIndexStruct.containsKey(t)) {
                parejas = invertedIndexStruct.get(t).getParejas();
                idf = invertedIndexStruct.get(t).getIDF();
                for (Map.Entry<String, Double> pareja : parejas.entrySet()) {
                    doc = pareja.getKey();
                    pond = pareja.getValue() * idf * idf;
                    if (recuperados.containsKey(doc)) {
                        recuperados.put(doc, recuperados.get(doc) + pond);
                    } else {
                        recuperados.put(doc, pond);
                    }
                }
            }
        }

        for (Map.Entry<String, Double> recuperado : recuperados.entrySet()){
            doc = recuperado.getKey();
            recuperados.put(doc, recuperado.getValue() / documentsLength.get(doc));
        }
    }

    private static Map<String, Double> sortDocs() {
        return recuperados.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private static void showResults(Map<String, Double> map, int n){
        Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();
        if (map.isEmpty())
            System.out.println("No results found.");
        else {
            while (it.hasNext() && n > 0) {
                Map.Entry<String, Double> entry = it.next();
                System.out.println("Document: " + entry.getKey() + " Weight: " + entry.getValue());
                read(entry.getKey());
                n--;
            }
        }
    }

    private static void read(String document){
        File f = new File("../corpus/corpus/" + document);
        try{
            String texto = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
            System.out.println(texto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void loadInvInd(){
        invertedIndexStruct = new HashMap<>();
        try{
            reader = new JsonReader(new FileReader("./invertedIndex.json"));
            invertedIndexStruct = gson.fromJson(reader, new TypeToken<Map<String, InvertedIndexItem>>(){}.getType());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void loadDocLen(){
        documentsLength = new HashMap<>();
        try{
            reader = new JsonReader(new FileReader("./documentsLength.json"));
            documentsLength = gson.fromJson(reader, new TypeToken<Map<String, Double>>(){}.getType());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void retrieve(){
        Scanner sc = new Scanner(System.in);
        gson = new Gson();

        CharFilterManager charFilterManager = new CharFilterManager();
        TermsFilterManager termsFilterManager = new TermsFilterManager();

        charFilterManager.addFilters();

        loadInvInd();
        loadDocLen();

        System.out.println("Query:");
        String texto = sc.nextLine();
        System.out.println("Max. Number of results: ");
        int nResultados = sc.nextInt();
        texto = charFilterManager.execute(texto); // PreprocesarCaracteres
        List<String> vTerms = getTerms(texto); // ExtraerPalabras
        vTerms = termsFilterManager.execute(vTerms); // PreprocesarTerms
        getDocs(vTerms); // Recuperar documentos
        Map<String, Double> sortedDocs = sortDocs(); // Ordenar resultado
        showResults(sortedDocs, nResultados); // Mostrar resultados
    }
}
