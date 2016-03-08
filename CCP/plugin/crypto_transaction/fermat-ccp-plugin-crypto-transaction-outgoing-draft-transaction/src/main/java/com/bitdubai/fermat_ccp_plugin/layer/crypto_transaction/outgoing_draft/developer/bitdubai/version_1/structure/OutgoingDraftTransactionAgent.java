package com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.Broadcaster;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.bitcoin_vault.CryptoVaultManager;
import com.bitdubai.fermat_bch_api.layer.crypto_vault.classes.transactions.DraftTransaction;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletTransactionRecord;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantCalculateBalanceException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.exceptions.CantRegisterDebitException;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_transmission.interfaces.CryptoTransmissionNetworkServiceManager;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.database.OutgoingDraftTransactionDao;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.exceptions.OutgoingIntraActorCantCancelTransactionException;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.exceptions.OutgoingIntraActorCantGetTransactionsException;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.exceptions.OutgoingIntraActorWalletNotSupportedException;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.util.OutgoingDraftTransactionWrapper;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_ccp_api.layer.platform_service.event_manager.events.OutgoingIntraUserTransactionRollbackNotificationEvent;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mati on 2016.02.15..
 */
public class OutgoingDraftTransactionAgent extends FermatAgent {

    private ErrorManager errorManager;
    private CryptoVaultManager cryptoVaultManager;
    private BitcoinNetworkManager bitcoinNetworkManager;
    private BitcoinWalletManager bitcoinWalletManager;
    private OutgoingDraftTransactionDao outgoingIntraActorDao;
    private CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager;
    private EventManager eventManager;
    private Broadcaster broadcaster;


    private Thread agentThread;
    private TransactionProcessorAgent transactionProcessorAgent;
    //private NetworkExecutorPool executorPool;


    public OutgoingDraftTransactionAgent(final ErrorManager errorManager,
                                                       final CryptoVaultManager cryptoVaultManager,
                                                       final BitcoinNetworkManager bitcoinNetworkManager,
                                                       final BitcoinWalletManager bitcoinWalletManager,
                                                       final OutgoingDraftTransactionDao outgoingIntraActorDao,
                                                       final CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager,
                                                       final EventManager eventManager,
                                                        final Broadcaster broadcaster
    ) {

        this.errorManager                            = errorManager;
        this.cryptoVaultManager                      = cryptoVaultManager;
        this.bitcoinNetworkManager                   = bitcoinNetworkManager;
        this.bitcoinWalletManager                    = bitcoinWalletManager;
        this.outgoingIntraActorDao                   = outgoingIntraActorDao;
        this.errorManager = errorManager;
        this.cryptoVaultManager = cryptoVaultManager;
        this.bitcoinWalletManager = bitcoinWalletManager;
        this.outgoingIntraActorDao = outgoingIntraActorDao;
        this.cryptoTransmissionNetworkServiceManager = cryptoTransmissionNetworkServiceManager;
        this.eventManager = eventManager;
        this.broadcaster = broadcaster;


    }


    public void start() {
        this.transactionProcessorAgent = new TransactionProcessorAgent();
        this.transactionProcessorAgent.initialize(this.errorManager,this.outgoingIntraActorDao,this.bitcoinWalletManager,this.cryptoVaultManager,this.bitcoinNetworkManager,this.cryptoTransmissionNetworkServiceManager, this.broadcaster);
        this.agentThread               = new Thread(this.transactionProcessorAgent);
        this.status = AgentStatus.STARTED;
        System.out.println("Agent started - started ");
    }

    public boolean isRunning() {
        return this.transactionProcessorAgent != null && this.transactionProcessorAgent.isRunning();
    }

    public void stop() {
        if (isRunning()) {
            this.transactionProcessorAgent.stop();
        }

    }


    private static class TransactionProcessorAgent implements Runnable {

        private AtomicBoolean running = new AtomicBoolean(false);
        private OutgoingDraftTransactionDao dao;
        private ErrorManager errorManager;
        private BitcoinWalletManager bitcoinWalletManager;
        private CryptoVaultManager cryptoVaultManager;
        private BitcoinNetworkManager bitcoinNetworkManager;
        private CryptoTransmissionNetworkServiceManager cryptoTransmissionManager;
        private EventManager eventManager;
        private Broadcaster broadcaster;


        private static final int SLEEP_TIME = 5000;


        /**
         * MonitorAgent interface implementation.
         */
        private void initialize (ErrorManager                               errorManager,
                                 OutgoingDraftTransactionDao dao,
                                 BitcoinWalletManager                       bitcoinWalletManager,
                                 CryptoVaultManager cryptoVaultManager,
                                 BitcoinNetworkManager bitcoinNetworkManager,
                                 CryptoTransmissionNetworkServiceManager    cryptoTransmissionNetworkServiceManager,
                                 Broadcaster broadcaster
                                 ) {
            this.dao = dao;
            this.errorManager = errorManager;
            this.cryptoVaultManager = cryptoVaultManager;
            this.bitcoinNetworkManager = bitcoinNetworkManager;
            this.bitcoinWalletManager = bitcoinWalletManager;
            this.broadcaster = broadcaster;
        }

        private void initialize(ErrorManager errorManager,
                                OutgoingDraftTransactionDao dao,
                                BitcoinWalletManager bitcoinWalletManager,
                                CryptoVaultManager cryptoVaultManager,
                                CryptoTransmissionNetworkServiceManager cryptoTransmissionNetworkServiceManager,
                                EventManager eventManager,
                                Broadcaster broadcaster) {
            this.dao = dao;
            this.errorManager = errorManager;
            this.cryptoVaultManager = cryptoVaultManager;
            this.bitcoinWalletManager = bitcoinWalletManager;
            this.cryptoTransmissionManager = cryptoTransmissionNetworkServiceManager;
            this.eventManager = eventManager;
            this.broadcaster = broadcaster;
        }

        public boolean isRunning() {
            return running.get();
        }

        public void stop() {
            running.set(false);
        }

        /**
         * Runnable Interface implementation.
         */
        @Override
        public void run() {

            running.set(true);
            /**
             * Infinite loop.
             */
            while (running.get()) {

                /**
                 * Sleep for a while.
                 */
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException interruptedException) {
                    cleanResources();
                    return;
                }

                /**
                 * Now I do the main task.
                 */
                doTheMainTask();

                /**
                 * Check if I have been Interrupted.
                 */
                if (Thread.currentThread().isInterrupted()) {
                    cleanResources();
                    return;
                }
            }
        }

        private void doTheMainTask() {
            try {

                List<OutgoingDraftTransactionWrapper> transactionList = dao.getNewTransactions();

//                System.out.print("-----------------------\n" +
//                        "OUTGOING INTRA USER TRANSACTION START - Get Pending Transactions!!!!! -----------------------\n" +
//                        "-----------------------\n STATE: ");



            /* For each transaction:
             1. We check that we can apply it
             2. We apply it in the bitcoin wallet available balance
            */
                for (OutgoingDraftTransactionWrapper transaction : transactionList) {
                    try {

                        BitcoinWalletTransactionRecord bitcoinWalletTransactionRecord = buildBitcoinTransaction(
                                transaction.getRequestId(),
                                transaction.getTxHash(),
                                transaction.getAddressTo(),
                                transaction.getActorFromPublicKey(),
                                transaction.getActorToPublicKey(),
                                transaction.getActorFromType(),
                                transaction.getActorToType(),
                                transaction.getValueToSend(),
                                transaction.getTimestamp(),
                                transaction.getMemo(),
                                transaction.getBlockchainNetworkType()
                        );
                        if (thereAreEnoughFunds(transaction)) {
                            debitFromAvailableBalance(bitcoinWalletTransactionRecord,transaction.getReferenceWallet(),transaction.getWalletPublicKey());
                            dao.setToDIW(transaction);
                            System.out.print("Debit new transaction.");
                        } else {
                            dao.cancelTransaction(transaction);
                            roolback(bitcoinWalletTransactionRecord,transaction.getReferenceWallet(),transaction.getWalletPublicKey(),false);
                            System.out.print("fondos insuficientes");
                            System.out.print("ROLLBACK 1");
                        }
                    } catch (OutgoingIntraActorWalletNotSupportedException | CantCalculateBalanceException
                            | CantRegisterDebitException | OutgoingIntraActorCantCancelTransactionException
                            | CantLoadWalletException e) {
                        //reportUnexpectedException(e);
                        dao.setToDIW(transaction);

                    }
                }

                // Now we check for all the transactions that have been discounted from the available amount
                // but bot applied to vault

                transactionList = dao.getAllInState(com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.outgoing_draft.developer.bitdubai.version_1.enums.TransactionState.DEBITED_IN_WALLET);


                for (OutgoingDraftTransactionWrapper transaction : transactionList) {
                    try {

                        DraftTransaction draftTransaction = cryptoVaultManager.getDraftTransaction(transaction.getBlockchainNetworkType(), transaction.getTxHash());

                        System.out.print("-------------- send draft to cryptoVaultManager");
                        draftTransaction = cryptoVaultManager.addInputsToDraftTransaction(draftTransaction, transaction.getValueToSend(), transaction.getAddressTo());

                        // just send the metadata in this place. This MUST be corrected.
                        dao.setToSTCV(transaction);

                    } catch (OutgoingIntraActorCantCancelTransactionException e) {
                        //If we cannot send the money at this moment then we'll keep trying.
                        reportUnexpectedException(e);

                        //if I spend more than five minutes I canceled
                        long sentDate = transaction.getTimestamp();
                        long currentTime = System.currentTimeMillis();
                        long dif = currentTime - sentDate;

                        if(dif >= 180000) {
                            dao.cancelTransaction(transaction);
                            roolback(buildBitcoinTransaction(
                                    transaction.getRequestId(),
                                    transaction.getTxHash(),
                                    transaction.getAddressTo(),
                                    transaction.getActorFromPublicKey(),
                                    transaction.getActorToPublicKey(),
                                    transaction.getActorFromType(),
                                    transaction.getActorToType(),
                                    transaction.getValueToSend(),
                                    transaction.getTimestamp(),
                                    transaction.getMemo(),
                                    transaction.getBlockchainNetworkType()
                            ),transaction.getReferenceWallet(),transaction.getWalletPublicKey(), true);
                            System.out.print("ROLLBACK 4");
                        }

                    }
                }





            /*
             * Now we proceed to apply the transactions sent to the bitcoin network to the wallet book
             * balance. We need to check the state of the transaction to the crypto vault before
             * discounting it
             */
                //TODO mejorar esto
                transactionList = dao.getSentToCryptoVaultTransactionsAndNotRead();

                /**
                 * Now we proceed to launch the notification
                 */
                if(!transactionList.isEmpty()){
                    launchFinishNotification();
                }else{
                    running.set(false);
                }
//                for (OutgoingDraftTransactionWrapper transaction : transactionList){
//                   launchFinishNotification();
//                }


            } catch (OutgoingIntraActorCantGetTransactionsException e) {
                reportUnexpectedException(e);
            } catch (Exception e) {
                reportUnexpectedException(FermatException.wrapException(e));
            }
        }



        private void cleanResources() {

        }

        private boolean thereAreEnoughFunds(OutgoingDraftTransactionWrapper transaction) throws OutgoingIntraActorWalletNotSupportedException, CantCalculateBalanceException, CantLoadWalletException {
            return getWalletAvailableBalance(transaction.getWalletPublicKey(), transaction.getReferenceWallet(), transaction.getBlockchainNetworkType()) >= transaction.getValueToSend();
        }

        private void reportUnexpectedException(Exception e) {
            this.errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CCP_OUTGOING_INTRA_ACTOR_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
        }

        private long getWalletAvailableBalance(String walletPublicKey, ReferenceWallet referenceWallet, BlockchainNetworkType blockchainNetworkType) throws CantLoadWalletException, CantCalculateBalanceException, OutgoingIntraActorWalletNotSupportedException {
            switch (referenceWallet) {
                case BASIC_WALLET_BITCOIN_WALLET:
                    return this.bitcoinWalletManager.loadWallet(walletPublicKey).getBalance(BalanceType.AVAILABLE).getBalance(blockchainNetworkType);
                default:
                    throw new OutgoingIntraActorWalletNotSupportedException("The wallet is not supported", null, "ReferenceWallet enum value: " + referenceWallet.toString(), "Missing case in switch statement");
            }
        }

        private void debitFromAvailableBalance(BitcoinWalletTransactionRecord transaction,ReferenceWallet referenceWallet,String walletPublicKey) throws CantLoadWalletException, CantRegisterDebitException, OutgoingIntraActorWalletNotSupportedException {
            switch (referenceWallet) {
                case BASIC_WALLET_BITCOIN_WALLET:
                    this.bitcoinWalletManager.loadWallet(walletPublicKey).getBalance(BalanceType.AVAILABLE).debit(transaction);
                    break;
                default:
                    throw new OutgoingIntraActorWalletNotSupportedException("The wallet is not supported", null, "ReferenceWallet enum value: " + walletPublicKey.toString(), "Missing case in switch statement");
            }
        }

        /**
         * bitcoin wallet and vault different states
         *
         * @param transaction
         */
        private void roolback(BitcoinWalletTransactionRecord transaction,ReferenceWallet referenceWallet,String walletPublicKey, boolean credit) {
            try {
                switch (referenceWallet) {
                    case BASIC_WALLET_BITCOIN_WALLET:
                        //TODO: hay que disparar un evento para que la wallet avise que la transaccion no se completo y eliminarla
                        BitcoinWalletWallet bitcoinWalletWallet = bitcoinWalletManager.loadWallet(walletPublicKey);
                        if(credit) {
                            bitcoinWalletWallet.getBalance(BalanceType.AVAILABLE).credit(transaction);
                        }
                        bitcoinWalletWallet.deleteTransaction(transaction.getRequestId());
                        //if the transaction is a payment request, rollback it state too
                        notificateRollbackToGUI(transaction);
                        break;
                    default:
                        throw new OutgoingIntraActorWalletNotSupportedException("Roolback", null, "ReferenceWallet enum value: " + referenceWallet.toString(), " Roolback");
                }
            } catch (CantLoadWalletException e) {
                e.printStackTrace();
            } catch (OutgoingIntraActorWalletNotSupportedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        private void roolback(){
//            FermatEvent fermatEvent = eventManager.getNewEvent(EventType.INCOMING_CRYPTO_METADATA);
//            IncomingCryptoMetadataEvent incomingCryptoMetadataReceive = (IncomingCryptoMetadataEvent) fermatEvent;
//            incomingCryptoMetadataReceive.setSource(EventSource.NETWORK_SERVICE_CRYPTO_TRANSMISSION);
//            eventManager.raiseEvent(incomingCryptoMetadataReceive);
//        }



        private void notificateRollbackToGUI(BitcoinWalletTransactionRecord transactionWrapper){
           /* FermatEvent                    platformEvent                  = eventManager.getNewEvent(EventType.OUTGOING_ROLLBACK_NOTIFICATION);
            OutgoingIntraRollbackNotificationEvent outgoingIntraRollbackNotificationEvent = (OutgoingIntraRollbackNotificationEvent) platformEvent;
            outgoingIntraRollbackNotificationEvent.setSource(EventSource.OUTGOING_INTRA_USER);
            outgoingIntraRollbackNotificationEvent.setActorId(transactionWrapper.getActorToPublicKey());
            outgoingIntraRollbackNotificationEvent.setActorType(transactionWrapper.getActorToType());
            outgoingIntraRollbackNotificationEvent.setAmount(transactionWrapper.getAmount());
            outgoingIntraRollbackNotificationEvent.setCryptoStatus(transactionWrapper.getCryptoStatus());
            outgoingIntraRollbackNotificationEvent.setWalletPublicKey(transactionWrapper.getWalletPublicKey());

            eventManager.raiseEvent(platformEvent);*/

            //broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, "TRANSACTION_REVERSE|" + transactionWrapper.getTransactionId().toString());

        }

        private void launchFinishNotification() {
            try {
                //Hay que disparar un evento para que escuche el Crypto Payment y revierta el accepted
                FermatEvent platformEvent  = eventManager.getNewEvent(EventType.OUTGOING_DRAFT_TRANSACTION_FINISHED);
                OutgoingIntraUserTransactionRollbackNotificationEvent outgoingIntraUserTransactionRollbackNotificationEvent = (OutgoingIntraUserTransactionRollbackNotificationEvent) platformEvent;
                outgoingIntraUserTransactionRollbackNotificationEvent.setSource(EventSource.CCP_OUTGOING_DRAFT_TRANSACTION);
                eventManager.raiseEvent(platformEvent);
            }
            catch(Exception e) {
                errorManager.reportUnexpectedPluginException(Plugins.CCP_OUTGOING_DRAFT_TRANSACTION, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
            }
        }





        private BitcoinWalletTransactionRecord buildBitcoinTransaction( final UUID transactionId,
                                                                        final String transactionHash,
                                                                        final CryptoAddress addressTo,
                                                                        final String actorFromPublicKey,
                                                                        final String actorToPublicKey,
                                                                        final Actors actorFromType,
                                                                        final Actors actorToType,
                                                                        final long amount,
                                                                        final long timeStamp,
                                                                        final String memo,
                                                                        final BlockchainNetworkType blockchainNetworkType) {
            return new BitcoinWalletTransactionRecord() {
                @Override
                public CryptoAddress getAddressFrom() {
                    return null;
                }

                @Override
                public UUID getTransactionId() {
                    return transactionId;
                }

                @Override
                public UUID getRequestId() {
                    return null;
                }

                @Override
                public CryptoAddress getAddressTo() {
                    return addressTo;
                }

                @Override
                public long getAmount() {
                    return amount;
                }

                @Override
                public long getTimestamp() {
                    return timeStamp;
                }

                @Override
                public String getMemo() {
                    return memo;
                }

                @Override
                public String getTransactionHash() {
                    return transactionHash;
                }

                @Override
                public String getActorToPublicKey() {
                    return actorToPublicKey;
                }

                @Override
                public String getActorFromPublicKey() {
                    return actorFromPublicKey;
                }

                @Override
                public Actors getActorToType() {
                    return actorToType;
                }

                @Override
                public Actors getActorFromType() {
                    return actorFromType;
                }

                @Override
                public BlockchainNetworkType getBlockchainNetworkType() {
                    return blockchainNetworkType;
                }
            };
        }

    }






}
