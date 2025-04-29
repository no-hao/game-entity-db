package Exceptions;

/**
 * Exception for when error on request to DB
 *
 * @author Zach Kline
 */
public class DBException extends Throwable {
  public DBException(String message) {
    super(message);
  }
}
