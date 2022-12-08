package es.uca.filters;

public class PunctuationFilter implements CharFilter {
    // . , : ;
    public String filter(String str){
        return str.replaceAll("[\\.,:;]", " ");
    }
}
