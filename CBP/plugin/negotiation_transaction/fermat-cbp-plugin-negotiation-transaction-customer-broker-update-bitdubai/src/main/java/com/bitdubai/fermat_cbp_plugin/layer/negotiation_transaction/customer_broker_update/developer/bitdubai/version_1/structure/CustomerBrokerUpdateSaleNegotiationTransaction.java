package com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationTransactionStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationType;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.exceptions.CantUpdateCustomerBrokerSaleException;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiation;
import com.bitdubai.fermat_cbp_api.layer.negotiation.customer_broker_sale.interfaces.CustomerBrokerSaleNegotiationManager;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.NegotiationTransactionCustomerBrokerUpdatePluginRoot;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.database.CustomerBrokerUpdateNegotiationTransactionDatabaseDao;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.exceptions.CantCancelSaleNegotiationTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.exceptions.CantRegisterCustomerBrokerUpdateNegotiationTransactionException;
import com.bitdubai.fermat_cbp_plugin.layer.negotiation_transaction.customer_broker_update.developer.bitdubai.version_1.exceptions.CantUpdateSaleNegotiationTransactionException;

import java.util.UUID;

/**
 * Created by Yordin Alayn on 16.12.15.
 */
public class CustomerBrokerUpdateSaleNegotiationTransaction {

    /*Represent the Negotiation Sale*/
    private CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager;

    /*Represent the Transaction database DAO */
    private CustomerBrokerUpdateNegotiationTransactionDatabaseDao customerBrokerUpdateNegotiationTransactionDatabaseDao;

    /*Represent the NegotiationTransactionCustomerBrokerNewPluginRoot*/
    private NegotiationTransactionCustomerBrokerUpdatePluginRoot pluginRoot;

    public CustomerBrokerUpdateSaleNegotiationTransaction(
            CustomerBrokerSaleNegotiationManager customerBrokerSaleNegotiationManager,
            CustomerBrokerUpdateNegotiationTransactionDatabaseDao customerBrokerUpdateNegotiationTransactionDatabaseDao,
            NegotiationTransactionCustomerBrokerUpdatePluginRoot pluginRoot
    ) {
        this.customerBrokerSaleNegotiationManager = customerBrokerSaleNegotiationManager;
        this.customerBrokerUpdateNegotiationTransactionDatabaseDao = customerBrokerUpdateNegotiationTransactionDatabaseDao;
        this.pluginRoot = pluginRoot;
    }

    //SEND PROCESS THE UPDATE SALE NEGOTIATION TRANSACTION
    public void sendSaleNegotiationTranasction(CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation) throws CantUpdateSaleNegotiationTransactionException {

        try {

            UUID transactionId = UUID.randomUUID();

            System.out.print(new StringBuilder()
                            .append("\n\n**** 3) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - SALE NEGOTIATION - SEND CUSTOMER BROKER UPDATE SALE NEGOTIATION TRANSACTION. transactionId: ")
                            .append(transactionId).append(" ****")
                            .append("\n --- Negotiation Mock XML Date")
                            .append("\n- NegotiationId = ").append(customerBrokerSaleNegotiation.getNegotiationId())
                            .append("\n- CustomerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- BrokerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- Status = ").append(customerBrokerSaleNegotiation.getStatus()).toString()
            );

            //UPDATE NEGOTIATION
            this.customerBrokerSaleNegotiationManager.updateCustomerBrokerSaleNegotiation(customerBrokerSaleNegotiation);

            //CREATE NEGOTIATION TRANSATION
            this.customerBrokerUpdateNegotiationTransactionDatabaseDao.createCustomerBrokerUpdateNegotiationTransaction(
                    transactionId,
                    customerBrokerSaleNegotiation,
                    NegotiationType.SALE,
                    NegotiationTransactionStatus.PENDING_SUBMIT
            );

        } catch (CantUpdateCustomerBrokerSaleException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), e, CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        } catch (CantRegisterCustomerBrokerUpdateNegotiationTransactionException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), e, CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR REGISTER CUSTOMER BROKER SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR PROCESS CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        }
    }

    //RECEIVE PROCESS THE UPDATE SALE NEGOTIATION TRANSACTION
    public void receiveSaleNegotiationTranasction(UUID transactionId, CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation) throws CantUpdateSaleNegotiationTransactionException {

        try {

            System.out.print(new StringBuilder().append("\n\n**** 21) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER UPDATE - SALE NEGOTIATION - RECEIVE CUSTOMER BROKER UPDATE SALE NEGOTIATION TRANSACTION. transactionId: ").append(transactionId).append(" ****\n").toString());

            System.out.print(new StringBuilder()
                            .append("\n\n --- Negotiation Mock XML Date")
                            .append("\n- NegotiationId = ").append(customerBrokerSaleNegotiation.getNegotiationId())
                            .append("\n- CustomerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- BrokerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- Status = ").append(customerBrokerSaleNegotiation.getStatus()).toString()
            );

            //CREATE NEGOTIATION
            this.customerBrokerSaleNegotiationManager.updateCustomerBrokerSaleNegotiation(customerBrokerSaleNegotiation);
            this.customerBrokerSaleNegotiationManager.waitForBroker(customerBrokerSaleNegotiation);

            //CREATE NEGOTIATION TRANSATION
            this.customerBrokerUpdateNegotiationTransactionDatabaseDao.createCustomerBrokerUpdateNegotiationTransaction(
                    transactionId,
                    customerBrokerSaleNegotiation,
                    NegotiationType.SALE,
                    NegotiationTransactionStatus.PENDING_SUBMIT_CONFIRM
            );

        } catch (CantUpdateCustomerBrokerSaleException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), e, CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CREATE CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        } catch (CantRegisterCustomerBrokerUpdateNegotiationTransactionException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), e, CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR REGISTER CUSTOMER BROKER SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantUpdateSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantUpdateSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR PROCESS CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        }
    }

    //SEND PROCESS THE CANCEL SALE NEGOTIATION TRANSACTION
    public void sendCancelSaleNegotiationTranasction(CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation) throws CantCancelSaleNegotiationTransactionException {

        try {

            UUID transactionId = UUID.randomUUID();

            System.out.print(new StringBuilder().append("\n\n**** 3) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER CANCEL - SALE NEGOTIATION - SEND CUSTOMER BROKER CANCEL SALE NEGOTIATION TRANSACTION. transactionId: ").append(transactionId).append(" ****\n").toString());

            System.out.print(new StringBuilder()
                            .append("\n\n --- Negotiation Mock XML Date")
                            .append("\n- NegotiationId = ").append(customerBrokerSaleNegotiation.getNegotiationId())
                            .append("\n- CustomerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- BrokerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- Status = ").append(customerBrokerSaleNegotiation.getStatus()).toString()
            );

            //CANCEL NEGOTIATION
            this.customerBrokerSaleNegotiationManager.cancelNegotiation(customerBrokerSaleNegotiation);

            //CREATE NEGOTIATION TRANSATION
            this.customerBrokerUpdateNegotiationTransactionDatabaseDao.createCustomerBrokerUpdateNegotiationTransaction(
                    transactionId,
                    customerBrokerSaleNegotiation,
                    NegotiationType.SALE,
                    NegotiationTransactionStatus.PENDING_SUBMIT
            );

        } catch (CantUpdateCustomerBrokerSaleException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), e, CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CANCEL CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        } catch (CantRegisterCustomerBrokerUpdateNegotiationTransactionException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), e, CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR REGISTER CUSTOMER BROKER CANCEL SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR PROCESS CUSTOMER BROKER CANCEL SALE NEGOTIATION, UNKNOWN FAILURE.");
        }
    }

    //SEND PROCESS THE CANCEL SALE NEGOTIATION TRANSACTION
    public void receiveCancelSaleNegotiationTranasction(UUID transactionId, CustomerBrokerSaleNegotiation customerBrokerSaleNegotiation) throws CantCancelSaleNegotiationTransactionException {

        try {

            System.out.print(new StringBuilder().append("\n\n**** 21) MOCK NEGOTIATION TRANSACTION - CUSTOMER BROKER CANCEL - SALE NEGOTIATION - RECEIVE CUSTOMER BROKER CANCEL SALE NEGOTIATION TRANSACTION. transactionId: ").append(transactionId).append(" ****\n").toString());

            System.out.print(new StringBuilder()
                            .append("\n\n --- Negotiation Mock XML Date")
                            .append("\n- NegotiationId = ").append(customerBrokerSaleNegotiation.getNegotiationId())
                            .append("\n- CustomerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- BrokerPublicKey = ").append(customerBrokerSaleNegotiation.getCustomerPublicKey())
                            .append("\n- Status = ").append(customerBrokerSaleNegotiation.getStatus()).toString()
            );

            //CANCEL NEGOTIATION
            this.customerBrokerSaleNegotiationManager.cancelNegotiation(customerBrokerSaleNegotiation);

            //CREATE NEGOTIATION TRANSATION
            this.customerBrokerUpdateNegotiationTransactionDatabaseDao.createCustomerBrokerUpdateNegotiationTransaction(
                    transactionId,
                    customerBrokerSaleNegotiation,
                    NegotiationType.SALE,
                    NegotiationTransactionStatus.PENDING_SUBMIT_CONFIRM
            );

        } catch (CantUpdateCustomerBrokerSaleException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), e, CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR CANCEL CUSTOMER BROKER SALE NEGOTIATION, UNKNOWN FAILURE.");
        } catch (CantRegisterCustomerBrokerUpdateNegotiationTransactionException e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), e, CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR REGISTER CUSTOMER BROKER CANCEL SALE NEGOTIATION TRANSACTION, UNKNOWN FAILURE.");
        } catch (Exception e) {
            pluginRoot.reportError(UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            throw new CantCancelSaleNegotiationTransactionException(e.getMessage(), FermatException.wrapException(e), CantCancelSaleNegotiationTransactionException.DEFAULT_MESSAGE, "ERROR PROCESS CUSTOMER BROKER CANCEL SALE NEGOTIATION, UNKNOWN FAILURE.");
        }
    }
}
