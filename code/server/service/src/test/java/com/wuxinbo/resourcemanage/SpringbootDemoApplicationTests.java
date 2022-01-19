package com.wuxinbo.resourcemanage;

import com.wuxinbo.resourcemanage.model.PhotoInfo;
import com.wuxinbo.resourcemanage.model.SysFileStoreNode;
import com.wuxinbo.resourcemanage.reposity.PhotoInfoReposity;
import com.wuxinbo.resourcemanage.reposity.SysFileStoreNodeReposity;
import com.wuxinbo.resourcemanage.service.FileInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class SpringbootDemoApplicationTests {
	@Autowired
	private PhotoInfoReposity photoInfoReposity;
	@Autowired
	private SysFileStoreNodeReposity sysFileStoreNodeReposity;
	@Autowired
	private FileInfoService fileInfoService;
	@Test
	void contextLoads() {
	}

	@Test
	public void PhotoInfo(){
		System.out.println(photoInfoReposity.count());
		PhotoInfo photoInfo =new PhotoInfo();
		photoInfo.setSpeed("1/250");
		photoInfo.setISO(100);
		photoInfo.setAperture("4");
		photoInfoReposity.save(photoInfo);
	}
	@Test
	public void fileNode(){
		SysFileStoreNode node =new SysFileStoreNode();
		node.setFileNodeName("D盘");
		node.setLocalPath("D:\\seafile\\photo");
		sysFileStoreNodeReposity.save(node);
	}
	@Test
	public void ReadFileInfo(){
		File dir =new File("D:\\seafile\\photo");
		SysFileStoreNode localPath = sysFileStoreNodeReposity.findByLocalPath(dir.getPath());
		fileInfoService.scanFile(localPath);
	}
}
