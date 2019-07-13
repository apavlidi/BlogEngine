package com.blogEngine;

import org.junit.rules.ExternalResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

public class MongoCleanupRule extends ExternalResource {


  private final Object testClassInstance;
  private final Class<?>[] collectionClasses;
  private final String fieldName;
  private final String getterName;

  /**
   * Creates a MongoCleanupRule using the template names <code>mongoTemplate</code> for the member
   * variable, and <code>getMongoTemplate</code> for the getter.
   * @param testClassInstance the test class instance itself.
   * @param collectionClasses the entity classes representing the collections to clean.
   */
  public MongoCleanupRule(final Object testClassInstance, final Class<?>... collectionClasses) {
    this(testClassInstance, "mongoTemplate", "getMongoTemplate", collectionClasses);
  }

  /**
   * Creates a MongoCleanupRule with given name for the template member resp. getter.
   * @param testClassInstance the test class instance itself.
   * @param fieldOrGetterName the name of the mongo template member variable resp. getter method.
   * @param collectionClasses the entity classes representing the collections to clean.
   */
  public MongoCleanupRule(final Object testClassInstance, final String fieldOrGetterName,
      final Class<?>... collectionClasses) {
    this(testClassInstance, fieldOrGetterName, fieldOrGetterName, collectionClasses);
  }

  private MongoCleanupRule(final Object testClassInstance, final String fieldName,
      final String getterName, final Class<?>... collectionClasses) {
    Assert.notNull(testClassInstance, "parameter 'testClassInstance' must not be null");
    Assert.notNull(fieldName, "parameter 'fieldName' must not be null");
    Assert.notNull(getterName, "parameter 'getterName' must not be null");
    Assert.notNull(collectionClasses, "parameter 'collectionClasses' must not be null");
    Assert.noNullElements(collectionClasses,
        "array 'collectionClasses' must not contain null elements");

    this.fieldName = fieldName;
    this.getterName = getterName;
    this.testClassInstance = testClassInstance;
    this.collectionClasses = collectionClasses;
  }

  @Override
  protected void before() {
    dropCollections();
  }

  @Override
  protected void after() {
    dropCollections();
  }

  private Class<?>[] getMongoCollectionClasses() {
    return collectionClasses;
  }

  private void dropCollections() {
    for (final Class<?> type : getMongoCollectionClasses()) {
      getMongoTemplate().dropCollection(type);
    }
  }

  private MongoTemplate getMongoTemplate() {
    try {
      Object value = ReflectionTestUtils.getField(testClassInstance, fieldName);
      if (value instanceof MongoTemplate) {
        return (MongoTemplate) value;
      }
      value = ReflectionTestUtils.invokeGetterMethod(testClassInstance, getterName);
      if (value instanceof MongoTemplate) {
        return (MongoTemplate) value;
      }
    } catch (final IllegalArgumentException e) {
      // throw exception with dedicated message at the end
    }
    throw new IllegalArgumentException(
        String.format(
            "%s expects either field '%s' or method '%s' in order to access the required MongoTemmplate",
            this.getClass().getSimpleName(), fieldName, getterName));
  }

}
