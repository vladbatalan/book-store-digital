package paw.command.command.model.dao;

public class ClientRepositoryImpl implements ClientRepositoryCustom{

    private static String collectionName = "";

    @Override
    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public void setCollectionName(String collectionName) {
        ClientRepositoryImpl.collectionName = collectionName;
    }
}
