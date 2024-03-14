package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.PhotoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface PhotoInfoReposity extends PagingAndSortingRepository<PhotoInfo,Integer> {
    /**
     * 通过Id去重
     * @param fileId
     * @return
     */
    List<PhotoInfo> findByFileId(Integer fileId);
    Page<PhotoInfo> findBySysFileStoreItemFileType(Pageable page,String fileType);

    /**
     * 根据镜头统计
     * @return
     */
    @Query("select lens,count(1) from PhotoInfo where lens is not null group by lens order by count(1)")
    List queryPhotoGroupByLens();

    /**
     * 根据焦段统计
     * @return
     */
    @Query("select focusLength,count(1) from PhotoInfo where focusLength is not null group by focusLength order by count(1)")
    List queryPhotoGroupByFoucus();
    @Query(value = "select date_format(i.shot_time,'%Y-%m-%d'),count(1) from photo_info i left join sys_file_store_item s on i.file_id=s.mid" +
            " where i.shot_time is not null and s.relative_url like '%export%'" +
            "group by date_format(i.shot_time,'%Y-%m-%d')\n" +
            " order by count(1) desc limit 0,20",nativeQuery = true)
    List queryPhotoGroupByShotTime();
}
