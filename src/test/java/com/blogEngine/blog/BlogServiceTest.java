package com.blogEngine.blog;

import com.blogEngine.MongoCleanupRule;
import com.blogEngine.domain.Blog;
import com.blogEngine.repository.BlogRepository;
import com.blogEngine.restExceptions.BlogNotFoundException;
import com.blogEngine.service.BlogService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

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
    Calendar date = Calendar.getInstance();
    given(blogRepository.findByTitle("title")).willReturn(new Blog("test", date));

    Blog blog = blogService.getBlogByTitle("title");

    assertThat(blog.getDate().toString()).isEqualTo(date.toString());
    assertThat(blog.getText()).isEqualTo("test");
  }

  @Test
  public void saveBlog_returnNewBlogInfo() {
    Blog newBlog = new Blog("newBlog", Calendar.getInstance());
    given(blogRepository.saveBlog(newBlog)).willReturn(newBlog);

    Blog blog = blogService.saveBlog(newBlog);

    assertThat(blog.getDate()).isEqualTo(newBlog.getDate());
    assertThat(blog.getText()).isEqualTo(newBlog.getText());
  }

  @Test
  public void deleteBlog_returnDeletedBlogInfo() {
    Blog blogTobeDeleted = new Blog("newBlog", Calendar.getInstance());
    given(blogRepository.deleteBlog(blogTobeDeleted.getTitle())).willReturn(blogTobeDeleted);

    Blog blog = blogService.deleteBlog(blogTobeDeleted.getTitle());

    assertThat(blog.getDate()).isEqualTo(blogTobeDeleted.getDate());
    assertThat(blog.getText()).isEqualTo(blogTobeDeleted.getText());
  }


  @Test
  public void getBlogs_shouldReturnAllBlogs() {
    List<Blog> blogsList = Arrays.asList(new Blog("blog 1"), new Blog("blog 2"));
    given(blogRepository.getBlogs()).willAnswer((Answer<List>) invocation -> blogsList);

    List<Blog> blogs = blogService.getBlogs();

    assertThat(blogs).isNotNull();
    assertThat(blogs.size()).isEqualTo(2);
      AssertionsForClassTypes.assertThat(blogs.stream().anyMatch(findTitle("blog 1"))).isTrue();
      AssertionsForClassTypes.assertThat(blogs.stream().anyMatch(findTitle("blog 2"))).isTrue();
  }

  @Test
  public void putBlog_shouldEditBlog() {
    String titleOfBlogInDB = "title of new blog";
    Blog newBlog = new Blog(titleOfBlogInDB);
    Blog mockedUpdatedBlog = new Blog(titleOfBlogInDB);

    given(blogRepository.updateBlog(anyString(), any(Blog.class)))
        .willReturn(mockedUpdatedBlog);

    Blog updatedBlog = blogService.editBlogByTitle(newBlog, titleOfBlogInDB);

    assertThat(mockedUpdatedBlog.getTitle()).isEqualTo(updatedBlog.getTitle());
  }

  @Test(expected = BlogNotFoundException.class)
  public void getBlogDetails_noFound() {
    given(blogRepository.findByTitle("title")).willReturn(null);

    blogService.getBlogByTitle("title");
  }

    private Predicate<Blog> findTitle(String title) {
        return blog -> blog.getTitle().equals(title);
    }

}
