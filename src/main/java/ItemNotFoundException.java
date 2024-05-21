public class ItemNotFoundException extends Throwable {
    public ItemNotFoundException(String itemName) {
        super(itemName);
    }
}
