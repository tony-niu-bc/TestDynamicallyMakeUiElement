package com.wzhnsc.testdynamicallymakeuielement;

import java.io.Serializable;

/**
 * 发现模块中的文章分类
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    // 分类的唯一码标识，零(０) - 全部，其它分类名与唯一码对由后台服务器下发
    private int catalogId;

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    // 分类的名称
    private String catalogName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
