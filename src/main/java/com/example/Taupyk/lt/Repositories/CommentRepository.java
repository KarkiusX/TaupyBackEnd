package com.example.Taupyk.lt.Repositories;

import com.example.Taupyk.lt.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying
    @Query("UPDATE Comment u set u.text = :Text, u.timeStamp = :timeStamp where u.UId = :commentId")
    int updateProduct(@Param("Text") String Text,
                      @Param("commentId") long commentId,
                      @Param("timeStamp") long timeStamp);


}
