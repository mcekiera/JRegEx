package Model.Regex.Type;

/**
 * Provides a general interface for diverse group of Enum classes which serves to describe a specific construct
 * within regular expression syntax
 */
public interface SpecificType {
    Type getGeneralType();
    String getDescription();
    String getBasicForm();
}
