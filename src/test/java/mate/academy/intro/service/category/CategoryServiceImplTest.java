package mate.academy.intro.service.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.dto.category.CategoryDto;
import mate.academy.intro.dto.category.CreateCategoryRequestDto;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CreateCategoryRequestDto requestDto;
    private Category categoryOne;
    private Category categoryTwo;
    private Long firstLong;
    private Long secondLong;
    private CategoryDto categoryDtoOne;
    private CategoryDto categoryDtoTwo;

    @BeforeEach
    void setUp() {
        firstLong = 1L;
        secondLong = 2L;
        requestDto = new CreateCategoryRequestDto(
                "Fiction","Fiction books");

        categoryOne = new Category();
        categoryOne.setId(firstLong);
        categoryOne.setName(requestDto.name());
        categoryOne.setDescription(requestDto.description());

        categoryTwo = new Category();
        categoryTwo.setId(secondLong);
        categoryTwo.setName("children`s books");
        categoryTwo.setDescription("shildren`s books");

        categoryDtoOne = new CategoryDto(
                categoryOne.getId(),
                categoryOne.getName(),
                categoryOne.getDescription());

        categoryDtoTwo = new CategoryDto(
                categoryTwo.getId(),
                categoryTwo.getName(),
                categoryTwo.getDescription());
    }

    @Test
    @DisplayName("Find all categories in database")
    public void findAll_ValidCategories_ShouldReturnListOfCategory() {
        Pageable pageable = PageRequest.of(0, 30);
        Page<Category> page = new PageImpl<>(List.of(categoryOne, categoryTwo));
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDtoOne, categoryDtoTwo);
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        Assertions.assertEquals(2, categoryDtos.size());
        Assertions.assertEquals(categoryDtoOne, categoryDtos.get(0));
        Assertions.assertEquals(categoryDtoTwo, categoryDtos.get(1));
    }

    @Test
    @DisplayName("Get category by id")
    public void getCategoryById_ValidCategory_ShouldReturnCategoryDto() {
        when(categoryRepository.findById(firstLong)).thenReturn(Optional.of(categoryOne));
        when(categoryMapper.toDto(categoryOne)).thenReturn(categoryDtoOne);
        CategoryDto expected = categoryService.getById(firstLong);
        Assertions.assertEquals(categoryDtoOne, expected);
    }

    @Test
    @DisplayName("Save category in database")
    public void saveCategory_ValidCategory_ShouldSaveCategory() {
        when(categoryRepository.save(categoryOne)).thenReturn(categoryOne);
        when(categoryMapper.toDto(categoryOne)).thenReturn(categoryDtoOne);
        when(categoryMapper.toEntity(categoryDtoOne)).thenReturn(categoryOne);
        CategoryDto expected = categoryService.save(categoryDtoOne);
        Assertions.assertEquals(expected, categoryDtoOne);
    }

    @Test
    @DisplayName("Update category by id")
    public void updateCategoryById_ValidCategory_ShouldReturnCategoryDto() {
        when(categoryRepository.findById(firstLong)).thenReturn(Optional.of(categoryOne));
        when(categoryRepository.save(categoryOne)).thenReturn(categoryOne);
        when(categoryMapper.toDto(categoryOne)).thenReturn(categoryDtoOne);
        CategoryDto actual = categoryService.update(firstLong, requestDto);
        Assertions.assertEquals(categoryDtoOne, actual);
    }

    @Test
    @DisplayName("Delete category by id")
    public void deleteCategoryById_ValidCategory_ShouldSuccess() {
        categoryService.deleteById(firstLong);
        verify(categoryRepository).deleteById(firstLong);
    }
}
