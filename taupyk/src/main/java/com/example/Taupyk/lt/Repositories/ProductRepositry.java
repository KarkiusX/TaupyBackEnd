package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositry extends JpaRepository<Product, Long> {
    @Modifying
    @Query("UPDATE Product u set u.name = :name, u.price = :price where u.UId = :productId")
    int updateProduct(@Param("name") String name,
                      @Param("price") double price,
                      @Param("productId") long productId);

}
