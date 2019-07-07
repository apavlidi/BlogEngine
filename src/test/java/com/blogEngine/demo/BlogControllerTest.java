package com.blogEngine.demo;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogEngine.demo.controller.BlogController;
import com.blogEngine.demo.domain.Blog;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogController.class)
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
  public void getBlog_notFound() throws Exception {
    given(blogService.getBlogByTitle(anyString())).willThrow(new BlogNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get("/blogs/1"))
        .andExpect(status().isNotFound());
  }

}
