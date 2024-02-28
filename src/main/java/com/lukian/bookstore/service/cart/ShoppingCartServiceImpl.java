package com.lukian.bookstore.service.cart;

import com.lukian.bookstore.dto.shoppingcart.AddItemToCartRequestDto;
import com.lukian.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.lukian.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.lukian.bookstore.exception.EntityNotFoundException;
import com.lukian.bookstore.mapper.CartItemMapper;
import com.lukian.bookstore.mapper.ShoppingCartMapper;
import com.lukian.bookstore.model.CartItem;
import com.lukian.bookstore.model.ShoppingCart;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.repository.cart.CartItemRepository;
import com.lukian.bookstore.repository.cart.ShoppingCartRepository;
import com.lukian.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final CartItemMapper itemMapper;
    private final ShoppingCartMapper cartMapper;
    private final UserRepository userRepository;

    /**
     * Method fetches cart from DB, maps requested dto to model,
     * sets cart found earlier and saves new item to item repo.
     *
     * Note: during mapping process book ID is being set, using named method
     *
     * @param userId     ID of user whose cart it is
     * @param requestDto DTO of requested item to add to the cart
     * @return DTO of updated cart (after desired item is added)
     */
    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDB = getCartFromDB(userId);
        CartItem requestedItemToAdd = itemMapper.toModel(requestDto);
        requestedItemToAdd.setShoppingCart(cartFromDB);
        itemRepository.save(requestedItemToAdd);
        return cartMapper.toDto(cartFromDB);
    }

    @Override
    public ShoppingCartDto getCartByUserId(Long id) {
        return cartMapper.toDto(getCartFromDB(id));
    }

    /**
     * Method updates existing item in existing cart.
     * It fetches existing item, uses special mapper in order to
     * update specific target based on DTO
     * (in this case target is earlier retrieved item that we want to update,
     * and source which retrieved cart is being updated from is passed DTO)
     *
     * Note: we do not need to fetch cart at first,
     * since all existing items by default have associated cart,
     * therefore we are fetching it in the end when returning response
     *
     * @param userId     ID of the user for whom the cart item is being updated.
     * @param cartItemId ID of the cart item to be updated.
     * @param requestDto DTO containing the updated information for the cart item.
     * @return DTO representation of the updated shopping cart after the item update.
     */
    @Override
    public ShoppingCartDto update(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);
        itemMapper.updateFromDto(requestDto, itemFromDb);
        itemRepository.save(itemFromDb);
        return cartMapper.toDto(getCartFromDB(userId));
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
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "User not found by user id: " + userId));
                    shoppingCart.setUser(userFromDb);
                    return cartRepository.save(shoppingCart);
                });
    }
}
