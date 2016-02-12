package com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateNewActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateNewCustomerIdentityWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListCustomerIdentityWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListPlatformsException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantSendActorNetworkServiceException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantUpdateActorExtraDataException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.ActorExtraData;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.ActorExtraDataManager;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces.CustomerIdentityWalletRelationship;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.exceptions.CantRequestQuotesException;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_broker.interfaces.CryptoBrokerManager;
import com.bitdubai.fermat_cbp_plugin.layer.actor.crypto_customer.developer.bitdubai.version_1.database.CryptoCustomerActorDao;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by angel on 5/1/16.
 */
public class CustomerActorManager implements ActorExtraDataManager {

    private CryptoCustomerActorDao dao;
    private CryptoBrokerManager cryptoBrokerANSManager;

    public CustomerActorManager(CryptoCustomerActorDao dao, CryptoBrokerManager cryptoBrokerANSManager){
        this.dao = dao;
        this.cryptoBrokerANSManager = cryptoBrokerANSManager;
    }

    /*==============================================================================================
    *
    *   Customer Identity Wallet Relationship
    *
    *==============================================================================================*/


        @Override
        public CustomerIdentityWalletRelationship createNewCustomerIdentityWalletRelationship(ActorIdentity identity, String walletPublicKey) throws CantCreateNewCustomerIdentityWalletRelationshipException {
            return this.dao.createNewCustomerIdentityWalletRelationship(identity, walletPublicKey);
        }

        @Override
        public Collection<CustomerIdentityWalletRelationship> getAllCustomerIdentityWalletRelationship() throws CantGetListCustomerIdentityWalletRelationshipException {
            return this.dao.getAllCustomerIdentityWalletRelationship();
        }

        @Override
        public CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByIdentity(String publicKey) throws CantGetListCustomerIdentityWalletRelationshipException {
            return this.dao.getCustomerIdentityWalletRelationshipByIdentity(publicKey);
        }

        @Override
        public CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByWallet(String walletPublicKey) throws CantGetListCustomerIdentityWalletRelationshipException {
            return this.dao.getCustomerIdentityWalletRelationshipByWallet(walletPublicKey);
        }

    /*==============================================================================================
    *
    *   Actor Extra Data
    *
    *==============================================================================================*/

        @Override
        public void createCustomerExtraData(ActorExtraData actorExtraData) throws CantCreateNewActorExtraDataException {
            this.dao.createCustomerExtraData(actorExtraData);
        }

        @Override
        public void updateCustomerExtraData(ActorExtraData actorExtraData) throws CantUpdateActorExtraDataException {
            this.dao.updateCustomerExtraData(actorExtraData);
        }

        @Override
        public Collection<ActorExtraData> getAllActorExtraData() throws CantGetListActorExtraDataException {
            return this.dao.getAllActorExtraData();
        }

        @Override
        public Collection<ActorExtraData> getAllActorExtraDataConnected() throws CantGetListActorExtraDataException {
            return this.dao.getAllActorExtraData();
        }

        @Override
        public ActorExtraData getActorExtraDataByIdentity(ActorIdentity identity) throws CantGetListActorExtraDataException {
            return this.dao.getActorExtraDataByPublicKey(identity.getPublicKey());
        }

        @Override
        public ActorIdentity getActorInformationByPublicKey(String publicKeyBroker) throws CantGetListActorExtraDataException {
            return this.dao.getActorInformationByPublicKey(publicKeyBroker);
        }

        @Override
        public Collection<Platforms> getPlatformsSupport(String CustomerPublicKey, Currency currency) throws CantGetListPlatformsException {
            return null;
        }

        @Override
        public void requestBrokerExtraData(ActorExtraData actorExtraData) throws CantSendActorNetworkServiceException {
            try {
                this.createCustomerExtraData(actorExtraData);
                this.cryptoBrokerANSManager.requestQuotes(actorExtraData.getCustomerPublicKey(), Actors.CBP_CRYPTO_CUSTOMER, actorExtraData.getBrokerIdentity().getPublicKey());
            } catch (CantCreateNewActorExtraDataException e) {
                throw new CantSendActorNetworkServiceException(e.getMessage(), e, "", "");
            } catch (CantRequestQuotesException e) {
                throw new CantSendActorNetworkServiceException(e.getMessage(), e, "", "");
            }
        }
}