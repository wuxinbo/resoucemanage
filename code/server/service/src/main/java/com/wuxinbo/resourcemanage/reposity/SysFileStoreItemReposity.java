package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.SysFileStoreItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysFileStoreItemReposity extends PagingAndSortingRepository<SysFileStoreItem,Integer> {

    /**
     * 根据相对地址查询
     * @param relativeUrl
     * @return
     */
    SysFileStoreItem findByRelativeUrl(String relativeUrl);
}
