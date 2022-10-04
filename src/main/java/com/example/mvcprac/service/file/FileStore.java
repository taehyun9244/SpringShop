package com.example.mvcprac.service.file;

import com.example.mvcprac.model.Image;
import com.example.mvcprac.model.Item;
import com.example.mvcprac.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileStore {

    private final ImageRepository imageRepository;

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<Image> saveImages(List<MultipartFile> multipartFiles, Item saveItem) throws IOException {
        List<Image> storeFileResult = new ArrayList<>();
        boolean firstImage = false;

        for (int i = 0; i < multipartFiles.size(); i++) {
            MultipartFile multipartFile = multipartFiles.get(i);
            if (multipartFile == multipartFiles.get(0)) {
                firstImage = true;
            } else firstImage = false;
           storeFileResult.add(saveImage(multipartFile, saveItem, firstImage));
        }
        return storeFileResult;
    }

    public Image saveImage(MultipartFile multipartFile, Item saveItem, boolean firstImage) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        Image image = new Image(originalFilename, storeFileName, saveItem, firstImage);
        Image saveImage = imageRepository.save(image);
        return saveImage;
    }


    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
