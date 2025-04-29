package Exceptions;

/**
 * Exception for when error trying to insert to table
 *
 * @author Zach Kline
 */
public class InvalidInsertException extends Exception {
  public InvalidInsertException(String message) {
    super(message);
  }
}
