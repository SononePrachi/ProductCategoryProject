package com.example.ProductCategory.Entity;





import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role;

    //Mapping is done by product
    @OneToMany(mappedBy = "user")
    private List<Product> products;

}
