import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;
    int initialMenuSize;

    //REFACTOR ALL THE REPEATED LINES OF CODE
    @BeforeEach
    public void setUp() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
        initialMenuSize = restaurant.getMenu().size();
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        // Mock the current time to 11:00:00, which is between the opening and closing time
        Mockito.doReturn(LocalTime.parse("11:00:00")).when(spyRestaurant).getCurrentTime();
        assertTrue(spyRestaurant.isRestaurantOpen());
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        // Mock the current time to 23:00:00, which is outside the opening and closing time
        Mockito.doReturn(LocalTime.parse("23:00:00")).when(spyRestaurant).getCurrentTime();
        assertFalse(spyRestaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {
        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws ItemNotFoundException {
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(ItemNotFoundException.class,
                () -> restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //<<<<<<< ORDER COST >>>>>>>>>>>>>>>
    // Use cases
    // 1. Cost of the item names being sent should be calculated properly
    // 2. In case the restaurant is closed while this operation is being performed then proper exception should be
    //    thrown.
    @Test
    public void order_total_of_items_is_matching() throws RestaurantClosedException,ItemNotFoundException{
        List<String> itemNames = new ArrayList<String>();
        itemNames.add("Sweet corn soup");
        itemNames.add("Vegetable lasagne");
        assertEquals(388, restaurant.getOrderTotal(itemNames));
    }

    @Test
    public void order_total_throws_exception_if_restaurant_is_closed() throws RestaurantClosedException, ItemNotFoundException{
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        // Mock the current time to 23:00:00, which is outside the opening and closing time
        Mockito.doReturn(LocalTime.parse("23:00:00")).when(spyRestaurant).getCurrentTime();
        List<String> itemNames = new ArrayList<String>();
        itemNames.add("Sweet corn soup");
        itemNames.add("Vegetable lasagne");
        assertThrows(RestaurantClosedException.class,
                () -> spyRestaurant.getOrderTotal(itemNames));
    }
}