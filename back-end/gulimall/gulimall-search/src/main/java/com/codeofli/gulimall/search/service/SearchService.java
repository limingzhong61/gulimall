package com.codeofli.gulimall.search.service;

import com.codeofli.gulimall.search.vo.SearchParam;
import com.codeofli.gulimall.search.vo.SearchResult;

public interface SearchService {
    SearchResult getSearchResult(SearchParam searchParam);
}
