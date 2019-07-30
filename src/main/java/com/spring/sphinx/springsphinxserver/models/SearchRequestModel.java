package com.spring.sphinx.springsphinxserver.models;

public class SearchRequestModel {
    public String value;
    public int page;
    public int limit;
    public int offset;

    public SearchRequestModel(){
        value = "";
        page = 1;
        limit = 10;
        offset = 0;
    }
}
