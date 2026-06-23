package post.service.Post.Service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;
import post.service.Post.Service.auth.AuthContextHolder;
import post.service.Post.Service.client.ConnectionsServiceClient;
import post.service.Post.Service.client.UploaderServiceClient;
import post.service.Post.Service.dto.PersonDto;
import post.service.Post.Service.dto.PostCreateRequestDto;
import post.service.Post.Service.dto.PostDto;
import post.service.Post.Service.entity.Post;
import post.service.Post.Service.event.PostCreated;
import post.service.Post.Service.exception.ResourceNotFoundException;
import post.service.Post.Service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsServiceClient connectionsServiceClient;
    private final UploaderServiceClient uploaderServiceClient;
    private final KafkaTemplate<Long, PostCreated> postCreatedKafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, MultipartFile file) {
        Long userId = AuthContextHolder.getCurrentUserId();
        log.info("Creating post for user with id: {}", userId);

        ResponseEntity<String> imageUrl = uploaderServiceClient.uploadFile(file);

        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post.setImageUrl(imageUrl.getBody());
        post = postRepository.save(post);

        List<PersonDto> personDtoList = connectionsServiceClient.getFirstDegreeConnections(userId);

        for(PersonDto person: personDtoList){
            PostCreated postCreated = PostCreated.builder()
            .postId(post.getId())
                    .content(post.getContent())
                    .userId(person.getUserId())
                    .ownerUserId(userId)
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic",postCreated);


        }
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);


        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found " +
                "with ID: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all the posts of a user with ID: {}", userId);
        List<Post> postList = postRepository.findByUserId(userId);

        return postList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }



}
