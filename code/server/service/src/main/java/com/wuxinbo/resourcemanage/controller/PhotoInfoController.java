package com.wuxinbo.resourcemanage.controller;

import com.wuxinbo.resourcemanage.jni.ImageMagick;
import com.wuxinbo.resourcemanage.model.*;
import com.wuxinbo.resourcemanage.reposity.PhotoInfoReposity;
import com.wuxinbo.resourcemanage.service.FileInfoService;
import com.wuxinbo.resourcemanage.service.PhotoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("photo")
@CrossOrigin("*")
public class PhotoInfoController extends BaseController {
    @Autowired
    PhotoInfoReposity photoInfoReposity;
    @Autowired
    FileInfoService fileInfoService;
    @RequestMapping("get")
    void getPhoto(Integer mid, HttpServletRequest request, HttpServletResponse response) {
        Optional<PhotoInfo> photo = photoInfoReposity.findById(mid);
        //是否获取压缩还是未压缩的图片
        String compress = request.getParameter("compress");
        photo.ifPresent(it -> {
            try {
                //生成缩略图
                String filepath = it.getSysFileStoreItem().getSysFileStoreNode().getLocalPath() +
                        it.getSysFileStoreItem().getRelativeUrl();
                //判断是否需要返回压缩图片
                if (compress==null||compress.equalsIgnoreCase("")){
                    ImageMagick.compressImage(it);
                    File thumbFile = new File(it.getThumbFilePath());
                    if (thumbFile.exists()){
                        filepath =thumbFile.getPath();
                    }
                }
                try (
                        FileInputStream fis = new FileInputStream(filepath)
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
                pageSize == null ? Constant.DEFAULT_PAGESIZE : pageSize);

        Page<PhotoInfo> all = photoInfoReposity.findBySysFileStoreItemFileType(page, "jpg");
        return all;
    }

    /**
     * 更新
     *
     * @param photoInfoList
     * @return
     */
    @RequestMapping("/update")
    protected Result update(@RequestBody List<PhotoInfo> photoInfoList) {
        if (photoInfoList != null && !photoInfoList.isEmpty()) {
            for (PhotoInfo photoInfo : photoInfoList) {
                photoInfoReposity.updateLike(photoInfo.getLike(), photoInfo.getMid());
            }
        }
        return Result.success();
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody
                             List<PhotoInfo> photoInfoList){
        // 删除相册中的照片
        if (photoInfoList!=null&& !photoInfoList.isEmpty()){
            for (PhotoInfo photoInfo : photoInfoList) {
                //循环删除
                PhotoInfo deletePhoto = photoInfoReposity.getByMid(photoInfo.getMid());
                if (deletePhoto!=null){
                    SysFileStoreItem sysFileStoreItem = deletePhoto.getSysFileStoreItem();
                    if (sysFileStoreItem!=null){
                       File file = new File(sysFileStoreItem.getSysFileStoreNode().getLocalPath()+sysFileStoreItem.getRelativeUrl());
                       if (file.exists()) file.delete();
                        fileInfoService.deletePhoto(file, sysFileStoreItem.getSysFileStoreNode());
                    }
                }
            }
        }
        return Result.success();
    }
    /**
     * 根据拍摄日期查询照片
     *
     * @param shotDate
     * @return
     */
    @RequestMapping("listByShotDate")
    List<PhotoInfo> listByShotDate(String shotDate) {
        if (shotDate == null || shotDate.equals("")) {
            return new ArrayList<>();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        try {
            Date date = simpleDateFormat.parse(shotDate);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            instance.add(Calendar.DATE, 1);
            return photoInfoReposity.findByshotTimeBetween(date, instance.getTime());
        } catch (ParseException e) {
            logger.error("parseException", e);
        }
        return new ArrayList<>();
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
        List list = null;
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
                    list = photoInfoReposity.queryPhotoGroupByShotTime();
                case ALL_SHOT_TIME:
                    list = photoInfoReposity.queryPhotoGroupByShotTimeAll();
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
