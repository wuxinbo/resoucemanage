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
        Metadata metadata = ImageMetadataReader.readMetadata(new File("E:\\Seafile\\photo\\2021\\2021-12\\19\\export\\DSC_2112.jpg"));
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            Collection<Tag> tags = directory.getTags();
            for (Tag tag : tags) {
                System.out.println(tag.getTagName()+" "+tag.getDescription());
            }
        }
        System.out.println(metadata.toString());

    }

}
