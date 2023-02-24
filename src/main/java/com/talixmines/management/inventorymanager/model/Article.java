package com.talixmines.management.inventorymanager.model;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.talixmines.management.utils.AbstractEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "article")
public class Article extends AbstractEntity {

  @Column(name = "codearticle")
  private String codeArticle;

  @Column(name = "designation")
  private String designation;

  @Column(name = "prixunitaireht")
  private BigDecimal prixUnitaireHt;

  @Column(name = "tauxtva")
  private BigDecimal tauxTva;

  @Column(name = "prixunitairettc")
  private BigDecimal prixUnitaireTtc;

  @Column(name = "photo")
  private String photo;

  @Column(name = "identreprise")
  private Integer idEntreprise;

  @ManyToOne
  @JoinColumn(name = "idcategory")
  private Category category;

  @OneToMany(mappedBy = "article")
  @ToString.Exclude
  private List<LigneVente> ligneVentes;

  @OneToMany(mappedBy = "article")
  @ToString.Exclude
  private List<LigneCommandeClient> ligneCommandeClients;

  @OneToMany(mappedBy = "article")
  @ToString.Exclude
  private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;

  @OneToMany(mappedBy = "article")
  @ToString.Exclude
  private List<MvtStk> mvtStks;

}
