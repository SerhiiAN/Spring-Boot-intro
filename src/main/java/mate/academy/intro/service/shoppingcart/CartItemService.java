package mate.academy.intro.service.shoppingcart;

import java.util.List;
import mate.academy.intro.dto.shoppingcart.CartItemDto;
import mate.academy.intro.dto.shoppingcart.CreateCartItemRequestDto;
import mate.academy.intro.dto.shoppingcart.UpdateCartItemRequestDto;
import org.springframework.data.domain.Pageable;

public interface CartItemService {
    List<CartItemDto> findAll(Pageable pageable);

    CartItemDto getById(Long id);

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto update(Long id, UpdateCartItemRequestDto categoryDto);

    void deleteById(Long id);
}
