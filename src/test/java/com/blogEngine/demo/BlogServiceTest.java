package com.blogEngine.demo;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @Test
  public void saveBlog_returnNewBlogInfo() {
    Blog newBlog = new Blog("newBlog", LocalDate.now());
    given(blogRepository.saveBlog(newBlog)).willReturn(newBlog);

    Blog blog = blogService.saveBlog(newBlog);

    assertThat(blog.getDate()).isEqualTo(newBlog.getDate().toString());
    assertThat(blog.getText()).isEqualTo(newBlog.getText());
  }

  @Test
  public void deleteBlog_returnDeletedBlogInfo() {
    Blog blogTobeDeleted = new Blog("newBlog", LocalDate.now());
    given(blogRepository.deleteBlog(blogTobeDeleted.getTitle())).willReturn(blogTobeDeleted);

    Blog blog = blogService.deleteBlog(blogTobeDeleted.getTitle());

    assertThat(blog.getDate()).isEqualTo(blogTobeDeleted.getDate().toString());
    assertThat(blog.getText()).isEqualTo(blogTobeDeleted.getText());
  }

  @Test
  public void putBlog_shouldEditBlog() {
    String title = "test title";
    Blog blogFoundFromTitle = new Blog(title, "text", LocalDate.now());
    Blog blogAfterEdit = new Blog("new title", "new text", LocalDate.now());

    given(blogRepository.findByTitle(title)).willReturn(blogFoundFromTitle);
    given(blogRepository.saveBlog(blogFoundFromTitle)).willReturn(blogAfterEdit);

    Blog newBlog = blogService.editBlogByTitle(blogAfterEdit, blogFoundFromTitle.getTitle());

    assertThat(newBlog.getDate()).isEqualTo(blogAfterEdit.getDate().toString());
    assertThat(newBlog.getText()).isEqualTo(blogAfterEdit.getText());
    assertThat(newBlog.getText()).isEqualTo(blogAfterEdit.getText());
  }

  @Test(expected = BlogNotFoundException.class)
  public void getBlogDetails_noFound() {
    given(blogRepository.findByTitle("title")).willReturn(null);

    blogService.getBlogByTitle("title");
  }

}
