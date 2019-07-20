package com.blogEngine;

import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BaseController {

  @MockBean
  public MappingMongoConverter mappingMongoConverter; //for auditing purposes of dataMongoDB

}
