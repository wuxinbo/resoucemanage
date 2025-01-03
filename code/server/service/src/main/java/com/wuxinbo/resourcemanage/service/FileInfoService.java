package com.wuxinbo.resourcemanage.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wuxinbo.resourcemanage.jni.FileWatch;
import com.wuxinbo.resourcemanage.model.*;
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
  public void scanFile(SysFileStoreNode sysFileStoreNode) {
    File dir = new File(sysFileStoreNode.getLocalPath());
    handleDir(dir.getPath(), sysFileStoreNode);
  }

  //    @Transactional(rollbackFor = Exception.class)
  public void filewatch() {
    Iterable<SysFileStoreNode> all = sysFileStoreNodeReposity.findAll();
    if (all != null) {

      all.forEach(node -> {
        taskExecutor.execute(() -> {
          FileWatch fileWatch = new FileWatch();
          while (true) {
            FileChangeNotify fileChangeNotify = fileWatch.watchDir(node.getLocalPath());

            if (fileChangeNotify != null && !StringUtils.isEmpty(fileChangeNotify.getFilePath())) {
              //
              try {
                taskExecutor.execute(() -> {
                  scanPhoto(fileChangeNotify, node);
                });
              } catch (Exception e) {
                logger.error("scanPhoto Fail ", e);
              }
            }
          }
        });
      });
    }
  }

  /**
   * 处理底层文件系统发出文件更新通知。
   * @param notify
   * @param node
   */
  public void scanPhoto(FileChangeNotify notify, SysFileStoreNode node) {
    File file = new File(node.getLocalPath() + File.separator + notify.getFilePath());
    if (file.isDirectory()) {
      logger.info("file is dir ,fileName is " + file.getPath());
      return;
    }
    logger.info(notify.getFilePath() + " file is change action is " + notify.getAction());

    if (notify.isRemoved()) { //文件删除，数据库数据
      deletePhoto(file, node);
      return;
    }
    //由于新增文件的时候windows 操作系统会通知两次，所以这里只监听文件变动
    if (notify.isAdd()){
      return;
    }
    //解析路径
    SysFileStoreItem sysFileStoreItem = saveFileInfo(file, node);
    if (sysFileStoreItem != null) {
      readPhotoInfoMeta(sysFileStoreItem);
    }
  }

  /**
   * 从本地文件获取元数据信息
   */
  @Transactional(rollbackFor = Exception.class)
  public void readPhotoInfoMeta(SysFileStoreItem item) {
    Iterable<SysFileStoreItem> fileStoreItems = null;
    if (item != null) {
      fileStoreItems = Arrays.asList(item);
    } else {
      fileStoreItems = sysFileStoreItemReposity.findAll();
    }
    for (SysFileStoreItem sysFileStoreItem : fileStoreItems) {
      if (sysFileStoreItem.getFileType() == null ||
        (!sysFileStoreItem.getFileType().equalsIgnoreCase("JPG") &&
          !sysFileStoreItem.getFileType().equalsIgnoreCase("NEF"))

      ) {
        continue;
      }
      Metadata metadata = null;
      PhotoInfo photoInfo = new PhotoInfo();
      photoInfo.setFileId(sysFileStoreItem.getMid());
      List<PhotoInfo> result = photoInfoReposity.findByFileId(photoInfo.getFileId());
      try {
        File photo = new File(sysFileStoreItem.getSysFileStoreNode().getLocalPath() + sysFileStoreItem.getRelativeUrl());
        if (photo.exists()) {
          metadata = ImageMetadataReader.readMetadata(photo);
        } else {
          //删除记录
          if (photoInfo != null) {
            deletePhoto(photoInfo, sysFileStoreItem);
            return;
          }
        }
      } catch (ImageProcessingException e) {
        logger.error("ImageProcessingException", e);
        continue;
      } catch (IOException e) {
        logger.error("IOException", e);
        //删除照片
        continue;
      }
      Iterable<Directory> directories = metadata.getDirectories();
      for (Directory directory : directories) {
        Collection<Tag> tags = directory.getTags();
        photoInfo.parsetagInfo(tags);
      }

      if (result != null&& !result.isEmpty()) {
        photoInfo.setMid(result.get(0).getMid());
        photoInfo.setUpdateTime(new Date());
      } else {
        photoInfo.setCreateTime(new Date());
      }
      photoInfoReposity.save(photoInfo);
    }
  }

  private void deletePhoto(File file, SysFileStoreNode sysFileStoreNode) {
    Iterable<SysFileStoreItem> result =
      sysFileStoreItemReposity.findByRelativeUrl(file.getPath().replace(sysFileStoreNode.getLocalPath(), ""));
    if (result != null) {
      for (SysFileStoreItem sysFileStoreItem : result) {
        List<PhotoInfo> photos = photoInfoReposity.findByFileId(sysFileStoreItem.getMid());
        //删除多个文件
        if (photos!=null&&!photos.isEmpty()){
          photos.forEach( it->photoInfoReposity.delete(it));
        }
        sysFileStoreItemReposity.delete(sysFileStoreItem);
      }
    }
  }

  private void deletePhoto(PhotoInfo photoInfo, SysFileStoreItem sysFileStoreItem) {
    photoInfoReposity.delete(photoInfo);
    sysFileStoreItemReposity.delete(sysFileStoreItem);

  }

  private SysFileStoreItem saveFileInfo(File file, SysFileStoreNode sysFileStoreNode) {
    SysFileStoreItem item = new SysFileStoreItem();
    item.setFileName(file.getName());
    item.setFileSize(Long.valueOf(file.length()).intValue());
    item.setNodeId(sysFileStoreNode.getMid());
    item.setRelativeUrl(file.getPath().replace(sysFileStoreNode.getLocalPath(), ""));
    item.setSysFileStoreNode(sysFileStoreNode);
    String[] names = file.getName().split("\\.");
    if (names.length > 1) {
      item.setFileType(names[1]);
    }
    if (file.getName().endsWith("tmp")) {
      return null;
    }
    Iterable<SysFileStoreItem> result = sysFileStoreItemReposity.findByRelativeUrl(item.getRelativeUrl());
    if (result != null&& result.iterator().hasNext()) {
      item.setMid(result.iterator().next().getMid());
      item.setUpdateTime(new Date());
    } else {
      item.setCreateTime(new Date());

    }
    System.out.println("filePath is " + item.getRelativeUrl());
    sysFileStoreItemReposity.save(item);
    return item;
  }

  /**
   * 使用递归的方式处理文件
   */
  private void handleDir(String path, SysFileStoreNode sysFileStoreNode) {
    File dir = new File(path);
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        handleDir(file.getPath(), sysFileStoreNode);
      }
      saveFileInfo(file, sysFileStoreNode);
    }

  }

  @Override
  public void afterPropertiesSet() throws Exception {
    filewatch();
  }
}
