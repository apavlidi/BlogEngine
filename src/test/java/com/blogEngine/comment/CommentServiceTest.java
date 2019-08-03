package com.blogEngine.comment;

import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import com.blogEngine.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    public void getCommentById_ShouldReturnComment() {
        String commentId = "12345";
        Comment tempComment = new Comment(commentId);
        tempComment.setBlog(new Blog("test blog"));
        tempComment.setComments(Collections.singletonList(new Comment()));

        given(commentRepository.getCommentById(anyString())).willReturn(tempComment);
        Comment comment = commentService.getCommentById(commentId);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(tempComment.getId());
        assertThat(comment.getBlog()).isNotNull();
        assertThat(comment.getComments()).isNotNull();
        assertThat(comment.getComments().size()).isEqualTo(1);
    }

}
