package com.example.ondongnae.backend.menu.model;

import com.example.ondongnae.backend.allergy.model.Allergy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
public class MenuAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="allergy_id")
    private Allergy allergy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;

    protected MenuAllergy() {} // JPA

    private MenuAllergy(Menu menu, Allergy allergy) {
        this.menu = menu;
        this.allergy = allergy;
    }

    public static MenuAllergy of(Menu menu, Allergy allergy) {
        return new MenuAllergy(menu, allergy);
    }

}
