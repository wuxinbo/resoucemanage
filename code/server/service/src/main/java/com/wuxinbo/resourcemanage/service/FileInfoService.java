package com.wuxinbo.resourcemanage.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wuxinbo.resourcemanage.model.PhotoInfo;
import com.wuxinbo.resourcemanage.model.SysFileStoreItem;
import com.wuxinbo.resourcemanage.model.SysFileStoreNode;
import com.wuxinbo.resourcemanage.reposity.PhotoInfoReposity;
import com.wuxinbo.resourcemanage.reposity.SysFileStoreItemReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * 本地文件管理方法
 */
@Service
public class FileInfoService extends BaseService{
    @Autowired
    private SysFileStoreItemReposity sysFileStoreItemReposity;
    @Autowired
    private PhotoInfoReposity photoInfoReposity;
    /**
     * 扫描文件并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanFile(SysFileStoreNode sysFileStoreNode){
        File dir = new File(sysFileStoreNode.getLocalPath());
        handleDir(dir.getPath(), sysFileStoreNode);
    }

    /**
     * 从本地文件获取元数据信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void readPhotoInfoMeta(){
        Iterable<SysFileStoreItem> all = sysFileStoreItemReposity.findAll();
        for (SysFileStoreItem sysFileStoreItem : all) {
            if (sysFileStoreItem.getFileType()==null||
                    (!sysFileStoreItem.getFileType().equals("JPG")&&
                    !sysFileStoreItem.getFileType().equals("NEF"))

            ){
                continue;
            }
            if (!sysFileStoreItem.getRelativeUrl().contains("2022")){
                continue;
            }
            Metadata metadata = null;
            try {
                metadata = ImageMetadataReader.readMetadata(new File(sysFileStoreItem.getSysFileStoreNode().getLocalPath()+sysFileStoreItem.getRelativeUrl()));
            } catch (ImageProcessingException e) {
                logger.error("ImageProcessingException",e);
                continue;
            } catch (IOException e) {
                logger.error("IOException",e);
                continue;
            }
            Iterable<Directory> directories = metadata.getDirectories();
            PhotoInfo photoInfo =new PhotoInfo();
            photoInfo.setFileId(sysFileStoreItem.getMid());
            for (Directory directory : directories) {
                Collection<Tag> tags = directory.getTags();
                photoInfo.parsetagInfo(tags);
            }
            PhotoInfo result = photoInfoReposity.findByFileId(photoInfo.getFileId());
            if (result!=null){
                photoInfo.setMid(result.getMid());
                photoInfo.setUpdateTime(new Date());
            }else{
                photoInfo.setCreateTime(new Date());
            }
            photoInfoReposity.save(photoInfo);
        }
    }
    /**
     * 使用递归的方式处理文件
     */
    private void handleDir(String path,SysFileStoreNode sysFileStoreNode){
        File dir =new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                if (file.getPath().contains("2022")){
                    handleDir(file.getPath(),sysFileStoreNode);
                }
            }
            SysFileStoreItem item =new SysFileStoreItem();
            item.setFileName(file.getName());
            item.setFileSize(Long.valueOf(file.length()).intValue());
            item.setNodeId(sysFileStoreNode.getMid());
            item.setRelativeUrl(file.getPath().replace(sysFileStoreNode.getLocalPath(),""));
            String[] names = file.getName().split("\\.");
            if (names.length>1){
                item.setFileType(names[1]);
            }else{
            }
            SysFileStoreItem reuslt = sysFileStoreItemReposity.findByRelativeUrl(item.getRelativeUrl());
            if (reuslt!=null){
                item.setMid(reuslt.getMid());
                item.setUpdateTime(new Date());
            }else{
                item.setCreateTime(new Date());

            }
            System.out.println("filePath is "+item.getRelativeUrl());
            sysFileStoreItemReposity.save(item);
        }
    }
}
