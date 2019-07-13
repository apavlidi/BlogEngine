package com.blogEngine.blog;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogEngine.controller.BlogController;
import com.blogEngine.domain.Blog;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogController.class) //it only boots the component that is defined.
public class BlogControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BlogService blogService;

  @Test
  public void getBlog_ShouldReturnBlog() throws Exception {
    given(blogService.getBlogByTitle(anyString())).willReturn(new Blog("test", LocalDate.now()));

    mockMvc.perform(MockMvcRequestBuilders.get("/blogs/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("text").value("test"))
        .andExpect(jsonPath("date").value(LocalDate.now().toString()));
  }

  @Test
  public void postBlog_ShouldReturnStatusOK() throws Exception {
    given(blogService.saveBlog(new Blog("test"))).willReturn(new Blog("test", LocalDate.now()));

    mockMvc.perform(MockMvcRequestBuilders.post("/blogs")
        .content(asJsonString(new Blog("test")))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void deleteBlog_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/blogs/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void putBlog_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/blogs/1")
        .content(asJsonString(new Blog("test")))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getBlog_notFound() throws Exception {
    given(blogService.getBlogByTitle(anyString())).willThrow(new BlogNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get("/blogs/1"))
        .andExpect(status().isNotFound());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
