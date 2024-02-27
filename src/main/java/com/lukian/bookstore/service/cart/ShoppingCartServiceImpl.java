package com.lukian.bookstore.service.cart;

import com.lukian.bookstore.dto.shoppingcart.AddItemToCartRequestDto;
import com.lukian.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.lukian.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.lukian.bookstore.exception.EntityNotFoundException;
import com.lukian.bookstore.mapper.CartItemMapper;
import com.lukian.bookstore.mapper.ShoppingCartMapper;
import com.lukian.bookstore.model.Book;
import com.lukian.bookstore.model.CartItem;
import com.lukian.bookstore.model.ShoppingCart;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.repository.book.BookRepository;
import com.lukian.bookstore.repository.cart.CartItemRepository;
import com.lukian.bookstore.repository.cart.ShoppingCartRepository;
import com.lukian.bookstore.repository.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository itemRepository;
    @Qualifier("cartItemMapper")
    private final CartItemMapper itemMapper;
    @Qualifier("shoppingCartMapper")
    private final ShoppingCartMapper cartMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * Fetching existing user's cart from DB based on passed ID, then assigning to
     * requested item that it belongs to the current cart
     * (This way it will bind them together based on mapping in entities).
     * After that item is saved and resides in needed cart.
     *
     * @param userId         ID of user whose cart it is
     * @param requestDto DTO of requested item to add to the cart
     * @return DTO of updated cart (after desired item is added)
     */
    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDB = getCartFromDB(userId);

        CartItem requestedItemToAdd = itemMapper.toModel(requestDto);

        Book bookFromDb = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book by given id"));

        requestedItemToAdd.setBook(bookFromDb);

        requestedItemToAdd.setQuantity(requestDto.quantity());

        requestedItemToAdd.setShoppingCart(cartFromDB);

        itemRepository.save(requestedItemToAdd);

        return cartMapper.toDto(cartFromDB);
    }

    @Override
    public ShoppingCartDto getCartByUserId(Long id) {
        return cartMapper.toDto(getCartFromDB(id));
    }

    /**
     * Updating existing item in user's cart
     *
     * Retrieves item by its ID, then using mapper updates model based on
     * passed request DTO. After that retrieves cart, based on user ID to showcase
     * changes in items.
     *
     * Note: we do not need to fetch at first, since each
     *
     * @param userId       ID of the user for whom the cart item is being updated.
     * @param cartItemId   ID of the cart item to be updated.
     * @param requestDto   DTO containing the updated information for the cart item.
     * @return             DTO representation of the updated shopping cart after the item update.
     * @throws RuntimeException if the cart item or user's cart cannot be found in the database.
     */
    @Override
    public ShoppingCartDto update(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);

        itemMapper.updateFromDto(requestDto, itemFromDb);

        ShoppingCart cartFromDb = getCartFromDB(userId);

        return cartMapper.toDto(cartFromDb);
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        itemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getCartFromDB(userId));
    }

    private CartItem getItemFromDb(Long cartItemId) {
        return itemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find item by item id: " + cartItemId));
    }

    private ShoppingCart getCartFromDB(Long userId) {
        Optional<ShoppingCart> cartFromDb = cartRepository.findByUserId(userId);
        if (cartFromDb.isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            User userFromDb = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found by user id: " + userId));
            shoppingCart.setUser(userFromDb);
            return cartRepository.save(shoppingCart);
        }
        return cartFromDb.get();
    }
}
