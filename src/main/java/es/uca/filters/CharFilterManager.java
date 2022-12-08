package es.uca.filters;

import java.util.ArrayList;
import java.util.List;

public class CharFilterManager {
    private List<CharFilter> filters;

    public CharFilterManager(){
        filters = new ArrayList<>();
    }
    public void addFilter(CharFilter filter) {
        filters.add(filter);
    }

    public void addFilters(){
        addFilter(new BracketsFilter());
        addFilter(new QuotesFilter());
        addFilter(new PunctuationFilter());
        addFilter(new SymbolsFilter());
        addFilter(new NumbersFilter());
        addFilter(new HyphenFilter());
        addFilter(new SpacesFilter());
        addFilter(new LowercaseFilter());
    }
    public String execute(String str){
        String result = str;
        for (CharFilter filter : filters){
            result = filter.filter(result);
        }
        return result;
    }
}