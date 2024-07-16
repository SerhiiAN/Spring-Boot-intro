package mate.academy.intro.service.shopping.cart;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.shoppingсart.CartItemDto;
import mate.academy.intro.dto.shoppingсart.CreateCartItemRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.repository.shopping.cart.CartItemRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public List<CartItemDto> findAll(Pageable pageable) {
        return cartItemRepository.findAll(pageable).stream()
                .map(cartItemMapper::toCartItemDto)
                .toList();
    }

    @Override
    public CartItemDto getById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find CartItem with id: " + id));
        return null;
    }

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        return cartItemMapper.toCartItemDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto update(Long id, CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot find CartItem with id: " + id));
        cartItemMapper.updateModel(requestDto, cartItem);
        return cartItemMapper.toCartItemDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
