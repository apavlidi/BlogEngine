package com.blogEngine.blog;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Blog;
import com.blogEngine.repository.BlogRepository;
import java.util.Calendar;
import java.util.List;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.Rule;
import org.junit.Test;
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

  @Test
  public void saveBlog_returnsSavedBlog() {
    Calendar date = Calendar.getInstance();
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
    Calendar date = Calendar.getInstance();
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
    blog.setDate(Calendar.getInstance());
    blog.setText("test");
    blogRepository.saveBlog(blog);

    Blog title = blogRepository.findByTitle("title");

    assertThat(title.getText()).isEqualTo("test");
  }

  @Test
  public void updateBlogByTitle_shouldReturnUpdatedBlog() {
    Blog blog = new Blog("title");
    blog.setDate(Calendar.getInstance());
    blog.setText("test");
    blogRepository.saveBlog(blog);
    Blog blogFromDB = blogRepository.findByTitle(blog.getTitle());

    Blog newBlog = new Blog("updated blog title");
    blog.setDate(Calendar.getInstance());
    blog.setText("updated blog text");

    Blog updatedBlogFromDB = blogRepository.updateBlog(blog.getTitle(), newBlog);

    assertThat(updatedBlogFromDB.getTitle()).isEqualTo(newBlog.getTitle());
    assertThat(updatedBlogFromDB.getText()).isEqualTo(newBlog.getText());
    assertThat(updatedBlogFromDB.getId()).isEqualTo(blogFromDB.getId());
  }

  @Test
  public void findBlogs_shouldReturnAllBlogs() {
    Blog blog = new Blog("blog 1");
    blog.setDate(Calendar.getInstance());
    blog.setText("test");
    blogRepository.saveBlog(blog);

    blog = new Blog("blog 2");
    blog.setDate(Calendar.getInstance());
    blog.setText("test2");
    blogRepository.saveBlog(blog);

    List<Blog> blogs = blogRepository.getBlogs();

    AssertionsForInterfaceTypes.assertThat(blogs).isNotNull();
    assertThat(blogs.size()).isEqualTo(2);
    assertThat(blogs.get(0).getTitle()).isEqualTo("blog 1");
    assertThat(blogs.get(0).getText()).isEqualTo("test");
    assertThat(blogs.get(1).getTitle()).isEqualTo("blog 2");
    assertThat(blogs.get(1).getText()).isEqualTo("test2");
  }

}
