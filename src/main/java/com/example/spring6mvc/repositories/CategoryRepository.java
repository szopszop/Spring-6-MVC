package com.example.spring6mvc.repositories;

import com.example.spring6mvc.enteties.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {


}
