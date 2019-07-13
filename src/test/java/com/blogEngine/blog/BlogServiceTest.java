package com.blogEngine.blog;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Blog;
import com.blogEngine.repository.BlogRepository;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.service.BlogService;
import java.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BlogServiceTest {

  @Rule
  public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Blog.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @MockBean
  private BlogRepository blogRepository;

  @Autowired
  private BlogService blogService;

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
