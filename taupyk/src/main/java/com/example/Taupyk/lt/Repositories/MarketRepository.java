package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarketRepository extends JpaRepository<Market, Long> {

    @Modifying
    @Query("UPDATE Market u set u.name = :name, u.iconName = :iconName where u.UId= :marketId")
    void updateMarket(@Param("name") String name,
                                   @Param("iconName") String iconName,
                                   @Param("marketId") long marketId);
}
