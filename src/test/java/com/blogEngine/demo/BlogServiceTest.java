package com.blogEngine.demo;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.blogEngine.demo.config.MongoConfig;
import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BlogServiceTest {

  @MockBean
  private BlogRepository blogRepository;

  @Autowired
  private BlogService blogService;

  @Before
  public void setup() {
  }

  @Test
  public void getBlogDetails_returnBlogInfo() {
    given(blogRepository.findByTitle("title")).willReturn(new Blog("test", LocalDate.now()));

    Blog blog = blogService.getBlogByTitle("title");

    assertThat(blog.getDate()).isEqualTo(LocalDate.now().toString());
    assertThat(blog.getText()).isEqualTo("test");
  }

  @Test(expected = BlogNotFoundException.class)
  public void getBlogDetails_noFound() {
    given(blogRepository.findByTitle("title")).willReturn(null);

    blogService.getBlogByTitle("title");
  }

}
