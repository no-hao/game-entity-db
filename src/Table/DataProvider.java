package Table;

import Exceptions.InvalidDeleteException;
import Exceptions.InvalidInsertException;
import Exceptions.InvalidUpdateException;

/**
 * This is the underlying structure
 * that all displays have in common.
 *
 * @author Zach Kline
 */
public interface DataProvider {

  /**
   * Get all the values that will be returned
   * @return String[]
   */
  String[] getColumnNames();

  /**
   * Returns rows of data from DB
   * @return Object[][]
   */
  Object[][] getRowData();

  /**
   * All the values required
   * to complete an insert.
   * @return String[]
   */
  String[] getInsertColumnNames();


  /**
   * Data has been updated and to
   * forward update to DB
   * @param row row of data affected
   * @param col col of data affected
   * @param value The new value for the data
   * @throws InvalidUpdateException
   */
  void updateData(int row, int col, Object value) throws InvalidUpdateException;

  /**
   * Row has been deleted
   * forward deleted row to DB
   * @param row Row that was deleted
   * @throws InvalidDeleteException
   */
  void deleteRow(int row) throws InvalidDeleteException;

  /**
   * Row has been inserted
   * forward insert to DB
   * @param values
   * @throws InvalidUpdateException
   * @throws InvalidInsertException
   */
  void insertRow(Object[] values) throws InvalidUpdateException, InvalidInsertException;
}

