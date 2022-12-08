package es.uca.filters;

public class SpacesFilter implements CharFilter {
    // Double space, \n and \t
    public String filter(String str){
        return str.replaceAll("\\s+", " ");
    }
}
