package com.codeofli.gulimall.search.controller;

import com.codeofli.gulimall.search.service.SearchService;
import com.codeofli.gulimall.search.vo.SearchParam;
import com.codeofli.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = {"/search.html","/"})
    public String getSearchPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        //SearchResult result=searchService.getSearchResult(searchParam);
        //model.addAttribute("result", result);
        return "search";
    }
}
