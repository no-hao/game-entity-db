package Table;

/**
 * Class to turn different Displays into
 * usable @class DataProvider types.
 *
 * @author Zach Kline
 */
public class DataProviderFactory {
  public static DataProvider createProvider(TableType type) {
    switch (type) {
      case CHARACTERS:
        return new DisplayOneDataProvider();
      case PLAYERS:
        return new DisplayTwoDataProvider();
      case ABILITIES:
        return new DisplayThreeDataProvider();
      case LOCATIONS:
        return new DisplayFourDataProvider();
      default:
        throw new IllegalArgumentException("Unknown TableType: " + type);
    }
  }
}