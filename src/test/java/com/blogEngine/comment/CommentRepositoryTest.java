package com.blogEngine.comment;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Comment;
import com.blogEngine.repository.CommentRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CommentRepositoryTest {

    @Rule
    public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Blog.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void getCommentByIdShouldReturnComment() {
        Comment comment = new Comment("commentId");
        comment.setText("Text of a comment");
        comment.setBlog(new Blog());
        comment.setComments(Collections.singletonList(new Comment("nestedCommentId")));
        mongoTemplate.save(comment);

        Comment commentFromDb = commentRepository.findCommentBy("commentId");
        assertThat(commentFromDb).isNotNull();
        assertThat(commentFromDb.getText()).isEqualTo(comment.getText());
        assertThat(commentFromDb.getId()).isEqualTo(comment.getId());
        assertThat(commentFromDb.getComments()).isNotNull();
        assertThat(commentFromDb.getComments()).isNotNull();
        AssertionsForClassTypes.assertThat(commentFromDb.getComments().stream().anyMatch(findId("nestedCommentId"))).isTrue();
    }

    @Test
    public void getCommentByInvalidIdShouldReturnNull() {
        Comment commentFromDb = commentRepository.findCommentBy("invalidId");
        assertThat(commentFromDb).isNull();
    }

    @Test
    public void saveCommentShouldReturnSavedComment() {
        Comment comment = new Comment();
        comment.setText("Text of a comment");
        comment.setBlog(new Blog());
        comment.setComments(Collections.singletonList(new Comment("nestedCommentId")));

        Comment commentFromDb = commentRepository.saveComment(comment);
        assertThat(commentFromDb).isNotNull();
        assertThat(commentFromDb.getText()).isEqualTo(comment.getText());
        assertThat(commentFromDb.getId()).isNotNull();
        assertThat(commentFromDb.getBlog()).isNotNull();
        AssertionsForClassTypes.assertThat(commentFromDb.getComments().stream().anyMatch(findId("nestedCommentId"))).isTrue();
    }

    @Test
    public void deleteCommentShouldReturnDeletedComment() {
        Comment comment = new Comment();
        comment.setText("Text of a comment");
        comment.setBlog(new Blog());
        comment.setComments(Collections.singletonList(new Comment("nestedCommentId")));
        Comment commentInDB = mongoTemplate.save(comment);

        Comment commentedDeleted = commentRepository.deleteBy(commentInDB.getId());
        assertThat(commentedDeleted).isNotNull();
        assertThat(commentedDeleted.getText()).isEqualTo(comment.getText());
        assertThat(commentedDeleted.getId()).isNotNull();
        assertThat(commentedDeleted.getBlog()).isNotNull();
        AssertionsForClassTypes.assertThat(commentedDeleted.getComments().stream().anyMatch(findId("nestedCommentId"))).isTrue();
    }

    @Test
    public void deleteCommentWithInvalidIdShouldReturnNull() {
        Comment commentedDeleted = commentRepository.deleteBy("invalidId");
        assertThat(commentedDeleted).isNull();
    }

    @Test
    public void updateCommentShouldReturnUpdatedComment() {
        Comment comment = new Comment();
        comment.setText("Text of a comment");
        comment.setBlog(new Blog());
        comment.setComments(Collections.singletonList(new Comment("nestedCommentId")));
        Comment commentInDB = mongoTemplate.save(comment);

        comment = new Comment();
        comment.setText("Text of a comment updated");

        Comment updatedComment = commentRepository.update(commentInDB.getId(), comment);
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getText()).isEqualTo(comment.getText());
        assertThat(updatedComment.getId()).isNotNull();
    }

    private Predicate<Comment> findId(String id) {
        return comment -> comment.getId().equals(id);
    }

}
