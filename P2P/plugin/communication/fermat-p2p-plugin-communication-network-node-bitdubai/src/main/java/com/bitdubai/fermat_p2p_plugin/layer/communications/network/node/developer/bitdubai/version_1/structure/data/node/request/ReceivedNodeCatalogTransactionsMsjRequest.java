/*
 * @#ReceivedNodeCatalogTransactionsMsjRequest.java - 2016
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.data.PackageContent;
import com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.entities.NodesCatalogTransaction;

import java.util.List;

/**
 * The Class <code>com.bitdubai.fermat_p2p_plugin.layer.communications.network.node.developer.bitdubai.version_1.structure.data.node.request.ReceivedNodeCatalogTransactionsMsjRequest</code>
 * <p/>
 * Created by Roberto Requena - (rart3001@gmail.com) on 05/04/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class ReceivedNodeCatalogTransactionsMsjRequest extends PackageContent {

    /**
     * Represent the list of transactions
     */
    private List<NodesCatalogTransaction> nodesCatalogTransactions;

    /**
     * Constructor with parameters
     * @param nodesCatalogTransactions
     */
    public ReceivedNodeCatalogTransactionsMsjRequest(List<NodesCatalogTransaction> nodesCatalogTransactions) {
        super();
        this.nodesCatalogTransactions = nodesCatalogTransactions;
    }

    /**
     * Get the list of node catalog nodesCatalogTransactions
     * @return nodesCatalogTransactions
     */
    public List<NodesCatalogTransaction> getNodesCatalogTransactions() {
        return nodesCatalogTransactions;
    }


}
