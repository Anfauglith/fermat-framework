package com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPNetworkService;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_issuer.interfaces.ActorAssetIssuer;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantAcceptConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantActorAssetPendingNotificationsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantAskConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantCancelConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantConfirmActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantDenyConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantDisconnectConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.asset_user.interfaces.ActorAssetUser;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantRegisterActorAssetUserException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantRequestCryptoAddressException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantRequestListActorAssetUserRegisteredException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_user.exceptions.CantSendCryptoAddressException;

import java.util.List;
import java.util.UUID;

/**
 * Created by root on 07/10/15.
 */
public interface AssetUserActorNetworkServiceManager extends FermatManager, DAPNetworkService {

    /**
     * Register the ActorAssetUser in the cloud server like online
     *
     * @param actorAssetUserToRegister
     * @throws CantRegisterActorAssetUserException
     */
    public void registerActorAssetUser(ActorAssetUser actorAssetUserToRegister) throws CantRegisterActorAssetUserException;

    /**
     * Update the ActorAssetUser in the cloud server like online
     *
     * @param actorAssetUserToRegister
     * @throws CantRegisterActorAssetUserException
     */
    public void updateActorAssetUser(ActorAssetUser actorAssetUserToRegister) throws CantRegisterActorAssetUserException;

//    /**
//     * Request a Crypto Address to the ActorAssetUser
//     *
//     * @param actorAssetIssuerSender
//     * @param actorAssetUserDestination
//     * @throws CantRequestCryptoAddressException
//     */
//    public void requestCryptoAddress(ActorAssetIssuer actorAssetIssuerSender, ActorAssetUser actorAssetUserDestination)  throws CantRequestCryptoAddressException;
//
//    /**
//     * Send a Crypto Address to the ActorAssetIssuer
//     *
//     * @param actorAssetUserSender
//     * @param actorAssetIssuerDestination
//     * @param cryptoAddress
//     * @throws CantSendCryptoAddressException
//     */
//    public void sendCryptoAddress(ActorAssetUser actorAssetUserSender, ActorAssetIssuer actorAssetIssuerDestination, CryptoAddress cryptoAddress)  throws CantSendCryptoAddressException;

    /**
     * Get the list of the ActorAssetUser registered
     *
     * @return List<ActorAssetUser>
     */
    public List<ActorAssetUser> getListActorAssetUserRegistered() throws CantRequestListActorAssetUserRegisteredException;

    /**
     * Get the the ActorAssetUser registered by Public key
     *
     * @return List<ActorAssetUser>
     */
    public List<ActorAssetUser> getActorAssetUserRegistered(String actorAssetUserPublicKey) throws CantRequestListActorAssetUserRegisteredException;

    /**
     * The method <code>askConnectionActorAsset</code> sends a connection request to anothe intra user.
     *
     * @param actorAssetLoggedInPublicKey   The logged public key of the
     * @param actorAssetLoggedName          The logged name
     * @param senderType                    The senderType
     * @param actorAssetToAddPublicKey      The actorAssetToAddPublicKey
     * @param actorAssetToAddName           The actorAssetToAddName
     * @param destinationType               The public key of the
     * @param profileImage                  The profile image of the user sending the request
     */
    void askConnectionActorAsset(String actorAssetLoggedInPublicKey,
                                 String actorAssetLoggedName,
                                 Actors senderType,
                                 String actorAssetToAddPublicKey,
                                 String actorAssetToAddName,
                                 Actors destinationType,
                                 byte[] profileImage) throws CantAskConnectionActorAssetException;

    /**
     * The method <code>acceptConnectionActorAsset</code> send an acceptance message of a connection request.
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset accepting the connection request.
     * @param ActorAssetToAddPublicKey    The public key of the actor asset to add
     */
    void acceptConnectionActorAsset(String actorAssetLoggedInPublicKey, String ActorAssetToAddPublicKey) throws CantAcceptConnectionActorAssetException;

    /**
     * The method <code>denyConnectionActorAsset</code> send an rejection message of a connection request.
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset accepting the connection request.
     * @param actorAssetToRejectPublicKey The public key of the actor asset to add
     */
    void denyConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToRejectPublicKey) throws CantDenyConnectionActorAssetException;

    /**
     * The method <coda>disconnectConnectionActorAsset</coda> disconnects and informs the other intra user the disconnecting
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset disconnecting the connection
     * @param actorAssetToDisconnectPublicKey The public key of the user to disconnect
     * @throws CantDisconnectConnectionActorAssetException
     */
    void disconnectConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToDisconnectPublicKey) throws CantDisconnectConnectionActorAssetException;

    /**
     * The method <coda>cancelConnectionActorAsset</coda> cancels and informs the other intra user the cancelling
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset cancelling the connection
     * @param actorAssetToCancelPublicKey The public key of the user to cancel
     * @throws CantCancelConnectionActorAssetException
     */
    void cancelConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToCancelPublicKey) throws CantCancelConnectionActorAssetException;

    /**
     * The method <coda>getPendingNotifications</coda> returns all pending notifications
     * of responses to requests for connection
     *
     * @return List of IntraUserNotification
     */
//    public List<IntraUserNotification> getActorAssetPendingNotifications() throws CantActorAssetPendingNotificationsException;

    /**
     * The method <coda>confirmActorAssetNotification</coda> confirm the pending notification
     */
    public void confirmActorAssetNotification(UUID notificationID) throws CantConfirmActorAssetNotificationException;

}
