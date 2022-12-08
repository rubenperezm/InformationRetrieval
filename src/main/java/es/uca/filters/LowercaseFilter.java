package es.uca.filters;

public class LowercaseFilter implements CharFilter {
    public String filter(String str){
        return str.toLowerCase();
    }
}
