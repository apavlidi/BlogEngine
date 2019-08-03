package com.blogEngine.comment;

import com.blogEngine.BaseController;
import com.blogEngine.controller.CommentController;
import com.blogEngine.domain.Comment;
import com.blogEngine.service.CommentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

}
