package Table;

public class DataProviderFactory {
  public static DataProvider createProvider(TableType type) {
    switch (type) {
      case USERS:
        return new UsersDataProvider();
      case TABLE2:
        return new UsersDataProvider();
      case TABLE3:
        return new UsersDataProvider();
      default:
        throw new IllegalArgumentException("Unknown TableType: " + type);
    }
  }
}