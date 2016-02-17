package com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantCreateNewCustomerIdentityWalletRelationshipException;
import com.bitdubai.fermat_cbp_api.layer.actor.crypto_customer.exceptions.CantGetListCustomerIdentityWalletRelationshipException;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by Angel 17-11-15
 */

public interface CryptoCustomerActorManager extends FermatManager {

    /**
     *
     * @param identity
     * @param wallet
     * @return
     * @throws CantCreateNewCustomerIdentityWalletRelationshipException
     */
    CustomerIdentityWalletRelationship createNewCustomerIdentityWalletRelationship(ActorIdentity identity, String wallet) throws CantCreateNewCustomerIdentityWalletRelationshipException;

    /**
     *
     * @return
     * @throws CantGetListCustomerIdentityWalletRelationshipException
     */
    Collection<CustomerIdentityWalletRelationship> getAllCustomerIdentityWalletRelationship() throws CantGetListCustomerIdentityWalletRelationshipException;

    /**
     *
     * @param publicKey
     * @return
     * @throws CantGetListCustomerIdentityWalletRelationshipException
     */
    CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByIdentity(String publicKey) throws CantGetListCustomerIdentityWalletRelationshipException;

    /**
     *
     * @param wallet
     * @return
     * @throws CantGetListCustomerIdentityWalletRelationshipException
     */
    CustomerIdentityWalletRelationship getCustomerIdentityWalletRelationshipByWallet(String wallet) throws CantGetListCustomerIdentityWalletRelationshipException;

}
