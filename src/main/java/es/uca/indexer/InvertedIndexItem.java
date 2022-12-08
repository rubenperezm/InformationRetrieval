package es.uca.indexer;

import java.util.HashMap;
import java.util.Map;

public class InvertedIndexItem{
    private Double IDF;
    private Map<String, Double> parejas;

    public InvertedIndexItem(String doc, Double tf){
        this.IDF = 0.0;
        this.parejas = new HashMap<>();
        this.addPareja(doc, tf);
    }

    public Double getIDF(){return this.IDF;}

    public void setIDF(Double idf){this.IDF = idf;}

    public Map<String, Double> getParejas(){return this.parejas;}
    public void addPareja(String doc, Double tf){this.parejas.put(doc, tf);}
}