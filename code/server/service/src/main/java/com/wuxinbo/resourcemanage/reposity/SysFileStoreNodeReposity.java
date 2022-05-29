package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.SysFileStoreNode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysFileStoreNodeReposity extends PagingAndSortingRepository<SysFileStoreNode,Integer> {
    SysFileStoreNode findByLocalPath(String path);
}
