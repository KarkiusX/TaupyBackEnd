package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.CustomUser;
import com.example.Taupyk.lt.Models.IP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPRepository extends JpaRepository<IP, Long> {
    @Modifying
    @Query("UPDATE IP u set u.viewCount = :views where u.IP = :ip")
    void updateMarket(@Param("views") int views,
                      @Param("ip") String ip);

}
