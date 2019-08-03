package com.blogEngine.comment;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Comment;
import com.blogEngine.domain.Profile;
import com.blogEngine.repository.CommentRepository;
import com.blogEngine.repository.ProfileRepository;
import com.mongodb.gridfs.GridFSFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static com.blogEngine.config.DatabaseProfiles.TEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //it boots a server
@ActiveProfiles(TEST)
public class CommentIT {

    @Rule
    public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Profile.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private String DOMAIN_COMMENTS_URL = "/comments";

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getCommentById_ShouldReturnComment() {
        //setup
        Comment tempComment = new Comment();
        Comment savedComment = commentRepository.save(tempComment);
        String commentId = savedComment.getId();

        //act
        ResponseEntity<Comment> response = restTemplate.getForEntity(DOMAIN_COMMENTS_URL + "/" + commentId, Comment.class);

        //assert
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getStatusCode()).isEqualTo(OK);

    }

}
