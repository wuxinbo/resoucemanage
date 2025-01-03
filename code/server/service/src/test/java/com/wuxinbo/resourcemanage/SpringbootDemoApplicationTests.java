package com.wuxinbo.resourcemanage;

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
import com.wuxinbo.resourcemanage.reposity.SysFileStoreNodeReposity;
import com.wuxinbo.resourcemanage.service.FileInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

//@SpringBootTest
class SpringbootDemoApplicationTests {
	@Autowired
	private PhotoInfoReposity photoInfoReposity;
	@Autowired
	private SysFileStoreNodeReposity sysFileStoreNodeReposity;
	@Autowired
	private SysFileStoreItemReposity sysFileStoreItemReposity;
	@Autowired
	private FileInfoService fileInfoService;
//	@Test
	void contextLoads() {
	}

//	@Test
	public void PhotoInfo(){
		System.out.println(photoInfoReposity.count());
		PhotoInfo photoInfo =new PhotoInfo();
		photoInfo.setSpeed("1/250");
		photoInfo.setISO(100);
		photoInfo.setAperture("4");
		photoInfoReposity.save(photoInfo);
	}
//	@Test
	public void fileNode(){
		SysFileStoreNode node =new SysFileStoreNode();
		node.setFileNodeName("D盘");
		node.setLocalPath("D:\\seafile\\photo");
		sysFileStoreNodeReposity.save(node);
	}
//	@Test
	public void ReadFileInfo(){
		File dir =new File("D:\\seafile\\photo");
		SysFileStoreNode localPath = sysFileStoreNodeReposity.findByLocalPath(dir.getPath());
		fileInfoService.scanFile(localPath);
	}
//	@Test
	public void writePhotoInfo() throws ImageProcessingException, IOException {
		Iterable<SysFileStoreItem> fileStoreItems  =sysFileStoreItemReposity.findAll();
		Iterator<SysFileStoreItem> iterator = fileStoreItems.iterator();
		while (iterator.hasNext()){
			SysFileStoreItem sysFileStoreItem = iterator.next();
			if (sysFileStoreItem.getRelativeUrl().contains("export")&&
					sysFileStoreItem.getFileType()!=null&&
					sysFileStoreItem.getFileType().equalsIgnoreCase("jpg")){
				Metadata metadata = null;
				PhotoInfo photoInfo =new PhotoInfo();
				photoInfo.setFileId(sysFileStoreItem.getMid());
				 photoInfoReposity.findByFileId(photoInfo.getFileId());

				try {
					File photo = new File(sysFileStoreItem.getSysFileStoreNode().getLocalPath() + sysFileStoreItem.getRelativeUrl());
					if (photo.exists()){
						metadata = ImageMetadataReader.readMetadata(photo);
					}else{
						continue;
					}
				} catch (ImageProcessingException e) {
					continue;
				} catch (IOException e) {
					//删除照片
					continue;
				}
				Iterable<Directory> directories = metadata.getDirectories();
				for (Directory directory : directories) {
					Collection<Tag> tags = directory.getTags();
					photoInfo.parsetagInfo(tags);
				}
				photoInfo.setCreateTime(new Date());
				photoInfoReposity.save(photoInfo);
			}
		}
	}
}
