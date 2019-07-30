package com.spring.sphinx.springsphinxserver.controllers;

import com.spring.sphinx.springsphinxserver.models.ItemModel;
import com.spring.sphinx.springsphinxserver.models.SearchRequestModel;
import com.spring.sphinx.springsphinxserver.search.UserSearch;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SearchController {

    private Map<String, Object> result = new HashMap<>();
    UserSearch userSearch = new UserSearch();

    @RequestMapping("/search")
    public Map<String, Object> index(@RequestBody SearchRequestModel searchModel) {

        int page = searchModel.page;
        int limit = searchModel.limit;
        int offset = (page - 1) * limit;
        int maxLimit = 100;

        result.put("data", userSearch.Search(searchModel.value, limit, offset, maxLimit));
        return result;
    }

    @RequestMapping("/search/suggest")
    public Map<String, Object> suggest(@RequestBody SearchRequestModel searchModel) {

        int maxLimit = searchModel.limit;
        result.put("data", userSearch.Suggest(searchModel.value, searchModel.limit, searchModel.offset, maxLimit));
        return result;
    }

    @RequestMapping("/item")
    public Map<String, Object> suggest(@RequestBody ItemModel itemModel) {

        result.put("data", userSearch.Item(itemModel.value, 1, 0, 1));
        return result;
    }
}
