package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.SysFileStoreItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFileStoreItemReposity extends PagingAndSortingRepository<SysFileStoreItem,Integer> {

    /**
     * 根据相对地址查询
     * @param relativeUrl
     * @return
     */
  Iterable<SysFileStoreItem> findByRelativeUrl(String relativeUrl);
}
