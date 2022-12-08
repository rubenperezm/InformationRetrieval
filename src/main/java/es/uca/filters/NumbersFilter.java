package es.uca.filters;

public class NumbersFilter implements CharFilter {
    public String filter(String str){
        return str.replaceAll("\\b\\d+\\b", " ");
    }
}
