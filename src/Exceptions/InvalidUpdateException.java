package Exceptions;

/**
 * Exception for when error trying to Update from table
 *
 * @author Zach Kline
 */
public class InvalidUpdateException extends Exception {
  public InvalidUpdateException(String message) {
    super(message);
  }
}
