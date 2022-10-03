package com.wuxinbo.resourcemanage.controller;

import com.wuxinbo.resourcemanage.model.BarChartData;
import com.wuxinbo.resourcemanage.model.Constant;
import com.wuxinbo.resourcemanage.model.PhotoGroupBy;
import com.wuxinbo.resourcemanage.model.PhotoInfo;
import com.wuxinbo.resourcemanage.reposity.PhotoInfoReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("photo")
@CrossOrigin("*")
public class PhotoInfoController extends BaseController {
    @Autowired
    PhotoInfoReposity photoInfoReposity;

    @RequestMapping("get")
    void getPhoto(Integer mid, HttpServletRequest request, HttpServletResponse response) {
        Optional<PhotoInfo> photo = photoInfoReposity.findById(mid);
        photo.ifPresent(it -> {
            try {
                try (
                        FileInputStream fis = new FileInputStream(it.getSysFileStoreItem().getSysFileStoreNode().getLocalPath() + it.getSysFileStoreItem().getRelativeUrl())
                ) {
                    ServletOutputStream outputStream = response.getOutputStream();
                    byte[] data = new byte[2048];
                    int length = 0;
                    while ((length = fis.read(data)) != -1) {
                        outputStream.write(data, 0, length);
                    }
                    outputStream.close();
                }
            } catch (FileNotFoundException e) {
                logger.error("FileNotFoundException", e);
            } catch (IOException e) {
                logger.error("FileNotFoundException", e);
            }
        });
    }

    @RequestMapping("listByPage")
    Page<PhotoInfo> listByPage(Integer pageSize, Integer currentPage,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        Pageable page = PageRequest.of(currentPage == null ? 0 : currentPage,
                pageSize == null ? Constant.DEFAULT_PAGESIZE : pageSize, Sort.by(Sort.Direction.DESC, "mid"));

        Page<PhotoInfo> all = photoInfoReposity.findBySysFileStoreItemFileType(page, "jpg");
        return all;
    }

    /**
     * 分组查询
     *
     * @param group
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("queryPhotoGroupby")
    BarChartData queryPhotoGroupby(Integer group,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        List list=null;
        PhotoGroupBy groupEnum
                = PhotoGroupBy.getEnumByValue(group.intValue());
        if (groupEnum != null) {
            switch (groupEnum) {
                case LENS:
                    list = photoInfoReposity.queryPhotoGroupByLens();
                    break;
                case FOUCUS_LENGTH:
                    list = photoInfoReposity.queryPhotoGroupByFoucus();
                    break;
                case SHOT_TIME:
                    list =photoInfoReposity.queryPhotoGroupByShotTime();
            }
        }
        BarChartData data = new BarChartData();
        if (list != null && !list.isEmpty()) {
            for (Object item : list) {
                Object[] o = (Object[]) item;
                data.addCategory(o[0].toString());
                data.addData(o[1]);
            }
        }
        return data;

    }


}
