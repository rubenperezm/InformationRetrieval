package es.uca.filters;

public class BracketsFilter implements CharFilter {
    // ( ) { } [ ]
    public String filter(String str){
        return str.replaceAll("[\\(\\){}\\[\\]]", " ");
    }
}
