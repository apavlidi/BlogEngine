package com.blogEngine.blog;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.blog.domain.Blog;
import com.blogEngine.blog.repository.BlogRepository;
import java.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BlogRepositoryTest {

  @Rule
  public final MongoCleanupRule cleanupRule = new MongoCleanupRule(this, Blog.class);

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private BlogRepository blogRepository;

  @AfterEach
  void clean() {
  }

  @BeforeEach
  void setup() {
    Blog blog = new Blog("title");
    blog.setDate(LocalDate.now());
    blog.setText("test");
    blogRepository.saveBlog(blog);
  }

  @Test
  public void saveBlog_returnsSavedBlog() {
    LocalDate date = LocalDate.of(2019, 3, 1);
    Blog blog = new Blog();
    blog.setDate(date);
    blog.setTitle("title");
    blog.setText("test");
    Blog saveBlog = blogRepository.saveBlog(blog);

    assertThat(saveBlog.getText()).isEqualTo(blog.getText());
    assertThat(saveBlog.getTitle()).isEqualTo(blog.getTitle());
    assertThat(saveBlog.getDate()).isEqualTo(blog.getDate());
  }

  @Test
  public void deleteBlog_returnsDeletedBlog() {
    LocalDate date = LocalDate.of(2019, 3, 1);
    Blog blog = new Blog();
    blog.setDate(date);
    blog.setTitle("title");
    blog.setText("test");
    Blog saveBlog = blogRepository.saveBlog(blog);

    Blog deleteBlog = blogRepository.deleteBlog(saveBlog.getTitle());

    assertThat(deleteBlog.getText()).isEqualTo(saveBlog.getText());
    assertThat(deleteBlog.getTitle()).isEqualTo(saveBlog.getTitle());
    assertThat(deleteBlog.getDate()).isEqualTo(saveBlog.getDate());
  }

  @Test
  public void findBlogByTitle_returnsBlog() {
    Blog blog = new Blog("title");
    blog.setDate(LocalDate.now());
    blog.setText("test");
    blogRepository.saveBlog(blog);

    Blog title = blogRepository.findByTitle("title");

    assertThat(title.getText()).isEqualTo("test");
  }

}
