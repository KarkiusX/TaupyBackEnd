package com.example.Taupyk.lt.Controllers;

import com.example.Taupyk.lt.DTO.CommentDTO;
import com.example.Taupyk.lt.DTO.UserDto;
import com.example.Taupyk.lt.Models.Comment;
import com.example.Taupyk.lt.Models.Market;
import com.example.Taupyk.lt.Models.CustomUser;
import com.example.Taupyk.lt.Models.Product;
import com.example.Taupyk.lt.Repositories.CommentRepository;
import com.example.Taupyk.lt.Repositories.CustomUserRepository;
import com.example.Taupyk.lt.Repositories.MarketRepository;
import com.example.Taupyk.lt.Repositories.ProductRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
@CrossOrigin
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CustomUserRepository userRepository;

    @Autowired
    ProductRepositry productRepositry;

    @GetMapping(path = "/comment/")
    public ResponseEntity getComments()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO> commentDTOS = comments.stream().map(comment ->
        {
            String name = userRepository.findById(comment.getUser().getUId()).get().getUsername();
            LocalDate localDate = new Date(comment.getTimeStamp() * 1000).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return new CommentDTO(comment.getText(),new UserDto(name), localDate);
        }).toList();
        return ResponseEntity.status(HttpStatus.OK).body(commentDTOS);
    }
    @GetMapping(path = "/comment/{userId}")
    public ResponseEntity<List<Comment>> getComment(@PathVariable long userId)
    {
        List<Comment> comments = commentRepository.findAll().stream().filter(p -> p.getUser().getUId() == userId).collect(Collectors.toList());
        if(comments.isEmpty())
            return ResponseEntity.noContent().build();

        return new ResponseEntity<List<Comment>>(comments, HttpStatus.FORBIDDEN);
    }
    @DeleteMapping(path = "/comment/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long commentId)
    {
        Optional<Comment> comment= commentRepository.findById(commentId);

        if(comment.isPresent())
        {
            commentRepository.deleteById(commentId);
            return new ResponseEntity<>(comment.get(), HttpStatus.OK);
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
    @Transactional
    @PutMapping(path = "/comment/{id}")
    public ResponseEntity updateComment(@RequestBody Comment comment, @PathVariable long id)
    {
        Optional<Comment> commentExist = commentRepository.findById(id);
        if(comment != null && commentExist.isPresent())
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUser customUser = userRepository.getById(commentExist.get().getUser().getUId());
            customUser.loadUser();
            if(authentication.getName() != customUser.getUsername())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Neleidžiama");

            commentRepository.updateProduct(comment.getText(), id, comment.getTimeStampTime());
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(path = "/comment/")
    public ResponseEntity createComment(@RequestBody Comment comment)
    {
        Optional<CustomUser> user = userRepository.findById(comment.getUser().getUId());
        if(!user.isPresent())
            return ResponseEntity.noContent().build();

        Optional<Product> product = productRepositry.findById(comment.getProduct().getUId());
        if(!product.isPresent())
            return ResponseEntity.noContent().build();


        comment.setTimeStamp(comment.getTimeStampTime());
        comment.setUser(user.get());
        comment.setProduct(product.get());
        Comment marketR = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
