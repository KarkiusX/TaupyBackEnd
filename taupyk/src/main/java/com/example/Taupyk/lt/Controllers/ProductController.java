package com.example.Taupyk.lt.Controllers;

import com.example.Taupyk.lt.DTO.ProductDto;
import com.example.Taupyk.lt.Models.IP;
import com.example.Taupyk.lt.Models.Market;
import com.example.Taupyk.lt.Models.Product;
import com.example.Taupyk.lt.Repositories.CommentRepository;
import com.example.Taupyk.lt.Repositories.IPRepository;
import com.example.Taupyk.lt.Repositories.MarketRepository;
import com.example.Taupyk.lt.Repositories.ProductRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
@CrossOrigin
@Validated
public class ProductController {

    @Autowired
    ProductRepositry productRepositry;

    @Autowired
    MarketRepository marketRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    IPRepository ipRepository;

    @GetMapping(path = "/product/")
    public List<Product> getProducts()
    {
        List<Product> products = productRepositry.findAll();
        return products;
    }
    @Transactional
    @GetMapping(path = "/products/{name}")
    public ResponseEntity getProductBy(@PathVariable String name, HttpServletRequest request)
    {
        Optional<IP> ips = ipRepository.findAll().stream().filter(ip -> ip.getIP().equals(request.getRemoteAddr())).findFirst();
        if(ips.isPresent())
        {
            IP ip = ips.get();
            int viewCount = ip.getViewCount();
            if(ip.getTimeStamp() + 86400 <= ip.getTimeStampTime())
                viewCount = 0;

            if(viewCount > 5 && viewCount < 20)
            {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication instanceof AnonymousAuthenticationToken)
                    return ResponseEntity.status(HttpStatus.OK).body("Pasiektas maksimalus peržiūrų skaičus");

            }
            else if(viewCount <= 5)
            {
                ipRepository.updateMarket(ip.getViewCount() + 1, ip.getIP());
            }
            else{
                return ResponseEntity.noContent().build();
            }
        }
        else
        {

            IP ip = new IP();
            ip.setIP(request.getRemoteAddr().trim());
            ip.setViewCount(1);
            ip.setTimeStamp(ip.getTimeStampTime());

            ipRepository.save(ip);
        }

        List<Product> products = productRepositry.findAll().stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList());
        if(products.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        else
            return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping(path = "/product/{id}")
    public ResponseEntity getProductById(@PathVariable long id)
    {
        Optional<Product> product = productRepositry.findById(id);
        if(product.isPresent())
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        else
            return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/product/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id)
    {
        Optional<Product> productNew = productRepositry.findById(id);

        if(productNew.isPresent())
        {
            commentRepository.findAll().forEach(comment -> {
                if(comment.getProduct().getUId() == id)
                    commentRepository.delete(comment);
            });
            productRepositry.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @PutMapping(path = "/product/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto product, @PathVariable long id)
    {
        Optional<Product> product1 = productRepositry.findById(id);
        if(product1.isPresent())
        {
            productRepositry.updateProduct(product.getName(), product.getPrice(), id);
            product1 = productRepositry.findById(id);
            return new ResponseEntity<Product>(product1.get(), HttpStatus.OK);
        }
        else
        {
            return ResponseEntity.noContent().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/product/")
    public ResponseEntity createProduct(@RequestBody Product product)
    {
        Optional<Market> market = marketRepository.findById(product.getMarket().getUId());
        if(market.isPresent())
        {

            product.setMarket(market.get());
            Product productR = productRepositry.save(product);

            ProductDto productDto = new ProductDto();
            productDto.setName(productR.getName());
            productDto.setPrice(productR.getPrice());

            return new ResponseEntity<>(productDto, HttpStatus.CREATED);
        }
        else{
            return ResponseEntity.noContent().build();
        }
    }
}
