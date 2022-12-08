package es.uca.filters;

public class SymbolsFilter implements CharFilter {
    // + * @ # $ ^ % ? ! = < > ! ¿ & |
    public String filter(String str){
        return str.replaceAll("[\\+\\*@#\\$\\^%\\?!\\/=<>!¿&\\|]", " ");
    }
}
