package com.codeofli.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**'
 * 2级分类vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2Vo {
    //父分类id
    private String catalog1Id;

    private String id;

    private String name;

    private List<Catalog3Vo> catalog3List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catalog3Vo{
        private String catalog2Id;
        private String id;
        private String name;
    }
}
