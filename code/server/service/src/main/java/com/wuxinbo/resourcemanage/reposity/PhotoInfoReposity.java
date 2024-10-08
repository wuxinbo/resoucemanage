package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.PhotoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface PhotoInfoReposity extends PagingAndSortingRepository<PhotoInfo, Integer> {
    /**
     * 通过Id去重
     *
     * @param fileId
     * @return
     */
    List<PhotoInfo> findByFileId(Integer fileId);

    @Query(value = "select i.*,item.relative_url from photo_info i left join sys_file_store_item item on i.file_id =item.mid " +
            "where item.mid is not null " +
            "and item.file_type ='jpg'" +
            " and relative_url like '%export%'" +
            "order by i.shot_time desc ", nativeQuery = true,
            countQuery = "select count(1) from photo_info i left join sys_file_store_item item on" +
                    " i.file_id =item.mid where item.mid is not null and item.file_type ='jpg' and relative_url like '%export%'"
    )
    Page<PhotoInfo> findBySysFileStoreItemFileType(Pageable page, String fileType);


    /**
     * 更新like
     *
     * @param it
     */
    @Transactional
    @Modifying
    @Query(value = "update photo_info set `like` =:like,update_time=now() where mid =:mid", nativeQuery = true)
    void updateLike(Integer like,Integer mid);

    /**
     * 通过拍摄日期查询
     *
     * @return
     */
    @Query(value = "select i.*,item.relative_url from photo_info i left join sys_file_store_item item on i.file_id =item.mid " +
            "where item.mid is not null " +
            "and item.file_type ='jpg'" +
            " and item.relative_url like '%export%'" +
            "and i.shot_time between :startDate and :endDate " +
            "order by i.shot_time desc ", nativeQuery = true
    )
    List<PhotoInfo> findByshotTimeBetween(Date startDate, Date endDate);

    /**
     * 根据镜头统计
     *
     * @return
     */
    @Query("select lens,count(1) from PhotoInfo where lens is not null group by lens order by count(1)")
    List queryPhotoGroupByLens();

    /**
     * 根据焦段统计
     *
     * @return
     */
    @Query("select focusLength,count(1) from PhotoInfo where focusLength is not null group by focusLength order by count(1)")
    List queryPhotoGroupByFoucus();

    @Query(value = "select date_format(i.shot_time,'%Y-%m-%d'),count(1) from photo_info i left join sys_file_store_item s on i.file_id=s.mid" +
            " where i.shot_time is not null and s.relative_url like '%export%'" +
            "group by date_format(i.shot_time,'%Y-%m-%d')\n" +
            " order by count(1) desc limit 0,20", nativeQuery = true)
    List queryPhotoGroupByShotTime();

    @Query(value = "select date_format(i.shot_time,'%Y-%m-%d'),count(1) from photo_info i left join sys_file_store_item s on i.file_id=s.mid" +
            " where i.shot_time is not null and s.relative_url like '%export%'" +
            "group by date_format(i.shot_time,'%Y-%m-%d')\n" +
            " order by date_format(i.shot_time,'%Y-%m-%d') desc ", nativeQuery = true)
    List queryPhotoGroupByShotTimeAll();
}
