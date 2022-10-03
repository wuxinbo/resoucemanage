package com.wuxinbo.resourcemanage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.wuxinbo.resourcemanage.jni.FileWatch;
import org.junit.jupiter.api.Test;

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
                System.out.println(tag.getTagName()+":"+tag.getDescription());
            }
        }
        System.out.println(metadata.toString());

    }

//    @Test
    public void fileWatch(){
        FileWatch fileWatch =new FileWatch();
        while (true){
            String dirName = fileWatch.watchDir("D:\\software\\");
            System.out.println(dirName);
        }
    }

}
