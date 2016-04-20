package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.exceptions.CantDeleteRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.exceptions.CantInsertRecordDataBaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.common.network_services.template.exceptions.RecordNotFoundException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.Package;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.PackageDecoder;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.util.PackageEncoder;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.HeadersAttName;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.exception.PackageTypeNotSupportedException;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.caches.ClientsSessionMemoryCache;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.conf.ClientChannelConfigurator;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.FermatWebSocketChannelEndpoint;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.ActorTraceDiscoveryQueryRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInActorRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInClientRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInNetworkServiceRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckInProfileDiscoveryQueryRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutActorRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutClientRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.CheckOutNetworkServiceRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.processors.clients.NearNodeListRequestProcessor;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.CheckedInClient;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ClientsConnectionHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.ClientsRegistrationHistory;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationResult;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.enums.RegistrationType;

import org.jboss.logging.Logger;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.channels.endpoinsts.servers.FermatWebSocketClientChannelServerEndpoint</code> this
 * is a especial channel to manage all the communication between the clients and the node
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 12/11/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
@ServerEndpoint(
        value = "/client-channel",
        configurator = ClientChannelConfigurator.class,
        encoders = {PackageEncoder.class},
        decoders = {PackageDecoder.class}
)
public class FermatWebSocketClientChannelServerEndpoint extends FermatWebSocketChannelEndpoint {

    /**
     * Represent the LOG
     */
    private final Logger LOG = Logger.getLogger(FermatWebSocketClientChannelServerEndpoint.class.getName());

    /**
     * Represent the clientsSessionMemoryCache instance
     */
    private ClientsSessionMemoryCache clientsSessionMemoryCache;

    /**
     * Constructor
     */
    public FermatWebSocketClientChannelServerEndpoint(){
        super();
        this.clientsSessionMemoryCache = ClientsSessionMemoryCache.getInstance();
    }

    /**
     * (non-javadoc)
     *
     * @see FermatWebSocketChannelEndpoint#initPackageProcessorsRegistration()
     */
    @Override
    protected void initPackageProcessorsRegistration(){

        /*
         * Register all messages processor for this
         * channel
         */
        registerMessageProcessor(new ActorTraceDiscoveryQueryRequestProcessor(this));
        registerMessageProcessor(new CheckInClientRequestProcessor(this));
        registerMessageProcessor(new CheckInNetworkServiceRequestProcessor(this));
        registerMessageProcessor(new CheckInActorRequestProcessor(this));
        registerMessageProcessor(new CheckInProfileDiscoveryQueryRequestProcessor(this));
        registerMessageProcessor(new CheckOutActorRequestProcessor(this));
        registerMessageProcessor(new CheckOutClientRequestProcessor(this));
        registerMessageProcessor(new CheckOutNetworkServiceRequestProcessor(this));
        registerMessageProcessor(new NearNodeListRequestProcessor(this));

    }

    /**
     *  Method called to handle a new connection
     *
     * @param session connected
     * @param endpointConfig created
     * @throws IOException
     */
    @OnOpen
    public void onConnect(Session session, EndpointConfig endpointConfig) throws IOException {

        LOG.info(" New connection stablished: " + session.getId());

        /*
         * Get the node identity
         */
        setChannelIdentity((ECCKeyPair) endpointConfig.getUserProperties().get(HeadersAttName.NPKI_ATT_HEADER_NAME));
        endpointConfig.getUserProperties().remove(HeadersAttName.NPKI_ATT_HEADER_NAME);

        /*
         * Get the client public key identity
         */
        String cpki = (String) endpointConfig.getUserProperties().get(HeadersAttName.CPKI_ATT_HEADER_NAME);

        /*
         * Mach the session whit the client public key identity
         */
        clientsSessionMemoryCache.add(cpki, session);

        /*
         * Create a new ClientsConnectionHistory
         */
        ClientsConnectionHistory clientsConnectionHistory = new ClientsConnectionHistory();
        clientsConnectionHistory.setIdentityPublicKey(cpki);
        clientsConnectionHistory.setStatus(ClientsConnectionHistory.STATUS_SUCCESS);

    }

    /**
     * Method called to handle a new message received
     *
     * @param packageReceived new
     * @param session sender
     */
    @OnMessage
    public void newPackageReceived(Package packageReceived, Session session) {

        LOG.info("New message Received");
        LOG.info("Session: " + session.getId() + " packageReceived = " + packageReceived + "");

        try {

            /*
             * Process the new package received
             */
            processMessage(packageReceived, session);

        }catch (PackageTypeNotSupportedException p){
            LOG.warn(p.getMessage());
        }

    }

    /**
     * Method called to handle a connection close
     *
     * @param closeReason message with the details.
     * @param session     closed session.
     */
    @OnClose
    public void onClose(final CloseReason closeReason,
                        final Session     session    ) {

        LOG.info("Closed session : " + session.getId() + " Code: (" + closeReason.getCloseCode() + ") - reason: " + closeReason.getReasonPhrase());

        try {

            /*
             * if the client is checked in, i will delete the record
             * if not, i will register the inconsistency
             */
            String clientPublicKey = clientsSessionMemoryCache.get(session);

            if (getDaoFactory().getCheckedInClientDao().exists(clientPublicKey)) {

                getDaoFactory().getCheckedInClientDao().delete(clientPublicKey);

                insertClientsRegistrationHistory(
                        clientPublicKey,
                        RegistrationResult.SUCCESS,
                        closeReason.toString()
                );

            } else {

                insertClientsRegistrationHistory(
                        clientPublicKey,
                        RegistrationResult.IGNORED,
                        "There's no client registered with the given public key, indicated closed reason: "+closeReason.toString()
                );
            }

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    /**
     * Create a new row into the data base
     *
     * @param publicKey of the client.
     * @param result    of the registration.
     * @param detail    of the registration.
     *
     * @throws CantInsertRecordDataBaseException if something goes wrong.
     */
    private void insertClientsRegistrationHistory(final String             publicKey,
                                                  final RegistrationResult result   ,
                                                  final String             detail   ) throws CantInsertRecordDataBaseException {

        /*
         * Create the ClientsRegistrationHistory
         */
        ClientsRegistrationHistory clientsRegistrationHistory = new ClientsRegistrationHistory();
        clientsRegistrationHistory.setIdentityPublicKey(publicKey);
        clientsRegistrationHistory.setType(RegistrationType.CHECK_OUT);
        clientsRegistrationHistory.setResult(result);
        clientsRegistrationHistory.setDetail(detail);

        /*
         * Save into the data base
         */
        getDaoFactory().getClientsRegistrationHistoryDao().create(clientsRegistrationHistory);
    }


}