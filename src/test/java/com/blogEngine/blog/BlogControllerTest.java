package com.blogEngine.blog;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blogEngine.BaseController;
import com.blogEngine.controller.BlogController;
import com.blogEngine.domain.Blog;
import com.blogEngine.domain.Profile;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BlogController.class) //it only boots the component that is defined.
public class BlogControllerTest extends BaseController {

  private final String DOMAIN_BASE_URL = "/blogs";
  private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BlogService blogService;

  @Test
  public void getBlog_ShouldReturnBlog() throws Exception {
    Calendar date = Calendar.getInstance();
    given(blogService.getBlogByTitle(anyString())).willReturn(new Blog("test", date));

    dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));

    mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("text").value("test"))
        .andExpect(jsonPath("date").value(dateFormatter.format(date.getTime())));
  }

  @Test
  public void postBlog_ShouldReturnStatusOK() throws Exception {
    given(blogService.saveBlog(new Blog("test"))).willReturn(new Blog("test", Calendar.getInstance()));

    Blog mockBlog = new Blog("test");
    mockBlog.setText("test text");
    mockBlog.setProfile(new Profile("testProfile"));

    mockMvc.perform(MockMvcRequestBuilders.post(DOMAIN_BASE_URL)
        .content(asJsonString(mockBlog))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @Ignore
  public void deleteBlog_ShouldReturnStatusOK() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(DOMAIN_BASE_URL + "/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void putBlog_ShouldReturnStatusOK() throws Exception {
    Blog newBlog = new Blog("blog 1");
    newBlog.setText("some text of a blog post");
    newBlog.setProfile(new Profile("alexis"));

    given(blogService.editBlogByTitle(any(Blog.class), anyString())).willReturn(new Blog("blog changed"));

    mockMvc.perform(MockMvcRequestBuilders.put(DOMAIN_BASE_URL + "/1")
        .content(asJsonString(newBlog))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("title").value("blog changed"))
        .andExpect(status().isOk());
  }

  @Test
  public void getBlog_notFound() throws Exception {
    given(blogService.getBlogByTitle(anyString())).willThrow(new BlogNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL + "/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getBlogs_shouldReturnAllBlogs() throws Exception {
    List blogsList = Arrays.asList(new Blog("blog 1"), new Blog("blog 2"));
    given(blogService.getBlogs()).willAnswer((Answer<List<Blog>>) invocation -> blogsList);

    mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL))
        .andExpect(status().isOk());
  }

  @Test
  public void putInvaldBlog_ShouldReturnBadRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put(DOMAIN_BASE_URL + "/1")
        .content(asJsonString(new Blog()))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
