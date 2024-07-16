package mate.academy.intro.service.shopping.cart;

import mate.academy.intro.dto.shoppingсart.CartItemDto;
import mate.academy.intro.dto.shoppingсart.CreateCartItemRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CartItemService {
    List<CartItemDto> findAll(Pageable pageable);

    CartItemDto getById(Long id);

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto update(Long id, CreateCartItemRequestDto categoryDto);

    void deleteById(Long id);
}
