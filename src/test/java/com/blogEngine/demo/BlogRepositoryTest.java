package com.blogEngine.demo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.blogEngine.demo.domain.Blog;
import com.blogEngine.demo.repository.BlogRepository;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
public class BlogRepositoryTest {

  @Autowired
  private BlogRepository blogRepository;

  @AfterEach
  void clean() {
  }

  @BeforeEach
  void setup() throws Exception {
    Blog blog = new Blog("title");
    blog.setDate(LocalDate.now());
    blog.setText("test");
    blogRepository.saveBlog(blog);
  }

  @Test
  public void findBlogByTitle_returnsBlog() {
    Blog blog = new Blog("title");
    blog.setDate(LocalDate.now());
    blog.setText("test");
    blogRepository.saveBlog(blog);

    Blog title = blogRepository.findByTitle("title");

    System.out.println(title);
    System.out.println(title.getText());
    System.out.println(title.getDate());

    assertThat(title.getText()).isEqualTo("test");
  }

}
