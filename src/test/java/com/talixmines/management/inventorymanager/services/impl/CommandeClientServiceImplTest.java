package com.talixmines.management.inventorymanager.services.impl;

import com.talixmines.management.exception.EntityNotFoundException;
import com.talixmines.management.exception.ErrorCodes;
import com.talixmines.management.exception.InvalidOperationException;
import com.talixmines.management.inventorymanager.dto.ArticleDto;
import com.talixmines.management.inventorymanager.dto.CommandeClientDto;
import com.talixmines.management.inventorymanager.dto.LigneCommandeClientDto;
import com.talixmines.management.inventorymanager.model.EtatCommande;
import com.talixmines.management.inventorymanager.services.ClientService;
import com.talixmines.management.inventorymanager.services.CommandeClientService;
import com.talixmines.management.inventorymanager.services.MvtStkService;
import com.talixmines.management.utils.ConstantUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandeClientServiceImplTest {

    @Autowired
    private CommandeClientService commandeClientService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MvtStkService mvtStkService;

    @Test
    public void shouldSaveCommandeClientWithSuccess() {

        List<LigneCommandeClientDto> listLigneCommandeClient = new ArrayList<>();
        var ligneCommandeClient = ConstantUtils.getLigneCommandeClient();
        ligneCommandeClient.getArticle().setId(100);
        listLigneCommandeClient.add(ligneCommandeClient);
        var client = ConstantUtils.getClient();
        client.setId(200);

        var commandeClientDto = ConstantUtils.getCommandeClient();

        commandeClientDto.setClient(client);
        commandeClientDto.setLigneCommandeClients(listLigneCommandeClient);

        var savedCommande = commandeClientService.save(commandeClientDto);

        assertNotNull(savedCommande);
        assertNotNull(savedCommande.getId());
        assertEquals(commandeClientDto.getEtatCommande(), savedCommande.getEtatCommande());
        assertEquals(commandeClientDto.getCode(), savedCommande.getCode());
        assertEquals(commandeClientDto.getClient().getId(), savedCommande.getClient().getId());

        var mvtStock = mvtStkService.stockReelArticle(commandeClientDto.getLigneCommandeClients().get(0).getArticle().getId());

        var stockTotal = new BigDecimal("20.0");
        //assertEquals(mvtStock, new BigDecimal("13.00"));
        assertTrue(stockTotal.compareTo(mvtStock) > 0);

        List<LigneCommandeClientDto> linesCommandeClient = commandeClientService.findAllLignesCommandesClientByCommandeClientId(savedCommande.getId());

        assertNotNull(linesCommandeClient);
        assertNotNull(linesCommandeClient.get(0).getId());
        assertEquals(linesCommandeClient.get(0).getQuantite(), new BigDecimal("2.00"));
    }

    @Test
    public void shoulUpdateEtatCommandeWithValideeState() {
        var commandeClientDto = ConstantUtils.getCommandeClient();
        commandeClientDto.setId(50);

        var commandeClientUpdate = commandeClientService.updateEtatCommande(commandeClientDto.getId(), EtatCommande.VALIDEE);

        assertNotNull(commandeClientUpdate);
        assertEquals(commandeClientUpdate.getEtatCommande(), EtatCommande.VALIDEE);
    }

    @Test
    public void shoulUpdateEtatCommandeWithLivreeState() {
        var commandeClientDto = commandeClientService.findById(52);

        var commandeClientUpdate = commandeClientService.updateEtatCommande(commandeClientDto.getId(), EtatCommande.LIVREE);

        assertNotNull(commandeClientUpdate);
        assertEquals(commandeClientUpdate.getEtatCommande(), EtatCommande.LIVREE);
    }

    @Test
    public void updateEtatCommandeAlreadyLivreeShouldThrowInvalidOperationException() {

        InvalidOperationException expectedException = assertThrows(InvalidOperationException.class, () -> commandeClientService.updateEtatCommande(53, EtatCommande.LIVREE));

        assertEquals(ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE, expectedException.getErrorCode());
        assertEquals("Unable to update order already delivered", expectedException.getMessage());
    }

    @Test
    public void updateEtatCommandeEnPreparationShouldThrowInvalidOperationException() {

        InvalidOperationException expectedException = assertThrows(InvalidOperationException.class, () -> commandeClientService.updateEtatCommande(51, EtatCommande.LIVREE));

        assertEquals(ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE, expectedException.getErrorCode());
        assertEquals("Veuillez valider la commande avant livraison", expectedException.getMessage());
    }

    @Test
    public final void testFindById() throws Exception {

        /* Getting commandeClient */
        CommandeClientDto commandeClientDto = commandeClientService.findById(50);

        assertNotNull(commandeClientDto,"The order should be existing");
        assertEquals(commandeClientDto.getIdEntreprise(),3);
        assertEquals(commandeClientDto.getCode(),"COMM-50");
        assertEquals(commandeClientDto.getClient().getId(),200);
    }
    @Test
    public void findByIdShouldThrowEntityNotFoundException() {
        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> commandeClientService.findById(0));

        assertEquals(ErrorCodes.COMMANDE_CLIENT_NOT_FOUND, expectedException.getErrorCode());

    }


    @Test
    public void testUpdateQuantiteCommande() {
        var commandeClientDto = commandeClientService.updateQuantiteCommande(51, 33, BigDecimal.valueOf(10));

        assertNotNull(commandeClientDto);

        var ligneCommandeClientOptional = commandeClientService.findLigneCommandeClient(33);

        assertNotNull(ligneCommandeClientOptional.get());
        assertEquals(ligneCommandeClientOptional.get().getQuantite(),new BigDecimal("10.00"));
    }


    @Test
    public void testUpdateArticle() {
        var commandeClientDto = commandeClientService.updateArticle(51, 33, 107);

        assertNotNull(commandeClientDto);

        var ligneCommandeClientOptional = commandeClientService.findLigneCommandeClient(33);

        assertNotNull(ligneCommandeClientOptional.get());
        assertEquals(ligneCommandeClientOptional.get().getArticle().getId(),107);
    }

    @Test
    public void testUpdateClient() {
        var commandeClientDto = commandeClientService.updateClient(51, 201);

        assertNotNull(commandeClientDto);
        assertEquals(commandeClientDto.getClient().getId(),201);
        assertEquals(commandeClientDto.getClient().getNom(),"zimba");
        assertEquals(commandeClientDto.getClient().getPrenom(),"moussa");
    }

    @Test
    public final void testReadCustomersOrdersByFilters() throws Exception {

        /* Getting articles*/
        Page<CommandeClientDto> orders = commandeClientService.readOrdersByFilters(null, null,null, null, null, null, null, null, null, null,null, null, PageRequest.of(0, 3));

        assertNotNull(orders);
        assertEquals(orders.getContent().get(0).getId().intValue(), 50);
        assertEquals(orders.getTotalElements(), 4);
        assertEquals(orders.getTotalPages(), 2);
        assertEquals(orders.getNumberOfElements(), 3);

    }

    @Test
    public final void testReadCustomersOrdersByStatus() throws Exception {

        /* Getting articles*/
        Page<CommandeClientDto> orders = commandeClientService.readOrdersByFilters(null, null,null, EtatCommande.LIVREE, null, null, null, null, null, null, null, null, PageRequest.of(0, 3));

        assertNotNull(orders);
        assertEquals(orders.getContent().get(0).getId().intValue(), 53);
        assertEquals(orders.getTotalElements(), 1);
        assertEquals(orders.getTotalPages(), 1);
        assertEquals(orders.getNumberOfElements(), 1);

    }

    @Test
    public final void testReadCustomersOrdersByCodeArticle() throws Exception {

        /* Getting articles*/
        Page<CommandeClientDto> orders = commandeClientService.readOrdersByFilters(null, null,null, null, null, null, null, null, null, null, "ART-105", null, PageRequest.of(0, 3));

        assertNotNull(orders);
        assertEquals(orders.getContent().get(0).getId().intValue(), 51);
        assertEquals(orders.getTotalElements(), 2);
        assertEquals(orders.getTotalPages(), 1);
        assertEquals(orders.getNumberOfElements(), 2);

    }
}
