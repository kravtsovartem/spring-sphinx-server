package com.spring.sphinx.springsphinxserver.search;

import com.spring.sphinx.springsphinxserver.SphinxConstants;
import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UserSearch {
    private SphinxClient sphinxClient = new SphinxClient(SphinxConstants.HOST, SphinxConstants.PORT);

    LinkedHashMap opts = new LinkedHashMap();

    public UserSearch() {
        opts.put("before_match", "<strong>");
        opts.put("after_match", "</strong>");
        opts.put("chunk_separator", " ... ");
        opts.put("limit", 400);
        opts.put("around", 15);
    }

    private SphinxResult GetOpenSphinxResult(String query, int limit, int offset, int maxLimit) {

        try {
            if(!sphinxClient.Open()) {
                throw new Error("SphinxClient: соединение завершилось ошибкой.");
            }

            sphinxClient.SetLimits(offset, limit, maxLimit);

            return sphinxClient.Query(query, SphinxConstants.INDEX);

        } catch (SphinxException e) {
            throw new Error(e.getStackTrace().toString());
        }
    }

    public SphinxResult Search(String query, int limit, int offset, int maxLimit) {
        SphinxResult result = new SphinxResult();
        try {
            result = GetOpenSphinxResult(query, limit, offset, maxLimit);

            String[] arrayText = new String[result.matches.length];
            for (int i = 0; i < result.matches.length; i++) {
                arrayText[i] = result.matches[i].attrValues.get(2).toString();
            }

            String[] arrHighlightText = sphinxClient.BuildExcerpts(arrayText, "index_news", query, opts);
            for (int j = 0; j < result.matches.length; j++) {
                ArrayList arr = new ArrayList();

                var item = result.matches[j];
                arr.add(item.attrValues.get(0));
                arr.add(item.attrValues.get(1));
                arr.add(item.attrValues.get(2));
                arr.add(arrHighlightText[j]);

                result.matches[j].attrValues = arr;
            }

        } catch (SphinxException e) {
            throw new Error(e.getStackTrace().toString());
        }

        sphinxClient.Close();
        return result;
    }

    public String[] Suggest(String query, int limit, int offset, int maxLimit) {
        String[] result = new String[0];
        try {
            SphinxResult sphinxResult = GetOpenSphinxResult(query, limit, offset, maxLimit);

            String[] arrayText = new String[sphinxResult.matches.length];
            for(int i = 0; i < sphinxResult.matches.length; i++) {
                arrayText[i] = sphinxResult.matches[i].attrValues.get(1).toString();
            }

            result = sphinxClient.BuildExcerpts(arrayText,"index_news", query, opts);

        } catch (SphinxException e) {
            throw new Error(e.getStackTrace().toString());
        }

        sphinxClient.Close();
        return result;
    }

    public LinkedHashMap Item(String query, int limit, int offset, int maxLimit) {

        LinkedHashMap item = new LinkedHashMap();

        SphinxResult sphinxResult = GetOpenSphinxResult(query, limit, offset, maxLimit);
        ArrayList result = sphinxResult.matches[0].attrValues;

        item.put("uid", result.get(0));
        item.put("name", result.get(1));
        item.put("text", result.get(2));

        sphinxClient.Close();
        return item;
    }
}
