package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.respond;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.client.respond.MsgRespond;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.profiles.NodeProfile;
import com.google.gson.Gson;

/**
 * The Class <code>com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.node.respond.UpdateNodeInCatalogMsjRespond</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class UpdateNodeInCatalogMsjRespond extends MsgRespond {

    /**
     * Represent the nodeProfileAdded
     */
    private NodeProfile nodeProfileAdded;

    /**
     * Represent the alreadyExists
     */
    private Boolean alreadyExists;

    /**
     * Constructor with parameters
     *
     * @param status
     * @param details
     */
    public UpdateNodeInCatalogMsjRespond(STATUS status, String details, NodeProfile nodeProfileAdded, Boolean alreadyExists) {
        super(status, details);
        this.nodeProfileAdded = nodeProfileAdded;
        this.alreadyExists = alreadyExists;
    }

    /**
     * Get the NodeProfileAdded value
     *
     * @return NodeProfileAdded
     */
    public NodeProfile getNodeProfileAdded() {
        return nodeProfileAdded;
    }

    /**
     * Get the AlreadyExists value
     *
     * @return AlreadyExists
     */
    public Boolean getAlreadyExists() {
        return alreadyExists;
    }

    public static UpdateNodeInCatalogMsjRespond parseContent(String content) {

        return new Gson().fromJson(content, UpdateNodeInCatalogMsjRespond.class);
    }
}
