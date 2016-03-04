/*
 * @#AssetTransmissionPluginRoot.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1;

import android.util.Base64;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.DiscoveryQueryParameters;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.BroadcasterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPMessageSubject;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPMessageType;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPPublicKeys;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.EventType;
import com.bitdubai.fermat_dap_api.layer.all_definition.events.ActorAssetIssuerCompleteRegistrationNotificationEvent;
import com.bitdubai.fermat_dap_api.layer.all_definition.events.ActorAssetNetworkServicePendingNotificationEvent;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPMessage;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantGetDAPMessagesException;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantSendMessageException;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.exceptions.CantUpdateMessageStatusException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.DAPActor;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.AssetIssuerActorRecord;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.ActorAssetNetworkServiceRecord;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRegisterActorAssetIssuerException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRequestListActorAssetIssuerRegisteredException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.interfaces.AssetIssuerActorNetworkServiceManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.enums.ActorAssetProtocolState;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.enums.AssetNotificationDescriptor;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantConfirmActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCreateActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDisconnectConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantReadRecordDataBaseException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantUpdateRecordDataBaseException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.interfaces.ActorNotification;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseFactory;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDeveloperDatabaseFactory;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.IncomingNotificationDao;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.OutgoingNotificationDao;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.exceptions.CantInitializeTemplateNetworkServiceDatabaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.contents.FermatMessageCommunication;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.base.AbstractNetworkServiceBase;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.MessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.contents.FermatMessage;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.enums.FermatMessagesStatus;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRegisterComponentException;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRequestListException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.communications.CommunicationNetworkServiceLocal;
//import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.database.communications.OutgoingMessageDao;

/**
 * The Class <code>com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.AssetIssuerActorNetworkServicePluginRoot</code> is
 * the responsible to initialize all component to work together, and hold all resources they needed.
 * <p/>
 * Created by Roberto Requena - (rrequena) on 21/07/15.
 * Modified by franklin on 17/10/2015
 *
 * @version 1.0
 */
public class AssetIssuerActorNetworkServicePluginRootNew extends AbstractNetworkServiceBase implements
        AssetIssuerActorNetworkServiceManager,
        DatabaseManagerForDevelopers,
        LogManagerForDevelopers {

    /**
     * Represent the dataBase
     */
    private Database dataBase;

    protected final static String DAP_IMG_ISSUER = "DAP_IMG_ISSUER";

//    /**
//     * DealsWithLogger interface member variable
//     */
//    static Map<String, LogLevel> newLoggingLevel = new HashMap<>();

    /**
     * Represent the communicationNetworkServiceDeveloperDatabaseFactory
     */
    private CommunicationNetworkServiceDeveloperDatabaseFactory communicationNetworkServiceDeveloperDatabaseFactory;

    /**
     * Represent the communicationRegistrationProcessNetworkServiceAgent
     */
//    private CommunicationRegistrationProcessNetworkServiceAgent communicationRegistrationProcessNetworkServiceAgent;

    /**
     * Represent the communicationNetworkServiceConnectionManager
     */
//    private CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager;

    /**
     * Represent the actorAssetIssuerRegisteredList
     */
    private List<ActorAssetIssuer> actorAssetIssuerRegisteredList;

    /**
     * Represent the actorAssetIssuerPendingToRegistration
     */
    private List<PlatformComponentProfile> actorAssetIssuerPendingToRegistration;

    /**
     * DAO
     */
    private IncomingNotificationDao incomingNotificationsDao;
    private OutgoingNotificationDao outgoingNotificationDao;

    private long reprocessTimer = 300000; //five minutes

    private Timer timer = new Timer();
    /**
     * Executor
     */
    ExecutorService executorService;

//    private AssetIssuerActorNetworkServiceAgent assetIssuerActorNetworkServiceAgent;

    /**
     * Constructor
     */
    public AssetIssuerActorNetworkServicePluginRootNew() {
        super(
                new PluginVersionReference(new Version()),
                EventSource.NETWORK_SERVICE_ACTOR_ASSET_ISSUER,
                PlatformComponentType.NETWORK_SERVICE,
                NetworkServiceType.ASSET_ISSUER_ACTOR,
                "Actor Network Service Asset Issuer",
                null
        );
        this.actorAssetIssuerRegisteredList = new ArrayList<>();
        this.actorAssetIssuerPendingToRegistration = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        try {
        /*
         * Initialize the data base
         */
            initializeDb();
        /*
         * Initialize Developer Database Factory
         */
            communicationNetworkServiceDeveloperDatabaseFactory = new CommunicationNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
            communicationNetworkServiceDeveloperDatabaseFactory.initializeDatabase();

            //DAO
            incomingNotificationsDao = new IncomingNotificationDao(dataBase, this.pluginFileSystem, this.pluginId);

            outgoingNotificationDao = new OutgoingNotificationDao(dataBase, this.pluginFileSystem, this.pluginId);

            executorService = Executors.newFixedThreadPool(2);

            // change message state to process again first time
            reprocessPendingMessage();

            //declare a schedule to process waiting request message
            this.startTimer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        super.stop();
        executorService.shutdownNow();
    }

    @Override
    public void onNewMessagesReceive(FermatMessage newFermatMessageReceive) {
        try {
            System.out.println("ACTOR ASSET ISSUER MENSAJE ENTRANTE A JSON: " + newFermatMessageReceive.toJson());

            ActorAssetNetworkServiceRecord assetUserNetworkServiceRecord = ActorAssetNetworkServiceRecord.fronJson(newFermatMessageReceive.getContent());

            switch (assetUserNetworkServiceRecord.getAssetNotificationDescriptor()) {
                case ASKFORCONNECTION:
                    System.out.println("ACTOR ASSET ISSUER MENSAJE LLEGO: " + assetUserNetworkServiceRecord.getActorSenderAlias());
                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_RECEIVE);
                    System.out.println("ACTOR ASSET ISSUER REGISTRANDO EN INCOMING NOTIFICATION DAO");
                    assetUserNetworkServiceRecord.setFlagRead(false);
                    incomingNotificationsDao.createNotification(assetUserNetworkServiceRecord);

                    //NOTIFICATION LAUNCH
                    launchNotificationActorAsset();
//                    broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, "CONNECTION_REQUEST|" + assetUserNetworkServiceRecord.getActorSenderPublicKey());
                    broadcaster.publish(BroadcasterType.NOTIFICATION_SERVICE, DAPPublicKeys.DAP_COMMUNITY_ISSUER.getCode(), "CONNECTION-REQUEST_" + assetUserNetworkServiceRecord.getActorSenderPublicKey());

                    respondReceiveAndDoneCommunication(assetUserNetworkServiceRecord);
                    break;

                case ACCEPTED:
                    //TODO: ver si me conviene guardarlo en el outogoing DAO o usar el incoming para las que llegan directamente
                    assetUserNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.ACCEPTED);
                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.DONE);
                    outgoingNotificationDao.update(assetUserNetworkServiceRecord);

                    //create incoming notification
                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_RECEIVE);
                    assetUserNetworkServiceRecord.setFlagRead(false);
                    incomingNotificationsDao.createNotification(assetUserNetworkServiceRecord);
                    System.out.println("ACTOR ASSET ISSUER MENSAJE ACCEPTED LLEGÓ: " + assetUserNetworkServiceRecord.getActorSenderAlias());

                    //NOTIFICATION LAUNCH
                    launchNotificationActorAsset();
                    respondReceiveAndDoneCommunication(assetUserNetworkServiceRecord);
                    break;

                case RECEIVED:
                    //launchIncomingRequestConnectionNotificationEvent(actorNetworkServiceRecord);
                    System.out.println("ACTOR ASSET ISSUER THE RECORD WAS CHANGE TO THE STATE OF DELIVERY: " + assetUserNetworkServiceRecord.getActorSenderAlias());
                    if (assetUserNetworkServiceRecord.getResponseToNotificationId() != null)
                        outgoingNotificationDao.changeProtocolState(assetUserNetworkServiceRecord.getResponseToNotificationId(), ActorAssetProtocolState.DONE);

                    // close connection, sender is the destination
                    System.out.println("ACTOR ASSET ISSUER THE CONNECTION WAS CHANGE TO DONE" + assetUserNetworkServiceRecord.getActorSenderAlias());

                    getCommunicationNetworkServiceConnectionManager().closeConnection(assetUserNetworkServiceRecord.getActorSenderPublicKey());
                    System.out.println("ACTOR ASSET ISSUER THE CONNECTION WAS CLOSED AND THE AWAITING POOL CLEARED." + assetUserNetworkServiceRecord.getActorSenderAlias());
                    break;

                case DENIED:
                    assetUserNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.DENIED);
                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.DONE);
                    outgoingNotificationDao.update(assetUserNetworkServiceRecord);

                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_RECEIVE);
                    assetUserNetworkServiceRecord.setFlagRead(false);
                    incomingNotificationsDao.createNotification(assetUserNetworkServiceRecord);
                    System.out.println("ACTOR ASSET ISSUER MENSAJE DENIED LLEGÓ: " + assetUserNetworkServiceRecord.getActorDestinationPublicKey());

                    //NOTIFICATION LAUNCH
                    launchNotificationActorAsset();
                    respondReceiveAndDoneCommunication(assetUserNetworkServiceRecord);
                    break;

                case DISCONNECTED:
                    assetUserNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.DISCONNECTED);
                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.DONE);
                    outgoingNotificationDao.update(assetUserNetworkServiceRecord);

                    assetUserNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_RECEIVE);
                    assetUserNetworkServiceRecord.setFlagRead(false);
                    incomingNotificationsDao.createNotification(assetUserNetworkServiceRecord);
                    System.out.println("ACTOR ASSET ISSUER MENSAJE DISCONNECTED LLEGÓ: " + assetUserNetworkServiceRecord.getActorSenderAlias());

                    //NOTIFICATION LAUNCH
                    launchNotificationActorAsset();
                    respondReceiveAndDoneCommunication(assetUserNetworkServiceRecord);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            //quiere decir que no estoy reciviendo metadata si no una respuesta
            e.printStackTrace();

        }

        System.out.println("Actor Asset Issuer Llegaron mensajes!!!!");

        try {
            getCommunicationNetworkServiceConnectionManager().getIncomingMessageDao().markAsRead(newFermatMessageReceive);
        } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantUpdateRecordDataBaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSentMessage(FermatMessage messageSent) {
        try {
            ActorAssetNetworkServiceRecord assetUserNetworkServiceRecord = ActorAssetNetworkServiceRecord.fronJson(messageSent.getContent());

            if (assetUserNetworkServiceRecord.getActorAssetProtocolState() == ActorAssetProtocolState.DONE) {
                // close connection, sender is the destination
                System.out.println("ACTOR ASSET ISSUER CERRANDO LA CONEXION DEL HANDLE NEW SENT MESSAGE NOTIFICATION");
                //   communicationNetworkServiceConnectionManager.closeConnection(actorNetworkServiceRecord.getActorDestinationPublicKey());
//                assetUserActorNetworkServiceAgent.getPoolConnectionsWaitingForResponse().remove(assetUserNetworkServiceRecord.getActorDestinationPublicKey());
            }

            //done message type receive
            if (assetUserNetworkServiceRecord.getAssetNotificationDescriptor() == AssetNotificationDescriptor.RECEIVED) {
                assetUserNetworkServiceRecord.setActorAssetProtocolState(ActorAssetProtocolState.DONE);
                outgoingNotificationDao.update(assetUserNetworkServiceRecord);
//                assetUserActorNetworkServiceAgent.getPoolConnectionsWaitingForResponse().remove(assetUserNetworkServiceRecord.getActorDestinationPublicKey());
            }
            System.out.println("SALIENDO DEL HANDLE ACTOR ASSET ISSUER NEW SENT MESSAGE NOTIFICATION");

        } catch (Exception e) {
            //quiere decir que no estoy reciviendo metadata si no una respuesta
            System.out.println("EXCEPCION DENTRO DEL PROCCESS EVENT IN ACTOR ASSET ISSUER");
            e.printStackTrace();

        }
    }

    @Override
    protected void onNetworkServiceRegistered() {
        try {
            //TODO Test this functionality
            for (PlatformComponentProfile platformComponentProfile : actorAssetIssuerPendingToRegistration) {
                wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().registerComponentForCommunication(getNetworkServiceProfile().getNetworkServiceType(), platformComponentProfile);
                System.out.println("AssetIssuerActorNetworkServicePluginRoot - Trying to register to: " + platformComponentProfile.getAlias());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFailureComponentConnectionRequest(PlatformComponentProfile remoteParticipant) {
        //I check my time trying to send the message
        checkFailedDeliveryTime(remoteParticipant.getIdentityPublicKey());
    }

//    @Override
//    public PlatformComponentProfile getProfileSenderToRequestConnection(String identityPublicKeySender) {
//        try {
//
//            Actors actors = outgoingNotificationDao.getActorTypeFromRequest(identityPublicKeySender);
//
//            return wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection()
//                    .constructPlatformComponentProfileFactory(identityPublicKeySender,
//                            "sender_alias",
//                            "sender_name",
//                            NetworkServiceType.UNDEFINED,
//                            platformComponentTypeSelectorByActorType(actors),
//                            "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public PlatformComponentProfile getProfileDestinationToRequestConnection(String identityPublicKeyDestination) {
//        try {
//
//            Actors actors = outgoingNotificationDao.getActorTypeToRequest(identityPublicKeyDestination);
//
//            return wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection()
//                    .constructPlatformComponentProfileFactory(identityPublicKeyDestination,
//                            "destination_alias",
//                            "destination_name",
//                            NetworkServiceType.UNDEFINED,
//                            platformComponentTypeSelectorByActorType(actors),
//                            "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private void reprocessPendingMessage() {
        try {
            outgoingNotificationDao.changeStatusNotSentMessage();

            List<ActorAssetNetworkServiceRecord> lstActorRecord = outgoingNotificationDao.
                    listRequestsByProtocolStateAndNotDone(
                            ActorAssetProtocolState.PROCESSING_SEND
                    );

            for (final ActorAssetNetworkServiceRecord cpr : lstActorRecord) {

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendNewMessage(
//                                    getProfileSenderToRequestConnection(cpr.getActorSenderPublicKey()),
//                                    getProfileDestinationToRequestConnection(cpr.getActorDestinationPublicKey()),
//                                    cpr.toJson());

                                    getProfileSenderToRequestConnection(
                                            cpr.getActorSenderPublicKey(),
                                            NetworkServiceType.UNDEFINED,
                                            platformComponentTypeSelectorByActorType(cpr.getActorSenderType())
                                    ),
                                    getProfileDestinationToRequestConnection(
                                            cpr.getActorDestinationPublicKey(),
                                            NetworkServiceType.UNDEFINED,
                                            platformComponentTypeSelectorByActorType(cpr.getActorDestinationType())
                                    ),
                                    cpr.toJson());
                        } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (CantGetActorAssetNotificationException e) {
            System.out.println("ACTOR ASSET USER NS EXCEPCION REPROCESANDO MESSAGEs");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("ACTOR ASSET USER NS EXCEPCION REPROCESANDO MESSAGEs");
            e.printStackTrace();
        }
    }

    @Override
    protected void reprocessMessages() {
//        try {
//            outgoingNotificationDao.changeStatusNotSentMessage();
//        } catch (CantGetActorAssetNotificationException e) {
//            System.out.println("INTRA USER NS EXCEPCION REPROCESANDO MESSAGEs");
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("INTRA USER NS EXCEPCION REPROCESANDO MESSAGEs");
//            e.printStackTrace();
//        }
    }

    @Override
    protected void reprocessMessages(String identityPublicKey) {
//        try {
//            outgoingNotificationDao.changeStatusNotSentMessage(identityPublicKey);
//        } catch (CantGetActorAssetNotificationException e) {
//            System.out.println("ACTOR ASSET USER NS EXCEPCION REPROCESANDO MESSAGEs");
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("ACTOR ASSET USER NS EXCEPCION REPROCESANDO MESSAGEs");
//            e.printStackTrace();
//        }
    }

    private PlatformComponentType platformComponentTypeSelectorByActorType(final Actors type) throws InvalidParameterException {

        switch (type) {
            case DAP_ASSET_ISSUER:
                return PlatformComponentType.ACTOR_ASSET_ISSUER;
            case DAP_ASSET_USER:
                return PlatformComponentType.ACTOR_ASSET_USER;
            case DAP_ASSET_REDEEM_POINT:
                return PlatformComponentType.ACTOR_ASSET_REDEEM_POINT;

            default:
                throw new InvalidParameterException(
                        " actor type: " + type.name() + "  type-code: " + type.getCode(),
                        " type of actor not expected."
                );
        }
    }

    //TODO REVISAR ESCUCHAR/LANZAR MISMO EVENTO PARA LOS 3 ACTORES PUEDA AFECTAR FUNCIONAMIENTO
    private void launchNotificationActorAsset() {
        FermatEvent fermatEvent = eventManager.getNewEvent(EventType.ACTOR_ASSET_NETWORK_SERVICE_NEW_NOTIFICATIONS);
        fermatEvent.setSource(EventSource.NETWORK_SERVICE_ACTOR_ASSET_ISSUER);
        ActorAssetNetworkServicePendingNotificationEvent actorAssetRequestConnectionEvent = (ActorAssetNetworkServicePendingNotificationEvent) fermatEvent;
        eventManager.raiseEvent(actorAssetRequestConnectionEvent);
    }

    private ActorAssetNetworkServiceRecord swapActor(ActorAssetNetworkServiceRecord assetUserNetworkServiceRecord) {
        // swap actor
        String actorDestination = assetUserNetworkServiceRecord.getActorDestinationPublicKey();
        Actors actorsType = assetUserNetworkServiceRecord.getActorDestinationType();

        assetUserNetworkServiceRecord.setActorDestinationPublicKey(assetUserNetworkServiceRecord.getActorSenderPublicKey());
        assetUserNetworkServiceRecord.setActorDestinationType(assetUserNetworkServiceRecord.getActorSenderType());

        assetUserNetworkServiceRecord.setActorSenderPublicKey(actorDestination);
        assetUserNetworkServiceRecord.setActorSenderType(actorsType);

        return assetUserNetworkServiceRecord;
    }

    // respond receive and done notification
    private void respondReceiveAndDoneCommunication(ActorAssetNetworkServiceRecord assetUserNetworkServiceRecord) {

        assetUserNetworkServiceRecord = swapActor(assetUserNetworkServiceRecord);
        try {
            UUID newNotificationID = UUID.randomUUID();
            long currentTime = System.currentTimeMillis();
            ActorAssetProtocolState actorAssetProtocolState = ActorAssetProtocolState.PROCESSING_SEND;
            assetUserNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.RECEIVED);
            outgoingNotificationDao.createNotification(
                    newNotificationID,
                    assetUserNetworkServiceRecord.getActorSenderPublicKey(),
                    assetUserNetworkServiceRecord.getActorSenderType(),
                    assetUserNetworkServiceRecord.getActorDestinationPublicKey(),
                    assetUserNetworkServiceRecord.getActorSenderAlias(),
                    assetUserNetworkServiceRecord.getActorSenderProfileImage(),
                    assetUserNetworkServiceRecord.getActorDestinationType(),
                    assetUserNetworkServiceRecord.getAssetNotificationDescriptor(),
                    currentTime,
                    actorAssetProtocolState,
                    false,
                    1,
                    assetUserNetworkServiceRecord.getBlockchainNetworkType(),
                    assetUserNetworkServiceRecord.getId()
            );
        } catch (CantCreateActorAssetNotificationException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method initialize the database
     *
     * @throws CantInitializeTemplateNetworkServiceDatabaseException
     */
    private void initializeDb() throws CantInitializeTemplateNetworkServiceDatabaseException {

        try {
            /*
             * Open new database connection
             */
            this.dataBase = this.pluginDatabaseSystem.openDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {

            /*
             * The database exists but cannot be open. I can not handle this situation.
             */
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantOpenDatabaseException);
            throw new CantInitializeTemplateNetworkServiceDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

        } catch (DatabaseNotFoundException e) {

            /*
             * The database no exist may be the first time the plugin is running on this device,
             * We need to create the new database
             */
            CommunicationNetworkServiceDatabaseFactory communicationNetworkServiceDatabaseFactory = new CommunicationNetworkServiceDatabaseFactory(pluginDatabaseSystem);

            try {

                /*
                 * We create the new database
                 */
                this.dataBase = communicationNetworkServiceDatabaseFactory.createDatabase(pluginId, CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            } catch (CantCreateDatabaseException cantOpenDatabaseException) {

                /*
                 * The database cannot be created. I can not handle this situation.
                 */
                reportUnexpectedError(e);
                throw new CantInitializeTemplateNetworkServiceDatabaseException(cantOpenDatabaseException.getLocalizedMessage());

            }
        }
    }

    private void checkFailedDeliveryTime(String destinationPublicKey) {
        try {

            List<ActorAssetNetworkServiceRecord> actorNetworkServiceRecordList = outgoingNotificationDao.getNotificationByDestinationPublicKey(destinationPublicKey);

            //if I try to send more than 5 times I put it on hold
            for (ActorAssetNetworkServiceRecord record : actorNetworkServiceRecordList) {

                if (!record.getActorAssetProtocolState().getCode().equals(ActorAssetProtocolState.WAITING_RESPONSE.getCode())) {
                    if (record.getSentCount() > 10) {
                        //  if(record.getSentCount() > 20)
                        //  {
                        //reprocess at two hours
                        //  reprocessTimer =  2 * 3600 * 1000;
                        // }

                        record.setActorAssetProtocolState(ActorAssetProtocolState.WAITING_RESPONSE);
                        record.setSentCount(1);
                        //update state and process again later

                        outgoingNotificationDao.update(record);
                    } else {
                        record.setSentCount(record.getSentCount() + 1);
                        outgoingNotificationDao.update(record);
                    }
                } else {
                    //I verify the number of days I'm around trying to send if it exceeds three days I delete record
                    long sentDate = record.getSentDate();
                    long currentTime = System.currentTimeMillis();
                    long dif = currentTime - sentDate;

                    double dias = Math.floor(dif / (1000 * 60 * 60 * 24));

                    if ((int) dias > 3) {
                        //notify the user does not exist to actor asset user actor plugin
                        record.changeDescriptor(AssetNotificationDescriptor.ACTOR_ASSET_NOT_FOUND);
                        incomingNotificationsDao.createNotification(record);

                        outgoingNotificationDao.delete(record.getId());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ACTOR ASSET ISSUER NS EXCEPCION VERIFICANDO WAIT MESSAGE");
            e.printStackTrace();
        }
    }

    /**
     * Mark the message as read
     *
     * @param fermatMessage
     */
    public void markAsRead(FermatMessage fermatMessage) throws CantUpdateRecordDataBaseException {
        try {
            ((FermatMessageCommunication) fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.READ);
            getCommunicationNetworkServiceConnectionManager().getIncomingMessageDao().update(fermatMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerActorAssetIssuer(ActorAssetIssuer actorAssetIssuerToRegister) throws CantRegisterActorAssetIssuerException {

        try {
            if (isRegister()) {
                final CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();
                /*
                 * Construct the profile
                 */
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(DAP_IMG_ISSUER, Base64.encodeToString(actorAssetIssuerToRegister.getProfileImage(), Base64.DEFAULT));
                String extraData = gson.toJson(jsonObject);

                final PlatformComponentProfile platformComponentProfileAssetIssuer = communicationsClientConnection.constructPlatformComponentProfileFactory(
                        actorAssetIssuerToRegister.getActorPublicKey(),
                        actorAssetIssuerToRegister.getName().toLowerCase().trim(),
                        actorAssetIssuerToRegister.getName(),
                        NetworkServiceType.UNDEFINED,
                        PlatformComponentType.ACTOR_ASSET_ISSUER,
                        extraData);
                /*
                 * ask to the communication cloud client to register
                 */
                /**
                 * I need to add this in a new thread other than the main android thread
                 */
                if (!actorAssetIssuerPendingToRegistration.contains(platformComponentProfileAssetIssuer)) {
                    actorAssetIssuerPendingToRegistration.add(platformComponentProfileAssetIssuer);
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            communicationsClientConnection.registerComponentForCommunication(
                                    getNetworkServiceProfile().getNetworkServiceType(), platformComponentProfileAssetIssuer);
                            onComponentRegistered(platformComponentProfileAssetIssuer);
                        } catch (CantRegisterComponentException e) {
                            e.printStackTrace();
                        }
                    }
                }, "ACTOR ASSET ISSUER REGISTER-ACTOR");
                thread.start();
            }
        } catch (Exception e) {
            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "Plugin was not registered";

            CantRegisterActorAssetIssuerException pluginStartException = new CantRegisterActorAssetIssuerException(CantStartPluginException.DEFAULT_MESSAGE, e, context, possibleCause);
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        }
    }

    @Override
    public void updateActorAssetIssuer(ActorAssetIssuer actorAssetIssuerToRegister) throws CantRegisterActorAssetIssuerException {

        try {
            if (isRegister()) {
                final CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();
                /*
                 * Construct the profile
                 */
                Gson gson = new Gson();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(DAP_IMG_ISSUER, Base64.encodeToString(actorAssetIssuerToRegister.getProfileImage(), Base64.DEFAULT));
                String extraData = gson.toJson(jsonObject);

                final PlatformComponentProfile platformComponentProfileAssetIssuer = communicationsClientConnection.constructPlatformComponentProfileFactory(
                        actorAssetIssuerToRegister.getActorPublicKey(),
                        actorAssetIssuerToRegister.getName().toLowerCase().trim(),
                        actorAssetIssuerToRegister.getName(),
                        NetworkServiceType.UNDEFINED,
                        PlatformComponentType.ACTOR_ASSET_ISSUER,
                        extraData);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            communicationsClientConnection.updateRegisterActorProfile(
                                    getNetworkServiceProfile().getNetworkServiceType(), platformComponentProfileAssetIssuer);
                            onComponentRegistered(platformComponentProfileAssetIssuer);
                        } catch (CantRegisterComponentException e) {
                            e.printStackTrace();
                        }
                    }
                }, "ACTOR ASSET ISSUER UPDATE-ACTOR");
                thread.start();
            }
        } catch (Exception e) {
            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "Plugin was not registered";

            CantRegisterActorAssetIssuerException pluginStartException = new CantRegisterActorAssetIssuerException(CantStartPluginException.DEFAULT_MESSAGE, e, context, possibleCause);
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        }
    }

    @Override
    public List<ActorAssetIssuer> getListActorAssetIssuerRegistered() throws CantRequestListActorAssetIssuerRegisteredException {

        try {
            if (actorAssetIssuerRegisteredList != null && !actorAssetIssuerRegisteredList.isEmpty()) {
                actorAssetIssuerRegisteredList.clear();
            }

            DiscoveryQueryParameters discoveryQueryParametersAssetUser = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
                    constructDiscoveryQueryParamsFactory(
                            PlatformComponentType.ACTOR_ASSET_ISSUER,   //applicant = who made the request
                            NetworkServiceType.UNDEFINED,               //NetworkServiceType you want to find
                            null,                                       // alias
                            null,                                       // identityPublicKey
                            null,                                       // location
                            null,                                       // distance
                            null,                                       // name
                            null,                                       // extraData
                            null,                                       // offset
                            null,                                       // max
                            null,                       // fromOtherPlatformComponentType, when use this filter apply the identityPublicKey
                            null);

            List<PlatformComponentProfile> platformComponentProfileRegisteredListRemote = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().requestListComponentRegistered(discoveryQueryParametersAssetUser);

            if (platformComponentProfileRegisteredListRemote != null && !platformComponentProfileRegisteredListRemote.isEmpty()) {

                for (PlatformComponentProfile platformComponentProfile : platformComponentProfileRegisteredListRemote) {

                    String profileImage = "";
                    if (!platformComponentProfile.getExtraData().equals("")) {
                        try {
                            JsonParser jParser = new JsonParser();
                            JsonObject jsonObject = jParser.parse(platformComponentProfile.getExtraData()).getAsJsonObject();

                            profileImage = jsonObject.get(DAP_IMG_ISSUER).getAsString();
                        } catch (Exception e) {
                            profileImage = platformComponentProfile.getExtraData();
                        }
                    }

                    byte[] imageByte = Base64.decode(profileImage, Base64.DEFAULT);

                    ActorAssetIssuer actorAssetIssuerNew = new AssetIssuerActorRecord(
                            platformComponentProfile.getIdentityPublicKey(),
                            platformComponentProfile.getName(),
                            imageByte,
                            platformComponentProfile.getLocation());

                    actorAssetIssuerRegisteredList.add(actorAssetIssuerNew);
                }
            } else {
                return actorAssetIssuerRegisteredList;
            }

        } catch (CantRequestListException e) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "Cant Request List Actor Asset User Registered";

            CantRequestListActorAssetIssuerRegisteredException pluginStartException = new CantRequestListActorAssetIssuerRegisteredException(CantRequestListActorAssetIssuerRegisteredException.DEFAULT_MESSAGE, null, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        }

        return actorAssetIssuerRegisteredList;
    }


    @Override
    public void askConnectionActorAsset(String actorAssetLoggedInPublicKey,
                                        String actorAssetLoggedName,
                                        Actors senderType,
                                        String actorAssetToAddPublicKey,
                                        String actorAssetToAddName,
                                        Actors destinationType,
                                        byte[] profileImage,
                                        BlockchainNetworkType blockchainNetworkType) throws CantAskConnectionActorAssetException {

        try {
            UUID newNotificationID = UUID.randomUUID();
            AssetNotificationDescriptor assetNotificationDescriptor = AssetNotificationDescriptor.ASKFORCONNECTION;
            long currentTime = System.currentTimeMillis();
            ActorAssetProtocolState actorAssetProtocolState = ActorAssetProtocolState.PROCESSING_SEND;

            final ActorAssetNetworkServiceRecord assetIssuerNetworkServiceRecord = outgoingNotificationDao.createNotification(
                    newNotificationID,
                    actorAssetLoggedInPublicKey,
                    senderType,
                    actorAssetToAddPublicKey,
                    actorAssetLoggedName,
//                    intraUserToAddPhrase,
                    profileImage,
                    destinationType,
                    assetNotificationDescriptor,
                    currentTime,
                    actorAssetProtocolState,
                    false,
                    1,
                    blockchainNetworkType,
                    null
            );

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendNewMessage(
//                                getProfileSenderToRequestConnection(assetIssuerNetworkServiceRecord.getActorSenderPublicKey()),
//                                getProfileDestinationToRequestConnection(assetIssuerNetworkServiceRecord.getActorDestinationPublicKey()),
//                                assetIssuerNetworkServiceRecord.toJson());
                                getProfileSenderToRequestConnection(
                                        assetIssuerNetworkServiceRecord.getActorSenderPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(assetIssuerNetworkServiceRecord.getActorSenderType())
                                ),
                                getProfileDestinationToRequestConnection(
                                        assetIssuerNetworkServiceRecord.getActorDestinationPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(assetIssuerNetworkServiceRecord.getActorDestinationType())
                                ),
                                assetIssuerNetworkServiceRecord.toJson());
                    } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Sending message to the destination
        } catch (final CantCreateActorAssetNotificationException e) {
            reportUnexpectedError(e);
            throw new CantAskConnectionActorAssetException(e, "actor asset issuer network service", "database corrupted");
        } catch (final Exception e) {
            reportUnexpectedError(e);
            throw new CantAskConnectionActorAssetException(e, "actor asset issuer network service", "Unhandled error.");
        }
    }

    @Override
    public void acceptConnectionActorAsset(String actorAssetLoggedInPublicKey, String ActorAssetToAddPublicKey)
            throws CantAcceptConnectionActorAssetException {

        try {
            ActorAssetNetworkServiceRecord assetIssuerNetworkServiceRecord = incomingNotificationsDao.
                    changeActorAssetNotificationDescriptor(
                            ActorAssetToAddPublicKey,
                            AssetNotificationDescriptor.ACCEPTED,
                            ActorAssetProtocolState.DONE);
//TODO Evaluar diferencias en ActorAssetProtocolState.DONE y ActorAssetProtocolState.PEDNING_ACTION para conocer diferencias

            Actors actorSwap = assetIssuerNetworkServiceRecord.getActorSenderType();

            assetIssuerNetworkServiceRecord.setActorSenderPublicKey(actorAssetLoggedInPublicKey);
            assetIssuerNetworkServiceRecord.setActorSenderType(assetIssuerNetworkServiceRecord.getActorDestinationType());

            assetIssuerNetworkServiceRecord.setActorDestinationPublicKey(ActorAssetToAddPublicKey);
            assetIssuerNetworkServiceRecord.setActorDestinationType(actorSwap);

            assetIssuerNetworkServiceRecord.setActorSenderAlias(null);

            assetIssuerNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.ACCEPTED);

            assetIssuerNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_SEND);

            final ActorAssetNetworkServiceRecord messageToSend = outgoingNotificationDao.createNotification(
                    UUID.randomUUID(),
                    assetIssuerNetworkServiceRecord.getActorSenderPublicKey(),
                    assetIssuerNetworkServiceRecord.getActorSenderType(),
                    assetIssuerNetworkServiceRecord.getActorDestinationPublicKey(),
                    assetIssuerNetworkServiceRecord.getActorSenderAlias(),
                    assetIssuerNetworkServiceRecord.getActorSenderProfileImage(),
                    assetIssuerNetworkServiceRecord.getActorDestinationType(),
                    assetIssuerNetworkServiceRecord.getAssetNotificationDescriptor(),
                    System.currentTimeMillis(),
                    assetIssuerNetworkServiceRecord.getActorAssetProtocolState(),
                    false,
                    1,
                    assetIssuerNetworkServiceRecord.getBlockchainNetworkType(),
                    assetIssuerNetworkServiceRecord.getResponseToNotificationId()
            );

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Sending message to the destination
                        sendNewMessage(
//                                getProfileSenderToRequestConnection(messageToSend.getActorSenderPublicKey()),
//                                getProfileDestinationToRequestConnection(messageToSend.getActorDestinationPublicKey()),
//                                messageToSend.toJson());
                                getProfileSenderToRequestConnection(
                                        messageToSend.getActorSenderPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(messageToSend.getActorSenderType())
                                ),
                                getProfileDestinationToRequestConnection(
                                        messageToSend.getActorDestinationPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(messageToSend.getActorDestinationType())
                                ),
                                messageToSend.toJson());
                    } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            reportUnexpectedError(e);
            throw new CantAcceptConnectionActorAssetException("ERROR ACTOR ASSET ISSUER NS WHEN ACCEPTING CONNECTION", e, "", "Generic Exception");
        }
    }

    @Override
    public void denyConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToRejectPublicKey)
            throws CantDenyConnectionActorAssetException {

        try {
            final ActorAssetNetworkServiceRecord actorNetworkServiceRecord = incomingNotificationsDao.
                    changeActorAssetNotificationDescriptor(
                            actorAssetToRejectPublicKey,
                            AssetNotificationDescriptor.DENIED,
                            ActorAssetProtocolState.DONE);

            Actors actorSwap = actorNetworkServiceRecord.getActorSenderType();

            actorNetworkServiceRecord.setActorDestinationPublicKey(actorAssetToRejectPublicKey);
            actorNetworkServiceRecord.setActorSenderType(actorNetworkServiceRecord.getActorDestinationType());

            actorNetworkServiceRecord.setActorSenderPublicKey(actorAssetLoggedInPublicKey);
            actorNetworkServiceRecord.setActorDestinationType(actorSwap);


            actorNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.DENIED);

            actorNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_SEND);

            outgoingNotificationDao.createNotification(actorNetworkServiceRecord);

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    // Sending message to the destination
                    try {
                        sendNewMessage(
//                                getProfileSenderToRequestConnection(actorNetworkServiceRecord.getActorSenderPublicKey()),
//                                getProfileDestinationToRequestConnection(actorNetworkServiceRecord.getActorDestinationPublicKey()),
//                                actorNetworkServiceRecord.toJson());
                                getProfileSenderToRequestConnection(
                                        actorNetworkServiceRecord.getActorSenderPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(actorNetworkServiceRecord.getActorSenderType())
                                ),
                                getProfileDestinationToRequestConnection(
                                        actorNetworkServiceRecord.getActorDestinationPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(actorNetworkServiceRecord.getActorDestinationType())
                                ),
                                actorNetworkServiceRecord.toJson());
                    } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            reportUnexpectedError(e);
            throw new CantDenyConnectionActorAssetException("ERROR DENY CONNECTION TO ACTOR ASSET ISSUER", e, "", "Generic Exception");
        }
    }

    @Override
    public void disconnectConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToDisconnectPublicKey)
            throws CantDisconnectConnectionActorAssetException {

        try {
            ActorAssetNetworkServiceRecord assetIssuerNetworkServiceRecord = incomingNotificationsDao.
                    changeActorAssetNotificationDescriptor(
                            actorAssetToDisconnectPublicKey,
                            AssetNotificationDescriptor.DISCONNECTED,
                            ActorAssetProtocolState.PROCESSING_SEND);

            Actors actorSwap = assetIssuerNetworkServiceRecord.getActorSenderType();

            assetIssuerNetworkServiceRecord.setActorSenderPublicKey(actorAssetLoggedInPublicKey);
            assetIssuerNetworkServiceRecord.setActorSenderType(assetIssuerNetworkServiceRecord.getActorDestinationType());

            assetIssuerNetworkServiceRecord.setActorDestinationPublicKey(actorAssetToDisconnectPublicKey);
            assetIssuerNetworkServiceRecord.setActorDestinationType(actorSwap);

            assetIssuerNetworkServiceRecord.setActorSenderAlias(null);

            assetIssuerNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.DISCONNECTED);

            assetIssuerNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_SEND);

            //make message to actor
//            UUID newNotificationID = UUID.randomUUID();
//            AssetNotificationDescriptor assetNotificationDescriptor = AssetNotificationDescriptor.DISCONNECTED;
//            long currentTime = System.currentTimeMillis();
//            ActorAssetProtocolState actorAssetProtocolState = ActorAssetProtocolState.PROCESSING_SEND;

            final ActorAssetNetworkServiceRecord actorNetworkServiceRecord = outgoingNotificationDao.createNotification(
                    UUID.randomUUID(),
                    assetIssuerNetworkServiceRecord.getActorSenderPublicKey(),
                    assetIssuerNetworkServiceRecord.getActorSenderType(),
                    assetIssuerNetworkServiceRecord.getActorDestinationPublicKey(),
                    assetIssuerNetworkServiceRecord.getActorSenderAlias(),
                    assetIssuerNetworkServiceRecord.getActorSenderProfileImage(),
                    assetIssuerNetworkServiceRecord.getActorDestinationType(),
                    assetIssuerNetworkServiceRecord.getAssetNotificationDescriptor(),
                    System.currentTimeMillis(),
                    assetIssuerNetworkServiceRecord.getActorAssetProtocolState(),
                    false,
                    1,
                    assetIssuerNetworkServiceRecord.getBlockchainNetworkType(),
                    null
            );

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    // Sending message to the destination
                    try {
                        sendNewMessage(
//                                getProfileSenderToRequestConnection(actorNetworkServiceRecord.getActorSenderPublicKey()),
//                                getProfileDestinationToRequestConnection(actorNetworkServiceRecord.getActorDestinationPublicKey()),
//                                actorNetworkServiceRecord.toJson());
                                getProfileSenderToRequestConnection(
                                        actorNetworkServiceRecord.getActorSenderPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(actorNetworkServiceRecord.getActorSenderType())
                                ),
                                getProfileDestinationToRequestConnection(
                                        actorNetworkServiceRecord.getActorDestinationPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(actorNetworkServiceRecord.getActorDestinationType())
                                ),
                                actorNetworkServiceRecord.toJson());
                    } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            reportUnexpectedError(e);
            throw new CantDisconnectConnectionActorAssetException("ERROR DISCONNECTING ACTOR ASSET ISSUER ", e, "", "Generic Exception");
        }
    }

    @Override
    public void cancelConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToCancelPublicKey)
            throws CantCancelConnectionActorAssetException {

        try {
            final ActorAssetNetworkServiceRecord assetIssuerNetworkServiceRecord = incomingNotificationsDao.
                    changeActorAssetNotificationDescriptor(
                            actorAssetLoggedInPublicKey,
                            AssetNotificationDescriptor.CANCEL,
                            ActorAssetProtocolState.DONE);

            Actors actorSwap = assetIssuerNetworkServiceRecord.getActorSenderType();

            assetIssuerNetworkServiceRecord.setActorSenderPublicKey(actorAssetLoggedInPublicKey);
            assetIssuerNetworkServiceRecord.setActorSenderType(assetIssuerNetworkServiceRecord.getActorDestinationType());

            assetIssuerNetworkServiceRecord.setActorDestinationPublicKey(actorAssetToCancelPublicKey);
            assetIssuerNetworkServiceRecord.setActorDestinationType(actorSwap);

            assetIssuerNetworkServiceRecord.changeDescriptor(AssetNotificationDescriptor.CANCEL);

            assetIssuerNetworkServiceRecord.changeState(ActorAssetProtocolState.PROCESSING_SEND);

            outgoingNotificationDao.createNotification(assetIssuerNetworkServiceRecord);

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    // Sending message to the destination
                    try {
                        sendNewMessage(
//                                getProfileSenderToRequestConnection(assetIssuerNetworkServiceRecord.getActorSenderPublicKey()),
//                                getProfileDestinationToRequestConnection(assetIssuerNetworkServiceRecord.getActorDestinationPublicKey()),
//                                assetIssuerNetworkServiceRecord.toJson());
                                getProfileSenderToRequestConnection(
                                        assetIssuerNetworkServiceRecord.getActorSenderPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(assetIssuerNetworkServiceRecord.getActorSenderType())
                                ),
                                getProfileDestinationToRequestConnection(
                                        assetIssuerNetworkServiceRecord.getActorDestinationPublicKey(),
                                        NetworkServiceType.UNDEFINED,
                                        platformComponentTypeSelectorByActorType(assetIssuerNetworkServiceRecord.getActorDestinationType())
                                ),
                                assetIssuerNetworkServiceRecord.toJson());
                    } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            reportUnexpectedError(e);
            throw new CantCancelConnectionActorAssetException("ERROR CANCEL CONNECTION TO ACTOR ASSET ISSUER ", e, "", "Generic Exception");
        }
    }

    @Override
    public List<ActorNotification> getPendingNotifications() throws CantGetActorAssetNotificationException {
        try {

            return incomingNotificationsDao.listUnreadNotifications();

        } catch (CantGetActorAssetNotificationException e) {
            reportUnexpectedError(e);
            throw new CantGetActorAssetNotificationException(e, "ACTOR ASSET ISSUER network service", "database corrupted");
        } catch (Exception e) {
            reportUnexpectedError(e);
            throw new CantGetActorAssetNotificationException(e, "ACTOR ASSET ISSUER network service", "Unhandled error.");
        }
    }

    @Override
    public void confirmActorAssetNotification(UUID notificationID) throws CantConfirmActorAssetNotificationException {
        try {

            incomingNotificationsDao.markNotificationAsRead(notificationID);

        } catch (final Exception e) {
            reportUnexpectedError(e);
            throw new CantConfirmActorAssetNotificationException(e, "notificationID: " + notificationID, "Unhandled error.");
        }
    }

    @Override
    public void onComponentRegistered(PlatformComponentProfile platformComponentProfileRegistered) {

        if (platformComponentProfileRegistered.getPlatformComponentType() == PlatformComponentType.ACTOR_ASSET_ISSUER) {
            System.out.print("ACTOR ASSET ISSUER REGISTRADO: " + platformComponentProfileRegistered.getAlias());
        }

        /*
         * If is a actor registered
         */
        if (platformComponentProfileRegistered.getPlatformComponentType() == PlatformComponentType.ACTOR_ASSET_ISSUER &&
                platformComponentProfileRegistered.getNetworkServiceType() == NetworkServiceType.UNDEFINED) {

            Location loca = null;

            ActorAssetIssuer actorAssetIssuerNewRegistered = new AssetIssuerActorRecord(
                    platformComponentProfileRegistered.getIdentityPublicKey(),
                    platformComponentProfileRegistered.getName(),
                    new byte[0],
//                    convertoByteArrayfromString(platformComponentProfileRegistered.getExtraData()),
                    loca);

            System.out.println("ACTOR ASSET ISSUER REGISTRADO en A.N.S - Enviando Evento de Notificacion");

            FermatEvent event = eventManager.getNewEvent(EventType.COMPLETE_ASSET_ISSUER_REGISTRATION_NOTIFICATION);
            event.setSource(EventSource.ACTOR_ASSET_ISSUER);

            ((ActorAssetIssuerCompleteRegistrationNotificationEvent) event).setActorAssetIssuer(actorAssetIssuerNewRegistered);

            eventManager.raiseEvent(event);
        }
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // change message state to process retry later
                reprocessPendingMessage();
            }
        }, 0, reprocessTimer);
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        return communicationNetworkServiceDeveloperDatabaseFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
    }

    @Override
    public List<String> getClassesFullPath() {
        return null;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {

    }

    private void reportUnexpectedError(final Exception e) {
        errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN, e);
    }

    public void buildSendMessage() {


    }

    @Override
    public void sendMessage(DAPMessage dapMessage) throws CantSendMessageException {
        switch (dapMessage.getMessageContent().messageType()) {
            case ASSET_APPROPRIATION:
                assetAppropriated(dapMessage);
                break;
            case EXTENDED_PUBLIC_KEY:
                requestPublicKeyExtended(dapMessage);
                break;
            default:
                throw new CantSendMessageException("This message is not registered or can't be handled by this network service..");
        }
    }

    private void requestPublicKeyExtended(DAPMessage dapMessage) {
//TODO ESTE METODO DEBERIA PODER ADAPTARSE A LA NUEVA FORMA DE NOTIFICATIONS

        final DAPActor actorRedeemPointSender = dapMessage.getActorSender();
        final DAPActor actorIssuerDestination = dapMessage.getActorReceiver();
        final String messageContentIntoJson = dapMessage.toXML();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Sending message to the destination
                try {
                    sendNewMessage(
                            //TODO estos profile se sacaran de la tabla outgoing
//                            getProfileSenderToRequestConnection(actorRedeemPointSender.getActorPublicKey()),
//                            getProfileDestinationToRequestConnection(actorIssuerDestination.getActorPublicKey()),
//                            messageContentIntoJson);
                            getProfileSenderToRequestConnection(
                                    actorRedeemPointSender.getActorPublicKey(),
                                    NetworkServiceType.UNDEFINED,
                                    platformComponentTypeSelectorByActorType(actorRedeemPointSender.getType())
                            ),
                            getProfileDestinationToRequestConnection(
                                    actorIssuerDestination.getActorPublicKey(),
                                    NetworkServiceType.UNDEFINED,
                                    platformComponentTypeSelectorByActorType(actorIssuerDestination.getType())
                            ),
                            messageContentIntoJson);
                } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                    e.printStackTrace();
                }
            }
        });

//        try {
//            CommunicationNetworkServiceLocal communicationNetworkServiceLocal = getCommunicationNetworkServiceConnectionManager().getNetworkServiceLocalInstance(actorIssuerDestination.getActorPublicKey());
//
//            if (true) {
//
////                JsonObject jsonObject = new JsonObject();
////                jsonObject.addProperty(DAP_IMG_ISSUER, Base64.encodeToString(dapMessage.getActorSender().getProfileImage(), Base64.DEFAULT));
////                jsonObject.addProperty(DAP_IMG_ISSUER, Base64.encodeToString(dapMessage.getActorReceiver().getProfileImage(), Base64.DEFAULT));
//
//                String messageContentIntoJson = XMLParser.parseObject(dapMessage);
//
//                if (communicationNetworkServiceLocal != null) {
//
//                    //Send the message
//                    communicationNetworkServiceLocal.sendMessage(
//                            actorRedeemPointSender.getActorPublicKey(),
////                            actorIssuerDestination.getActorPublicKey(),
//                            messageContentIntoJson);
//
//                } else {
//
//                    /*
//                     * Created the message
//                     */
//                    FermatMessage fermatMessage = FermatMessageCommunicationFactory.constructFermatMessage(actorRedeemPointSender.getActorPublicKey(),//Sender
//                            actorIssuerDestination.getActorPublicKey(), //Receiver
//                            messageContentIntoJson,                //Message Content
//                            FermatMessageContentType.TEXT);//Type
//
//                    /*
//                     * Configure the correct status
//                     */
//                    ((FermatMessageCommunication) fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);
//
//                    /*
//                     * Save to the data base table
//                     */
//                    OutgoingMessageDao outgoingMessageDao = getCommunicationNetworkServiceConnectionManager().getOutgoingMessageDao();
//                    outgoingMessageDao.create(fermatMessage);
//
//                    /*
//                     * Create the sender basic profile
//                     */
//                    PlatformComponentProfile sender = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
//                            constructBasicPlatformComponentProfileFactory(
//                                    actorRedeemPointSender.getActorPublicKey(),
//                                    NetworkServiceType.UNDEFINED,
//                                    PlatformComponentType.ACTOR_ASSET_REDEEM_POINT);
//
//                    /*
//                     * Create the receiver basic profile
//                     */
//                    PlatformComponentProfile receiver = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
//                            constructBasicPlatformComponentProfileFactory(
//                                    actorIssuerDestination.getActorPublicKey(),
//                                    NetworkServiceType.UNDEFINED,
//                                    PlatformComponentType.ACTOR_ASSET_ISSUER);
//
//                    /*
//                     * Ask the client to connect
//                     */
//                    getCommunicationNetworkServiceConnectionManager().connectTo(sender, getPlatformComponentProfilePluginRoot(), receiver);
//
//                }
//            } else {
//
//                StringBuffer contextBuffer = new StringBuffer();
//                contextBuffer.append("Plugin ID: " + pluginId);
//                contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//                contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
//                contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//                contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
//                contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//                contextBuffer.append("errorManager: " + errorManager);
//                contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//                contextBuffer.append("eventManager: " + eventManager);
//
//                String context = contextBuffer.toString();
//                String possibleCause = "Asset Issuer Actor Network Service Not Registered";
//
//            }
//        } catch (Exception e) {
//
//            StringBuffer contextBuffer = new StringBuffer();
//            contextBuffer.append("Plugin ID: " + pluginId);
//            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
//            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
//            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//            contextBuffer.append("errorManager: " + errorManager);
//            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
//            contextBuffer.append("eventManager: " + eventManager);
//
//            String context = contextBuffer.toString();
//            String possibleCause = "Cant Request Public Key Extended";
//
//            reportUnexpectedError(e);
////            CantRequestCryptoAddressException pluginStartException = new CantRequestCryptoAddressException(CantStartPluginException.DEFAULT_MESSAGE, null, context, possibleCause);
////            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
//
////            throw pluginStartException;
//        }
    }

    private void assetAppropriated(DAPMessage dapMessage) throws CantSendMessageException {
//TODO ESTE METODO DEBERIA PODER ADAPTARSE A LA NUEVA FORMA DE NOTIFICATIONS
        String context = "Message: " + dapMessage;

        if (!(dapMessage.getActorSender() instanceof ActorAssetUser) || !(dapMessage.getActorReceiver() instanceof ActorAssetIssuer)) {
            throw new CantSendMessageException("One or both actors are from an incorrent type, please check", null, context);
        }

        final DAPActor actorAssetUserSender = dapMessage.getActorSender();
        final DAPActor actorAssetIssuerReceiver = dapMessage.getActorReceiver();
        final String messageContentIntoJson = dapMessage.toXML();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Sending message to the destination
                try {
                    sendNewMessage(
                            //TODO estos profile se sacaran de la tabla outgoing
//                            getProfileSenderToRequestConnection(actorAssetUserSender.getActorPublicKey()),
//                            getProfileDestinationToRequestConnection(actorAssetIssuerReceiver.getActorPublicKey()),
//                            messageContentIntoJson);
                            getProfileSenderToRequestConnection(
                                    actorAssetUserSender.getActorPublicKey(),
                                    NetworkServiceType.UNDEFINED,
                                    platformComponentTypeSelectorByActorType(actorAssetUserSender.getType())
                            ),
                            getProfileDestinationToRequestConnection(
                                    actorAssetIssuerReceiver.getActorPublicKey(),
                                    NetworkServiceType.UNDEFINED,
                                    platformComponentTypeSelectorByActorType(actorAssetIssuerReceiver.getType())
                            ),
                            messageContentIntoJson);
                } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantSendMessageException | InvalidParameterException e) {
                    e.printStackTrace();
                }
            }
        });

//        try {
//            CommunicationNetworkServiceLocal communicationNetworkServiceLocal = getCommunicationNetworkServiceConnectionManager().getNetworkServiceLocalInstance(actorAssetIssuerReceiver.getActorPublicKey());
//
//            if (true) {
//                String messageContentIntoJson = XMLParser.parseObject(dapMessage);
//
//                if (communicationNetworkServiceLocal != null) {
//
//                    //Send the message
//                    communicationNetworkServiceLocal.sendMessage(
//                            actorAssetUserSender.getActorPublicKey(),
////                            actorAssetIssuerReceiver.getActorPublicKey(),
//                            messageContentIntoJson);
//
//                } else {
//                    /*
//                     * Created the message
//                     */
//                    FermatMessage fermatMessage = FermatMessageCommunicationFactory.constructFermatMessage(
//                            actorAssetUserSender.getActorPublicKey(),//Sender
//                            actorAssetIssuerReceiver.getActorPublicKey(), //Receiver
//                            messageContentIntoJson,                //Message Content
//                            FermatMessageContentType.TEXT);//Type
//
//                    /*
//                     * Configure the correct status
//                     */
//                    ((FermatMessageCommunication) fermatMessage).setFermatMessagesStatus(FermatMessagesStatus.PENDING_TO_SEND);
//
//                    /*
//                     * Save to the data base table
//                     */
//                    OutgoingMessageDao outgoingMessageDao = getCommunicationNetworkServiceConnectionManager().getOutgoingMessageDao();
//                    outgoingMessageDao.create(fermatMessage);
//
//                    /*
//                     * Create the sender basic profile
//                     */
//                    PlatformComponentProfile sender = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
//                            constructBasicPlatformComponentProfileFactory(
//                                    actorAssetUserSender.getActorPublicKey(),
//                                    NetworkServiceType.UNDEFINED,
//                                    PlatformComponentType.ACTOR_ASSET_USER);
//
//                    /*
//                     * Create the receiver basic profile
//                     */
//                    PlatformComponentProfile receiver = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
//                            constructBasicPlatformComponentProfileFactory(
//                                    actorAssetIssuerReceiver.getActorPublicKey(),
//                                    NetworkServiceType.UNDEFINED,
//                                    PlatformComponentType.ACTOR_ASSET_ISSUER);
//
//                    /*
//                     * Ask the client to connect
//                     */
//                    getCommunicationNetworkServiceConnectionManager().connectTo(sender, getPlatformComponentProfilePluginRoot(), receiver);
//
//                }
//            } else {
//                throw new CantSendMessageException("The actor asset issuer network service is not registered.");
//            }
//        } catch (Exception e) {
//            throw new CantSendMessageException("Somethind bad happen while trying to send the asset appropriated message", e, context);
//        }
    }

    /**
     * Get the New Received Message List
     *
     * @return List<FermatMessage>
     */
    public List<FermatMessage> getNewReceivedMessageList() throws CantReadRecordDataBaseException {

        Map<String, Object> filters = new HashMap<>();
        filters.put(CommunicationNetworkServiceDatabaseConstants.INCOMING_MESSAGES_STATUS_COLUMN_NAME, MessagesStatus.NEW_RECEIVED.getCode());

        try {
            return getCommunicationNetworkServiceConnectionManager().getIncomingMessageDao().findAll(filters);
        } catch (com.bitdubai.fermat_p2p_api.layer.all_definition.communication.network_services.exceptions.CantReadRecordDataBaseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DAPMessage> getUnreadDAPMessagesByType(DAPMessageType type) throws CantGetDAPMessagesException {
        String context = "Message Type: " + type;
        List<DAPMessage> listToReturn = new ArrayList<>();
        try {
            for (FermatMessage message : getNewReceivedMessageList()) {
                try {
                    DAPMessage dapMessage = (DAPMessage) XMLParser.parseXML(message.getContent(), new DAPMessage());
                    if (dapMessage.getMessageContent().messageType() == type) {
                        listToReturn.add(dapMessage);
                        markAsRead(message);
                    }
                } catch (JsonSyntaxException jsonException) {
                    //This is not a DAPMessage, that's not my business. Let's just continue.
                    continue; //This statement is unnecessary but I'll keep it so people can understand better.
                }
            }
            return listToReturn;
        } catch (CantReadRecordDataBaseException | CantUpdateRecordDataBaseException e) {
            throw new CantGetDAPMessagesException(context, e);
        }
    }

    @Override
    public List<DAPMessage> getUnreadDAPMessageBySubject(DAPMessageSubject subject) throws CantGetDAPMessagesException {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void confirmReception(DAPMessage message) throws CantUpdateMessageStatusException {

    }
}