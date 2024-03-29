package ru.practicum.main_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.CategoryDto;
import ru.practicum.main_server.dto.NewCategoryDto;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.RejectedRequestException;
import ru.practicum.main_server.mapper.CategoryMapper;
import ru.practicum.main_server.model.Category;
import ru.practicum.main_server.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(long id) {

        return CategoryMapper
                .toCategoryDto(categoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ObjectNotFoundException("Category not found")));
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        if(categoryDto.getId() == null || categoryDto.getName().isEmpty()){
            throw new RejectedRequestException("category id and name must not be null");}
        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(
                ()-> new ObjectNotFoundException("category not found"));
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow();
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        if(newCategoryDto.getName()==null){
            throw new RejectedRequestException("category name must not be null");
        }
        Category category = CategoryMapper.toCategoryFromNewCategoryDto(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }
}
