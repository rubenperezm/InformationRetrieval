package es.uca.filters;

import java.util.List;

public interface TermsFilter {
    public List<String> filter(List<String> vTerms);
}