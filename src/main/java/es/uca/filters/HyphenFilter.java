package es.uca.filters;

public class HyphenFilter implements CharFilter {
    public String filter(String str){
        return str.replaceAll("-(?!\\w)|(?<!\\w)-", " ");
    }
}
