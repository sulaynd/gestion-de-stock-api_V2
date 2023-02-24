package com.talixmines.management.utils;

public interface Constants {

  String APP_ROOT = "talix-management/v1";
  String APP_USER_MANAGER_ROOT = "user-manager/v1";
  String APP_FINANCIAL_MANAGER_ROOT = "financial-manager/v1";
  String APP_PRODUCTION_MANAGER_ROOT = "production-manager/v1";
  String APP_ACTIVITY_MANAGER_ROOT = "activity-manager/v1";
  String APP_REPORT_MANAGER_ROOT = "report-manager/v1";
  String APP_INVENTORY_MANAGER_ROOT = "inventory-manager/v1";
  String COMMANDE_FOURNISSEUR_ENDPOINT = APP_ROOT + "/commandesfournisseurs";
  String CREATE_COMMANDE_FOURNISSEUR_ENDPOINT = COMMANDE_FOURNISSEUR_ENDPOINT + "/create";
  String FIND_COMMANDE_FOURNISSEUR_BY_ID_ENDPOINT = COMMANDE_FOURNISSEUR_ENDPOINT + "/{idCommandeFournisseur}";
  String FIND_COMMANDE_FOURNISSEUR_BY_CODE_ENDPOINT = COMMANDE_FOURNISSEUR_ENDPOINT + "/filter/{codeCommandeFournisseur}";
  String FIND_ALL_COMMANDE_FOURNISSEUR_ENDPOINT = COMMANDE_FOURNISSEUR_ENDPOINT + "/all";
  String DELETE_COMMANDE_FOURNISSEUR_ENDPOINT = COMMANDE_FOURNISSEUR_ENDPOINT + "/delete/{idCommandeFournisseur}";

  String ENTREPRISE_ENDPOINT = APP_USER_MANAGER_ROOT + "/entreprises";

  String FOURNISSEUR_ENDPOINT = APP_ROOT + "/fournisseurs";

  String UTILISATEUR_ENDPOINT = APP_USER_MANAGER_ROOT + "/users";

  String VENTES_ENDPOINT = APP_ROOT + "/ventes";

  String AUTHENTICATION_ENDPOINT = APP_USER_MANAGER_ROOT + "/users";

  String ACTION_AUTHENTICATION = "User authentication ";
  String TAG_USER_MANAGEMENT = "User manager";
  String ACTION_CREATE_USER = "Create a user";
  String ACTION_UPDATE_USER = "Update a user";
  String ACTION_READ_USER_BY_ID = "Read a user by Id";
  String ACTION_READ_USER_BY_EMAIL = "Read a user by Email";
  String ACTION_READ_USERS = "Read all users";
  String ACTION_DELETE_USER = "Delete a user";
}
