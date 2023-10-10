package pl.iwaniuk.webapi.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileContrller {

    @GetMapping(value = "/{fileName:.+}")
    public ResponseEntity<Resource> getImageAsResource(@PathVariable String fileName, HttpServletRequest request) {
        Path filePath = Paths.get("/Users/jakub/Desktop/web-api/files").resolve(fileName).toAbsolutePath().normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {

                String contentType = null;
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                if(contentType == null) {
                    contentType = "application/octet-stream";
                }



                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
