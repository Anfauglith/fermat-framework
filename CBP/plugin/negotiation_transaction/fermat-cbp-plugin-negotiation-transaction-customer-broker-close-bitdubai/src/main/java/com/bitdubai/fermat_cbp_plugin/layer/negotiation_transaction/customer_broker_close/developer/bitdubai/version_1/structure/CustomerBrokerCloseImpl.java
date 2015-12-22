package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_close.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransactionStatus;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_close.interfaces.CustomerBrokerClose;

import java.util.UUID;

/**
 * Created by Yordin Alayn on 22.12.15.
 */
public class CustomerBrokerCloseImpl implements CustomerBrokerClose {

    private static final int HASH_PRIME_NUMBER_PRODUCT  = 2017;
    private static final int HASH_PRIME_NUMBER_ADD      = 3203;

    private UUID transactionId;
    private UUID                            negotiationId;
    private String                          publicKeyBroker;
    private String                          publicKeyCustomer;
    private NegotiationTransactionStatus negotiationTransactionStatus;
    private String                          negotiationXML;
    private long timestamp;

    public CustomerBrokerCloseImpl(
            UUID                            transactionId,
            UUID                            negotiationId,
            String                          publicKeyBroker,
            String                          publicKeyCustomer,
            NegotiationTransactionStatus    negotiationTransactionStatus,
            String                          negotiationXML,
            long                            timestamp
    ){
        this.transactionId                  = transactionId;
        this.negotiationId                  = negotiationId;
        this.publicKeyBroker                = publicKeyBroker;
        this.publicKeyCustomer              = publicKeyCustomer;
        this.negotiationTransactionStatus   = negotiationTransactionStatus;
        this.negotiationXML                 = negotiationXML;
        this.timestamp                      = timestamp;
    }

    public UUID getTransactionId(){ return this.transactionId; }

    public UUID getNegotiationId(){ return this.negotiationId; }

    public String getPublicKeyBroker(){ return this.publicKeyBroker; }

    public String getPublicKeyCustomer(){ return this.publicKeyCustomer; }

    public NegotiationTransactionStatus getStatusTransaction(){ return this.negotiationTransactionStatus; }

    public String getNegotiationXML(){ return this.negotiationXML; }

    public long getTimestamp(){ return this.timestamp; }

    public boolean equals(Object o){
        if(!(o instanceof CustomerBrokerCloseImpl))
            return false;
        CustomerBrokerCloseImpl compare = (CustomerBrokerCloseImpl) o;
        return publicKeyBroker.equals(compare.getPublicKeyBroker()) && publicKeyCustomer.equals(compare.getPublicKeyCustomer());
    }

    @Override
    public int hashCode(){
        int c = 0;
        c += publicKeyBroker.hashCode();
        c += publicKeyCustomer.hashCode();
        return 	HASH_PRIME_NUMBER_PRODUCT * HASH_PRIME_NUMBER_ADD + c;
    }
}
