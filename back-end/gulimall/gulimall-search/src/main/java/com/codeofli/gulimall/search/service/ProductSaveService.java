package com.codeofli.gulimall.search.service;

import com.codeofli.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;


public interface ProductSaveService {

    boolean saveProductAsIndices(List<SkuEsModel> skuEsModels) throws IOException;
}
