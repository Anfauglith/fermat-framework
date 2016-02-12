package com.bitdubai.fermat_cbp_plugin.layer.sub_app_module.crypto_customer_community.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_customer.utils.CryptoCustomerActorConnection;
import com.bitdubai.fermat_cbp_api.layer.actor_network_service.crypto_customer.utils.CryptoCustomerExposingData;
import com.bitdubai.fermat_cbp_api.layer.sub_app_module.crypto_customer_community.interfaces.CryptoCustomerCommunityInformation;

import java.util.List;

/**
 * Created by Alejandro Bicelis on 2/2/2016.
 */

public class CryptoCustomerCommunitySubAppModuleInformation implements CryptoCustomerCommunityInformation {

    private final String publicKey;
    private final String alias    ;
    private final byte[] image    ;

    public CryptoCustomerCommunitySubAppModuleInformation(final String publicKey,
                                                          final String alias,
                                                          final byte[] image) {

        this.publicKey = publicKey;
        this.alias     = alias    ;
        this.image     = image    ;
    }

    public CryptoCustomerCommunitySubAppModuleInformation(final CryptoCustomerActorConnection actorConnection) {

        this.publicKey = actorConnection.getPublicKey();
        this.alias     = actorConnection.getAlias()    ;
        this.image     = actorConnection.getImage()    ;
    }

    public CryptoCustomerCommunitySubAppModuleInformation(final CryptoCustomerExposingData exposingData) {

        this.publicKey = exposingData.getPublicKey();
        this.alias     = exposingData.getAlias()    ;
        this.image     = exposingData.getImage()    ;
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public byte[] getImage() {
        return image;
    }

    @Override
    public List listCryptoCustomerWallets() {
        return null;
    }
}
