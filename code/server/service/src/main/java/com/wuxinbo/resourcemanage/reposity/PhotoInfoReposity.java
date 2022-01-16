package com.wuxinbo.resourcemanage.reposity;

import com.wuxinbo.resourcemanage.model.PhotoInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoInfoReposity extends CrudRepository<PhotoInfo,Integer> {

}
