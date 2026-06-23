package uploader.service.uploader.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uploader.service.uploader.service.service.UploaderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class UploaderController {

    private final UploaderService uploaderService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        String url = uploaderService.upload(file);
        return ResponseEntity.ok(url);
    }

}
