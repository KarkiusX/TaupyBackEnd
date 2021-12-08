package com.example.Taupyk.lt.Controllers;

import com.example.Taupyk.lt.DTO.MarketDto;
import com.example.Taupyk.lt.Models.Comment;
import com.example.Taupyk.lt.Models.Market;
import com.example.Taupyk.lt.Models.Product;
import com.example.Taupyk.lt.Repositories.CommentRepository;
import com.example.Taupyk.lt.Repositories.MarketRepository;
import com.example.Taupyk.lt.Repositories.ProductRepositry;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "https://taupyk-cia.herokuapp.com/", allowCredentials = "true")
@Validated
public class MarketController {

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    ProductRepositry productRepositry;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping(path = "/market")
    public ResponseEntity getMarkets()
    {
        return ResponseEntity.status(HttpStatus.OK).body(marketRepository.findAll());
    }
    @GetMapping(path = "/market/{marketId}/product/{productId}")
    public ResponseEntity getProductByMarket(@PathVariable long marketId, @PathVariable long productId)
    {
        Optional<Product> product = productRepositry.findById(productId);

        if(product.isPresent())
        {
            if(product.get().getMarket().getUId() == marketId)
            {
                return new ResponseEntity<>(product.get(), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(product.get(), HttpStatus.NOT_FOUND);
            }
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/market/")
    public ResponseEntity createMarket(@Valid @RequestBody MarketDto market)
    {
        if(market == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Market marketR = marketRepository.save(new Market(market.getName(), market.getIconName()));
        return ResponseEntity.status(HttpStatus.CREATED).body(market);
    }
    @GetMapping(path = "/market/{id}")
    public ResponseEntity getMarket(@PathVariable long id)
    {
        Optional<Market> market = marketRepository.findById(id);

        if(market.isPresent())
            return ResponseEntity.ok(market);
        else
            return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/market/{id}")
    public ResponseEntity<Market> deleteMarket(@PathVariable long id)
    {
        Optional<Market> market = marketRepository.findById(id);
        if(market.isPresent())
        {
            try{
                marketRepository.deleteById(id);
            }
            catch (Exception exception)
            {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.ok(market.get());
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
   // @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PutMapping(path = "/market/{id}")
    public ResponseEntity<Market> updateMarket(@PathVariable long id, @RequestBody Market market)
    {
        if(market == null)
            return ResponseEntity.unprocessableEntity().build();

        Optional<Market> marketExist = marketRepository.findById(id);
        if(!marketExist.isPresent())
            return ResponseEntity.noContent().build();

        marketRepository.updateMarket(market.getName(), market.getIconName(), id);

        return ResponseEntity.ok().build();
    }

}
