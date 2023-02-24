
INSERT INTO entreprise (id,adresse1,adresse2,codepostale,pays,ville,codefiscal,description,email,nom,numtel,photo,siteweb,creation_date,last_modified_date)
VALUES
    (3,'adresse1','adresse2','12345','SENEGAL','Dakar', '54321','bara tall company','email@gmail.com','talix mines','numtel','photo','url.com','2023-02-01 17:39:48', '2023-02-01 17:39:48');

INSERT INTO utilisateur (id,creation_date,last_modified_date,adresse1,adresse2,codepostale,pays,ville,datedenaissance,email,motdepasse,nom,photo,prenom,identreprise)
VALUES
    (105,'2023-02-01 17:39:48', '2023-02-01 17:39:48','adresse1','adresse2','12345','SENEGAL','Dakar', '2000-02-01 17:39:48','user@email.com','password','talix mines','photo','talix mines',3),
    (106,'2023-02-01 17:39:48', '2023-02-01 17:39:48','adresse1','adresse2','12345','SENEGAL','Dakar', '2000-02-01 17:39:48','alice@email.com','password','talix mines','photo','talix mines',3),
    (107,'2023-02-01 17:39:48', '2023-02-01 17:39:48','adresse1','adresse2','12345','SENEGAL','Dakar', '2000-02-01 17:39:48','doe@gmail.com','password','talix mines','photo','talix mines',3),
    (108,'2023-02-01 17:39:48', '2023-02-01 17:39:48','adresse1','adresse2','12345','SENEGAL','Dakar', '2000-02-01 17:39:48','john@email.com','password','talix mines','photo','talix mines',3);

INSERT INTO roles (id,creation_date,last_modified_date,rolename,idutilisateur)
VALUES
    (110,'2023-02-01 17:39:48', '2023-02-01 17:39:48','ROOT_ADMIN', 105),
    (111,'2023-02-01 17:39:48', '2023-02-01 17:39:48','SUPER_ADMIN', 105),
    (112,'2023-02-01 17:39:48', '2023-02-01 17:39:48','SUPER_ADMIN', 105),
    (113,'2023-02-01 17:39:48', '2023-02-01 17:39:48','SUPER_STATS', 106);

INSERT INTO category (id,   code,  designation,creation_date,last_modified_date,identreprise)
VALUES
    (100, 'CAT-100','CATEGORIE-100', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3),
    (101, 'CAT-101','CATEGORIE-101', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3),
    (102, 'CAT-102','CATEGORIE-102', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3),
    (103, 'CAT-103','CATEGORIE-103', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3),
    (104, 'CAT-104','CATEGORIE-104', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3),
    (105, 'CAT-105','CATEGORIE-105', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 3);

INSERT INTO article (id, creation_date,last_modified_date,codearticle, designation,photo,prixunitaireht,prixunitairettc,tauxtva,idcategory,identreprise)
VALUES
    (100,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-100','Designation article','photo',5, 10, 0.18, 101, 3),
    (101,'2023-02-06 17:39:48', '2023-02-06 17:39:48', 'ART-101','ART101','photo',2, 5, 0.18, 101, 3),
    (102,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-102','Designation article','photo',10, 20, 0.18, 101, 3),
    (103,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-103','Designation article','photo',5, 10, 0.18, 101, 3),
    (104,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-104','Designation article','photo',2, 5, 0.18, 101, 3),
    (105,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-105','Designation article','photo',10, 20, 0.18, 103, 3),
    (106,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-106','Designation article','photo',10, 25, 0.18, 103, 3),
    (107,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-107','Designation article','photo',1, 10, 0.18, 103, 3),
    (108,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-108','Designation article','photo',5, 10, 0.18, 103, 3),
    (109,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ART-109','Designation article','photo',4, 10, 0.18, 103, 3);

INSERT INTO ventes (id, creation_date,last_modified_date,code, commentaire,datevente,identreprise)
VALUES
    (100,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'VENTE-100','vente de moellons','2023-02-01 17:39:48', 3),
    (101,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'VENTE-101','vente de basalte','2023-02-01 17:39:48', 3),
    (102,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'VENTE-102','vente de moellons','2023-02-01 17:39:48', 3);

INSERT INTO lignevente (id, creation_date,last_modified_date,prixunitaire,quantite,idarticle,idvente,identreprise)
VALUES
    (100,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 5, 10, 100, 100, 3),
    (101,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 3, 5, 100, 100, 3),
    (102,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 2, 20, 101, 101, 3);


INSERT INTO client (id, creation_date,last_modified_date,nom,prenom,adresse1,adresse2,codepostale,pays,ville,photo,mail,num_tel,identreprise)
VALUES
    (200,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ka', 'salif','adresse1','adresse2','12345','SENEGAL','Dakar','photo','salif@mail.con', 'numtel', 3),
    (201,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'zimba', 'moussa','adresse1','adresse2','12345','SENEGAL','Dakar','photo','zimba@mail.con', 'numtel', 3),
    (202,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'gning', 'ousmane','adresse1','adresse2','12345','SENEGAL','Dakar','photo','gning@mail.con', 'numtel', 3),
    (203,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'ndiaye', 'souleye','adresse1','adresse2','12345','SENEGAL','Dakar','photo','ndiaye@mail.con', 'numtel', 3),
    (204,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'gaye', 'idriss','adresse1','adresse2','12345','SENEGAL','Dakar','photo','gaye@mail.con', 'numtel', 3);

INSERT INTO commandeclient (id, creation_date,last_modified_date,code,datecommande,etatcommande,idclient,identreprise)
VALUES
    (50,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'COMM-50', '2023-02-01 17:39:48', 'EN_PREPARATION', 200, 3),
    (51,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'COMM-51', '2023-02-02 17:39:48', 'EN_PREPARATION', 200, 3),
    (52,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'COMM-52', '2023-02-03 17:39:48', 'VALIDEE', 200, 3),
    (53,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 'COMM-53', '2023-02-04 17:39:48', 'LIVREE', 200, 3);


INSERT INTO lignecommandeclient (id, creation_date,last_modified_date,prixunitaire,quantite,idarticle, idcommandeclient,identreprise)
VALUES
    (30,'2023-02-01 17:39:48', '2023-02-01 17:39:48', 5, 10, 102, 50, 3),
    (31,'2023-02-01 17:39:48', '2023-02-02 17:39:48', 5, 5, 100, 52, 3),
    (32,'2023-02-01 17:39:48', '2023-02-03 17:39:48', 10, 20, 105, 53, 3),
    (33,'2023-02-01 17:39:48', '2023-02-03 17:39:48', 5, 5, 105, 51, 3);


INSERT INTO mvtstk (id, creation_date,last_modified_date,dateMvt,quantite,typemvt,idarticle, sourcemvt,identreprise)
VALUES
    (10,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 20, 'ENTREE', 100, 'COMMANDE_CLIENT', 3),
    (11,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 10, 'ENTREE', 101, 'COMMANDE_CLIENT', 3),
    (12,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 15, 'ENTREE', 102, 'COMMANDE_CLIENT', 3),
    (13,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 50, 'ENTREE', 103, 'COMMANDE_CLIENT', 3),
    (14,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 20, 'ENTREE', 104, 'COMMANDE_CLIENT', 3),
    (15,'2023-02-01 17:39:48', '2023-02-01 17:39:48', '2023-02-01 17:39:48', 30, 'ENTREE', 105, 'COMMANDE_CLIENT', 3);


