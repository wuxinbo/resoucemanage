package com.wuxinbo.resourcemanage;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class FileTest {



    @Test
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

}
