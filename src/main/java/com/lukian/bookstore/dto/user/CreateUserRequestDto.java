package com.lukian.bookstore.dto.user;

import com.lukian.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch.FieldMatchList({
        @FieldMatch(first = "password", second = "repeatPassword", message = "Passwords must match")
})
public class CreateUserRequestDto {
    public static final String EMAIL_REGEXP = "^.+@.+\\S+$";
    public static final String PASSWORD_REGEXP =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$";
    @NotBlank
    @Pattern(regexp = EMAIL_REGEXP,
            message = "Invalid email format")
    private String email;
    @NotBlank
    @Pattern(regexp = PASSWORD_REGEXP,
            message = "Password must contain at least one letter,"
                    + " one number, and one special character")
    @Length(min = 8, max = 35)
    private String password;
    @NotBlank
    @Length(min = 8, max = 35)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String shippingAddress;
}
