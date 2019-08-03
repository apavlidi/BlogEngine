package com.blogEngine.comment;

import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import com.blogEngine.restExceptions.CommentNotFoundException;
import com.blogEngine.service.CommentService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

        given(commentRepository.findCommentBy(anyString())).willReturn(tempComment);
        Comment comment = commentService.getCommentById(commentId);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(tempComment.getId());
        assertThat(comment.getBlog()).isNotNull();
        assertThat(comment.getComments()).isNotNull();
        assertThat(comment.getComments().size()).isEqualTo(1);
    }

    @Test
    public void getCommentsShouldReturnAllComments() {
        given(commentRepository.findAll()).willReturn(Arrays.asList(new Comment("commentId1"), new Comment("commentId2")));

        List<Comment> comments = commentService.getComments();

        assertThat(comments).isNotNull();
        assertThat(comments.size()).isEqualTo(2);
        AssertionsForClassTypes.assertThat(comments.stream().anyMatch(findId("commentId1"))).isTrue();
        AssertionsForClassTypes.assertThat(comments.stream().anyMatch(findId("commentId2"))).isTrue();
    }

    @Test(expected = CommentNotFoundException.class)
    public void getCommentByNonExistentIdShouldThrowNotCommentFoundException() {
        String commentId = "nonExistent";

        given(commentRepository.findCommentBy(anyString())).willReturn(null);
        commentService.getCommentById(commentId);
    }

    @Test
    public void saveCommentShouldReturnSavedComment() {
        String commentId = "12345";
        Comment newComment = new Comment(commentId);
        newComment.setBlog(new Blog("test blog"));
        newComment.setComments(Collections.singletonList(new Comment()));

        given(commentRepository.saveComment(any(Comment.class))).willReturn(newComment);
        Comment comment = commentService.saveComment(newComment);

        assertThat(comment).isNotNull();
        assertThat(comment.getId()).isEqualTo(newComment.getId());
        assertThat(comment.getBlog()).isNotNull();
        assertThat(comment.getComments()).isNotNull();
        assertThat(comment.getComments().size()).isEqualTo(1);
    }

    @Test
    public void deleteCommentShouldReturnDeletedComment() {
        String commentId = "12345";
        Comment commentToBeDeleted = new Comment(commentId);
        commentToBeDeleted.setBlog(new Blog("test blog"));
        commentToBeDeleted.setComments(Collections.singletonList(new Comment()));

        given(commentRepository.deleteBy(anyString())).willReturn(commentToBeDeleted);
        Comment deletedComment = commentService.delete(commentId);

        assertThat(deletedComment).isNotNull();
        assertThat(deletedComment.getId()).isEqualTo(commentToBeDeleted.getId());
        assertThat(deletedComment.getBlog()).isNotNull();
        assertThat(deletedComment.getBlog()).isEqualTo(commentToBeDeleted.getBlog());
        assertThat(deletedComment.getText()).isEqualTo(commentToBeDeleted.getText());
        assertThat(deletedComment.getComments()).isNotNull();
        assertThat(deletedComment.getComments().size()).isEqualTo(commentToBeDeleted.getComments().size());
    }

    @Test(expected = CommentNotFoundException.class)
    public void deleteCommentWithInvalidIdShouldThrowCommentNotFoundException() {
        String commentId = "nonExistent";
        Comment commentToBeDeleted = new Comment(commentId);
        commentToBeDeleted.setBlog(new Blog("test blog"));
        commentToBeDeleted.setComments(Collections.singletonList(new Comment()));

        given(commentRepository.deleteBy(anyString())).willReturn(null);
        commentService.delete(commentId);
    }

    @Test
    public void updateCommentShouldReturnUpdatedComment() {
        String commentId = "12345";
        Comment commentToBeUpdated = new Comment(commentId);
        commentToBeUpdated.setBlog(new Blog("test blog"));
        commentToBeUpdated.setText("Text");
        commentToBeUpdated.setComments(Collections.singletonList(new Comment()));

        Comment mockedComment = new Comment(commentId);
        commentToBeUpdated.setText("Updated Text");

        given(commentRepository.update(anyString(), any(Comment.class))).willReturn(mockedComment);
        Comment updatedComment = commentService.updateComment(commentId, mockedComment);

        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getId()).isEqualTo(mockedComment.getId());
        assertThat(updatedComment.getText()).isEqualTo(mockedComment.getText());
    }

    @Test(expected = CommentNotFoundException.class)
    public void updateCommentWithInvalidIdShouldThrowCommentNotFoundException() {
        String commentId = "12345";
        Comment commentToBeUpdated = new Comment(commentId);
        commentToBeUpdated.setBlog(new Blog("test blog"));
        commentToBeUpdated.setText("Text");
        commentToBeUpdated.setComments(Collections.singletonList(new Comment()));

        Comment mockedComment = new Comment(commentId);
        commentToBeUpdated.setText("Updated Text");

        given(commentRepository.update(anyString(), any(Comment.class))).willReturn(null);
        commentService.updateComment(commentId, mockedComment);
    }

    private Predicate<Comment> findId(String id) {
        return comment -> comment.getId().equals(id);
    }

}
