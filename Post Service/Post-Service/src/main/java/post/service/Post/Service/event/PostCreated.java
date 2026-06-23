package post.service.Post.Service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostCreated {
    private Long ownerUserId;
    private Long postId;
    private Long userId;
    private String content;
}
