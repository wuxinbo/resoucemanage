package com.wuxinbo.resourcemanage.service;

import com.wuxinbo.resourcemanage.model.SysFileStoreItem;
import com.wuxinbo.resourcemanage.model.SysFileStoreNode;
import com.wuxinbo.resourcemanage.reposity.SysFileStoreItemReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

/**
 * 本地文件管理方法
 */
@Service
public class FileInfoService {
    @Autowired
    private SysFileStoreItemReposity sysFileStoreItemReposity;
    /**
     * 扫描文件并保存到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanFile(SysFileStoreNode sysFileStoreNode){
        File dir = new File(sysFileStoreNode.getLocalPath());
        handleDir(dir.getPath(), sysFileStoreNode);
    }

    /**
     * 使用递归的方式处理文件
     */
    private void handleDir(String path,SysFileStoreNode sysFileStoreNode){
        File dir =new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()){
                handleDir(file.getPath(),sysFileStoreNode);
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
