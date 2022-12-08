package es.uca.indexer;

import es.uca.filters.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;

public class Indexer {
    private static HashMap<String, Integer> frecuencias;
    private static HashMap<String, InvertedIndexItem> invertedIndexStruct;
    private static List<String> getTerms(String texto){
        return new ArrayList<>(Arrays.asList(texto.split("\\s")));
    }

    private static HashMap<String, Double> longitudDocumentos;

    private static Gson gson;
    private static void tfStep1(List<String> terminos) {
        frecuencias.clear();
        for (String str : terminos) {
            if (frecuencias.containsKey(str)) {
                frecuencias.put(str, frecuencias.get(str) + 1);
            } else {
                frecuencias.put(str, 1);
            }
        }
    }

    public static void tfStep2(String doc) {
        int value;
        double tf;
        String key;
        for (Map.Entry<String, Integer> entry : frecuencias.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            tf = 1 + Math.log(value) / Math.log(2);
            if (invertedIndexStruct.containsKey(key))
                invertedIndexStruct.get(key).addPareja(doc, tf);
            else {
                invertedIndexStruct.put(key, new InvertedIndexItem(doc, tf));
            }
        }
    }

    public static void calculateIdfAndLength(int nDocuments){
        double IDF, peso, tfIdf;
        int nDocsTerm;
        String doc;
        for(Map.Entry<String, InvertedIndexItem> entry : invertedIndexStruct.entrySet()){
            nDocsTerm = entry.getValue().getParejas().size();
            IDF = Math.log((double)nDocsTerm / nDocuments) / Math.log(2);
            entry.getValue().setIDF(IDF);

            Map<String, Double> parejas = entry.getValue().getParejas();
            for (Map.Entry<String, Double> pareja : parejas.entrySet()){
                doc = pareja.getKey();
                peso = pareja.getValue();
                tfIdf = peso * IDF;
                if (longitudDocumentos.containsKey(doc)) {
                    longitudDocumentos.put(doc, longitudDocumentos.get(doc) + tfIdf * tfIdf); // Suma del cuadrado
                }else {
                    longitudDocumentos.put(doc, tfIdf * tfIdf); // Primer cuadrado
                }
            }
        }

        longitudDocumentos.replaceAll((k, v) -> Math.sqrt(v)); // Raiz
    }

    public static void store(Object obj, String fileName){
        String json = gson.toJson(obj);
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))){
            out.write(json);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void index(){
        gson = new Gson();
        frecuencias = new HashMap<>();
        invertedIndexStruct = new HashMap<>();
        longitudDocumentos = new HashMap<>();

        CharFilterManager charFilterManager = new CharFilterManager();
        TermsFilterManager termsFilterManager = new TermsFilterManager();

        charFilterManager.addFilters();

        File folder = new File("../corpus/corpus");
        File[] listOfFiles = folder.listFiles();
        String texto;
        List<String> vTerms;
        if (listOfFiles != null) {
            System.out.println("Indexing...");
            for (File f : listOfFiles) {
                try {
                    texto = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath()))); // Leer documento
                    texto = charFilterManager.execute(texto); // PreprocesarCaracteres
                    vTerms = getTerms(texto); // ExtraerPalabras
                    vTerms = termsFilterManager.execute(vTerms); // PreprocesarTerms
                    tfStep1(vTerms); // CalcularTF_Paso1
                    tfStep2(f.getName()); // CalcularTF_Paso2
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            calculateIdfAndLength(listOfFiles.length); // Calcular IDF y Long Documentos

            // Guardar información
            store(invertedIndexStruct, "./invertedIndex.json");
            store(longitudDocumentos, "./documentsLength.json");

            System.out.println("Finished.");

        }else{
            System.out.println("Folder not detected");
        }
    }
}