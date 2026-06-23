package post.service.Post.Service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import post.service.Post.Service.config.AppConfig;

@FeignClient(name = "uploader-service", path = "/uploads/file", url = "${UPLOADER_SERVICE_URI:http://localhost:9050}", configuration = AppConfig.class)
public interface UploaderServiceClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file);
}
