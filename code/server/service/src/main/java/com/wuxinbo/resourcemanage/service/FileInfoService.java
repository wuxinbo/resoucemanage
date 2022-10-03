package com.wuxinbo.resourcemanage.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wuxinbo.resourcemanage.jni.FileWatch;
import com.wuxinbo.resourcemanage.model.BarChartData;
import com.wuxinbo.resourcemanage.model.PhotoInfo;
import com.wuxinbo.resourcemanage.model.SysFileStoreItem;
import com.wuxinbo.resourcemanage.model.SysFileStoreNode;
import com.wuxinbo.resourcemanage.reposity.PhotoInfoReposity;
import com.wuxinbo.resourcemanage.reposity.SysFileStoreItemReposity;
import com.wuxinbo.resourcemanage.reposity.SysFileStoreNodeReposity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 本地文件管理方法
 */
@Service
public class FileInfoService extends BaseService implements InitializingBean {
    @Autowired
    private SysFileStoreItemReposity sysFileStoreItemReposity;
    @Autowired
    private PhotoInfoReposity photoInfoReposity;
    @Autowired
    private SysFileStoreNodeReposity sysFileStoreNodeReposity;
    @Autowired
    private TaskExecutor taskExecutor;
    /**
     * 扫描文件并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanFile(SysFileStoreNode sysFileStoreNode){
        File dir = new File(sysFileStoreNode.getLocalPath());
        handleDir(dir.getPath(), sysFileStoreNode);
    }

//    @Transactional(rollbackFor = Exception.class)
    public void filewatch(){
        Iterable<SysFileStoreNode> all = sysFileStoreNodeReposity.findAll();
        if (all!=null){

            all.forEach(node->{
                    taskExecutor.execute(()->{
                        FileWatch fileWatch =new FileWatch();
                        while (true){
                           String fileName = fileWatch.watchDir(node.getLocalPath());
                           if (!StringUtils.isEmpty(fileName)){
                               //
                               logger.info("file is change fileName is "+fileName);
                               try {
                                   scanPhoto(fileName,node);
                               }catch (Exception e){
                                   logger.error("scanPhoto Fail ",e);
                               }
                           }
                        }
                    });
            });
        }
    }

    public void scanPhoto(String filename,SysFileStoreNode node ){
       File file =new File( node.getLocalPath()+File.separator+filename);
       if (file.isDirectory()){
           logger.info("file is dir ,fileName is "+file.getPath());
           return;
       }
        //解析路径
        SysFileStoreItem sysFileStoreItem = saveFileInfo(file, node);
        if (sysFileStoreItem!=null){
            readPhotoInfoMeta(sysFileStoreItem);
        }
    }

    /**
     * 从本地文件获取元数据信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void readPhotoInfoMeta(SysFileStoreItem item){
        Iterable<SysFileStoreItem> fileStoreItems =null;
        if (item!=null){
            fileStoreItems = Arrays.asList(item);
        }else{
            fileStoreItems =sysFileStoreItemReposity.findAll();
        }
        for (SysFileStoreItem sysFileStoreItem : fileStoreItems) {
            if (sysFileStoreItem.getFileType()==null||
                    (!sysFileStoreItem.getFileType().equalsIgnoreCase("JPG")&&
                    !sysFileStoreItem.getFileType().equalsIgnoreCase("NEF"))

            ){
                continue;
            }
            Metadata metadata = null;
            PhotoInfo photoInfo =new PhotoInfo();
            photoInfo.setFileId(sysFileStoreItem.getMid());
            PhotoInfo result = photoInfoReposity.findByFileId(photoInfo.getFileId());
            try {
                File photo = new File(sysFileStoreItem.getSysFileStoreNode().getLocalPath() + sysFileStoreItem.getRelativeUrl());
                if (photo.exists()){
                    metadata = ImageMetadataReader.readMetadata(photo);
                }else{
                    //删除记录
                    if (photoInfo!=null){
                        photoInfoReposity.delete(photoInfo);
                        sysFileStoreItemReposity.delete(sysFileStoreItem);
                    }
                }
            } catch (ImageProcessingException e) {
                logger.error("ImageProcessingException",e);
                continue;
            } catch (IOException e) {
                logger.error("IOException",e);
                //删除照片
                continue;
            }
            Iterable<Directory> directories = metadata.getDirectories();
            for (Directory directory : directories) {
                Collection<Tag> tags = directory.getTags();
                photoInfo.parsetagInfo(tags);
            }

            if (result!=null){
                photoInfo.setMid(result.getMid());
                photoInfo.setUpdateTime(new Date());
            }else{
                photoInfo.setCreateTime(new Date());
            }
            photoInfoReposity.save(photoInfo);
        }
    }

    private SysFileStoreItem saveFileInfo(File file,SysFileStoreNode sysFileStoreNode){
        SysFileStoreItem item =new SysFileStoreItem();
        item.setFileName(file.getName());
        item.setFileSize(Long.valueOf(file.length()).intValue());
        item.setNodeId(sysFileStoreNode.getMid());
        item.setRelativeUrl(file.getPath().replace(sysFileStoreNode.getLocalPath(),""));
        item.setSysFileStoreNode(sysFileStoreNode);
        String[] names = file.getName().split("\\.");
        if (names.length>1){
            item.setFileType(names[1]);
        }
        if (file.getName().endsWith("tmp")){
            return null;
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
        return item;
    }
    /**
     * 使用递归的方式处理文件
     */
    private void handleDir(String path,SysFileStoreNode sysFileStoreNode){
        File dir =new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            saveFileInfo(file,sysFileStoreNode);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        filewatch();
    }
}
