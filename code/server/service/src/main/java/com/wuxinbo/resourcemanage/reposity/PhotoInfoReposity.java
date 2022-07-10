package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.PhotoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoInfoReposity extends PagingAndSortingRepository<PhotoInfo,Integer> {
    /**
     * 通过Id去重
     * @param fileId
     * @return
     */
    PhotoInfo findByFileId(Integer fileId);
    Page<PhotoInfo> findBySysFileStoreItemFileType(Pageable page,String fileType);
}
