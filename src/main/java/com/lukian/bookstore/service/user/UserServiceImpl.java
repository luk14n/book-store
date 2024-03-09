package com.lukian.bookstore.service.user;

import com.lukian.bookstore.dto.user.UserRegisterRequestDto;
import com.lukian.bookstore.dto.user.UserRegisterResponseDto;
import com.lukian.bookstore.exception.EntityNotFoundException;
import com.lukian.bookstore.exception.RegistrationException;
import com.lukian.bookstore.mapper.UserMapper;
import com.lukian.bookstore.model.Role;
import com.lukian.bookstore.model.ShoppingCart;
import com.lukian.bookstore.model.User;
import com.lukian.bookstore.repository.cart.ShoppingCartRepository;
import com.lukian.bookstore.repository.role.RoleRepository;
import com.lukian.bookstore.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ShoppingCartRepository cartRepository;
    private final RoleRepository roleRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto)
            throws RegistrationException {
        if ((userRepository.findByEmail(requestDto.email()).isEmpty())) {

            User user = userMapper.toModel(requestDto);

            assignShoppingCartToNewUser(user);

            assignDefaultUserRoleToNewUser(user);

            return userMapper.toDto(userRepository.save(user));
        }
        throw new RegistrationException(
                "User with email: " + requestDto.email() + " already exist");
    }

    private void assignDefaultUserRoleToNewUser(User user) {
        Role roleFromDb = roleRepository.findRoleByRole(Role.RoleName.ROLE_USER)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Cannot find role by given name"));

        Set<Role> userRoles = user.getRoles();
        if (userRoles == null) {
            userRoles = new HashSet<>();
        }
        userRoles.add(roleFromDb);
        user.setRoles(userRoles);
    }

    private void assignShoppingCartToNewUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        cartRepository.save(shoppingCart);
    }
}
