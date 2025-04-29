package Exceptions;

/**
 * Exception for when error trying to delete from table
 *
 * @author Zach Kline
 */
public class InvalidDeleteException extends Exception{
  public InvalidDeleteException(String message) {
    super(message);
  }
}
