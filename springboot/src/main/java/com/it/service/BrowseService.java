package com.it.service;

import com.it.entity.BrowseRecord;

import java.util.List;

public interface BrowseService {

    boolean insert(String userId, String productId);

    List<BrowseRecord>  getList();
    List<BrowseRecord>  getListMy();
    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);
}
