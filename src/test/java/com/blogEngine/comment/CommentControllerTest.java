package com.blogEngine.comment;

import com.blogEngine.BaseController;
import com.blogEngine.controller.CommentController;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Comment;
import com.blogEngine.restExceptions.CommentNotFoundException;
import com.blogEngine.service.CommentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static com.blogEngine.Utilies.getSizePropertyForFieldAnnotation;
import static com.blogEngine.Utilies.getStringWithLength;
import static com.blogEngine.blog.BlogControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class) //it only boots the component that is defined.
public class CommentControllerTest extends BaseController {

    private final String DOMAIN_BASE_URL = "/comments";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void getCommentIdShouldReturnComment() throws Exception {
        String commentId = "12345";

        Comment comment = new Comment(commentId);
        given(commentService.getCommentById(anyString())).willReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL + "/" + commentId)
                .accept(APPLICATION_JSON)).andExpect(jsonPath("id")
                .value(commentId)).andExpect(status().isOk());
    }

    @Test
    public void getNonExistentCommentIdShouldReturn404() throws Exception {
        String commentId = "nonExistentId";

        given(commentService.getCommentById(anyString())).willThrow(CommentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL + "/" + commentId)
                .accept(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void getCommentsShouldReturnAllComments() throws Exception {
        given(commentService.getComments()).willReturn(Arrays.asList(new Comment("commentId1"), new Comment("commentId2")));

        MvcResult mvcResult = mockMvc.perform(get(DOMAIN_BASE_URL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonBodyResponse = mvcResult.getResponse().getContentAsString();
        assertThat(jsonBodyResponse.indexOf("commentId1")).isNotEqualTo(-1);
        assertThat(jsonBodyResponse.indexOf("commentId2")).isNotEqualTo(-1);
    }


    @Test
    public void postValidCommentShouldReturnNewComment() throws Exception {
        Comment comment = new Comment();
        Blog blog = new Blog();
        comment.setBlog(blog);
        comment.setText("This a reply to a blog post");

        Comment mockedComment = new Comment("commentId");
        mockedComment.setText(comment.getText());
        given(commentService.saveComment(any(Comment.class))).willReturn(mockedComment);

        mockMvc.perform(MockMvcRequestBuilders.post(DOMAIN_BASE_URL, comment)
                .content(asJsonString(comment))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(jsonPath("id").value("commentId"))
                .andExpect(jsonPath("text").value("This a reply to a blog post"))
                .andExpect(status().isOk());
    }

    @Test
    public void postCommentWithoutBlogShouldReturn400() throws Exception {
        Comment comment = new Comment();
        comment.setText("This a reply to a blog post");

        mockMvc.perform(post(DOMAIN_BASE_URL).content(asJsonString(comment))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postCommentWithLessThanMinTextReturn400() throws Exception {
        Comment comment = new Comment();

        int minTextLength = getSizePropertyForFieldAnnotation("text", "min", Comment.class);
        String text = getStringWithLength(minTextLength - 1);
        comment.setText(text);

        mockMvc.perform(post(DOMAIN_BASE_URL).content(asJsonString(comment))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCommentShouldReturnComment() throws Exception {
        String commentId = "commentId";
        Comment comment = new Comment(commentId);
        comment.setText("Some reply to a blog");
        given(commentService.delete(anyString())).willReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.delete(DOMAIN_BASE_URL + "/" + commentId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(jsonPath("id").value("commentId"))
                .andExpect(jsonPath("text").value("Some reply to a blog"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNonExistentCommentShouldReturnComment() throws Exception {
        String nonExistentCommentId = "nonExistentId";

        given(commentService.delete(anyString())).willThrow(CommentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete(DOMAIN_BASE_URL + "/" + nonExistentCommentId)
                .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putValidCommentShouldReturnUpdatedComment() throws Exception {
        String commentId = "12345";
        Comment comment = new Comment(commentId);
        Blog blog = new Blog();
        comment.setBlog(blog);
        comment.setText("This a reply to a blog post");

        Comment mockedComment = new Comment(commentId);
        mockedComment.setText(comment.getText());
        mockedComment.setBlog(comment.getBlog());
        mockedComment.setText("This is the updated text");
        given(commentService.updateComment(anyString(), any(Comment.class))).willReturn(mockedComment);

        mockMvc.perform(MockMvcRequestBuilders.put(DOMAIN_BASE_URL + "/" + commentId, comment)
                .content(asJsonString(comment))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(jsonPath("id").value("12345"))
                .andExpect(jsonPath("text").value("This is the updated text"))
                .andExpect(status().isOk());
    }

}