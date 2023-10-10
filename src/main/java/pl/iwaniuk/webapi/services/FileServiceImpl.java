package pl.iwaniuk.webapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    private Path dir;

    public FileServiceImpl(@Value("${file.dir}") String dir) {
        this.dir = Paths.get(dir).toAbsolutePath().normalize();
    }

    @Override
    public String saveFile(MultipartFile file) {
        String name = UUID.randomUUID().toString()+"sRIuzcoJ1t"+file.getOriginalFilename().replace(" ","");
        Path dir_file = this.dir.resolve(name);
        try {
            Files.copy(file.getInputStream(),dir_file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public String saveFile(MultipartFile file,String fName) {
       fName = fName.replace(" ","");
        String name = UUID.randomUUID().toString()+"sRIuzcoJ1t"+fName;
        Path dir_file = this.dir.resolve(name);
        try {
            Files.copy(file.getInputStream(),dir_file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public String saveFileC(MultipartFile file, String catalogName) {
        String name = UUID.randomUUID().toString()+"sRIuzcoJ1t"+file.getOriginalFilename().replace(" ","");

        this.dir = this.dir.resolve(catalogName);

        Path dir_file = this.dir.resolve(name);
        try {
            Files.copy(file.getInputStream(),dir_file, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    public void deleteFile(String path) {
        Path dir_file = this.dir.resolve(path);
        try {
            Files.delete(dir_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
