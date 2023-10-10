package pl.iwaniuk.webapi.services;

import org.springframework.web.multipart.MultipartFile;

public interface
FileService {
    String saveFile(MultipartFile file);
    String saveFile(MultipartFile file,String fName);
    String saveFileC(MultipartFile file,String catalogName);
    void deleteFile(String path);
}
