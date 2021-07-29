package com.example.MyProjectWithSecurity.data;

import com.example.MyProjectWithSecurity.Repositories.BookFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResorceStorage {

    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private final BookFileRepository bookFileRepository;

    @Autowired
    public ResorceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        Logger.getLogger(this.getClass().getName()).info("File name is "+file.getOriginalFilename());
        String fileNameTest = file.getOriginalFilename();
        String resourceURI = null;
        if(!file.isEmpty()){
            if(!new File(uploadPath).exists()){
                Files.createDirectories(Paths.get(uploadPath));
                Logger.getLogger(ResorceStorage.class.getName()).info("created image folder in "+uploadPath);
            }
            String fileName = fileNameTest;//slug + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath,fileName);
            resourceURI = uploadPath +"/"+ fileName; //"/main/" + fileName;
            file.transferTo(path);
            Logger.getLogger(this.getClass().getSimpleName()).info(fileName + "uploaded OK!");

        }
        return resourceURI;
    }

    public Path getBookFilePath(String hash) {
        Book_File bookFile = bookFileRepository.findBook_FileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        Book_File bookFile = bookFileRepository.findBook_FileByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());
        if(mimeType != null){
            return MediaType.parseMediaType(mimeType);
        }
        else{
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        Book_File bookFile = bookFileRepository.findBook_FileByHash(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file is "+bookFile.getBook().getTitle());
        Path path = Paths.get(bookFile.getPath());
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path is "+path.toString());
        return Files.readAllBytes(path);
    }
}
