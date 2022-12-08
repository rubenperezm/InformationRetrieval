package es.uca.filters;

public class QuotesFilter implements CharFilter {
    // ' " ` ´
    public String filter(String str){
        return str.replaceAll("[\"'`´]", " ");
    }
}
