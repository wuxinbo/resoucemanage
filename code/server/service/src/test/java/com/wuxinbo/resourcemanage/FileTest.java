package com.wuxinbo.resourcemanage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wuxinbo.resourcemanage.jni.FileWatch;
import com.wuxinbo.resourcemanage.model.FileChangeNotify;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class FileTest {

    static {
        System.loadLibrary("filewatch");
    }

    //    @Test
    public void readInfo() throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(new File("D:\\seafile\\photo\\2021\\07\\10\\export\\DSC_1658.jpg"));
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            Collection<Tag> tags = directory.getTags();
            for (Tag tag : tags) {
                System.out.println(tag.getTagName() + ":" + tag.getDescription());
            }
        }
        System.out.println(metadata.toString());

    }

//        @Test
    public void fileWatch() throws IOException, AWTException, InterruptedException {
//        FileWatch fileWatch = new FileWatch();
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = ImageIO.read(new File("E:\\code\\resourceManage\\code\\qt\\logo.png"));
        TrayIcon myTrayIcon = new TrayIcon(image, "hello");
        systemTray.add(myTrayIcon);
        myTrayIcon.setImageAutoSize(true);

        myTrayIcon.displayMessage("resourceManage","文件已删除", TrayIcon.MessageType.INFO);
        Thread.sleep(5000);
//        while (true){
//            FileChangeNotify fileChangeNotify = fileWatch.watchDir("D:\\seafile\\");
//            if (fileChangeNotify!=null){
//                System.out.println(fileChangeNotify.getFilePath());
//            }
//        }
    }

}
