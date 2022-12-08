package es.uca.filters;

import java.util.ArrayList;
import java.util.List;

public class TermsFilterManager {
    private TermsFilter filters;

    public TermsFilterManager(){
        filters = new StopWordsFilter();
    }
    public void setFilter(TermsFilter filter) {
        filters = filter;
    }
    public List<String> execute(List<String> vTerms){
        List<String> result = vTerms;
        result = filters.filter(vTerms);
        return result;
    }
}