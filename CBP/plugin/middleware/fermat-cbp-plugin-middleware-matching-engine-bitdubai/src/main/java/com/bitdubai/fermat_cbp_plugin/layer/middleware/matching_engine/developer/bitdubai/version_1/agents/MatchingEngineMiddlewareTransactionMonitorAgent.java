package com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.agents;

import com.bitdubai.fermat_api.FermatAgent;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.AgentStatus;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.database.MatchingEngineMiddlewareDao;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

/**
 * The agent <code>com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.agents.MatchingEngineMiddlewareTransactionMonitorAgent</code>
 * has the responsibility of monitor all the new transactions in the wallets registered in the plug-in and get them as Input Transactions.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/02/2016.
 *
 * @author lnacosta
 * @version 1.0
 */
public final class MatchingEngineMiddlewareTransactionMonitorAgent extends FermatAgent {


    private static final int SLEEP = 5000;

    private Thread agentThread;

    private final ErrorManager                errorManager          ;
    private final MatchingEngineMiddlewareDao dao                   ;
    private final PluginVersionReference      pluginVersionReference;

    public MatchingEngineMiddlewareTransactionMonitorAgent(final ErrorManager                errorManager          ,
                                                           final MatchingEngineMiddlewareDao dao                   ,
                                                           final PluginVersionReference      pluginVersionReference) {

        this.errorManager           = errorManager          ;
        this.dao                    = dao                   ;
        this.pluginVersionReference = pluginVersionReference;

        this.agentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning())
                    process();
            }
        });
    }

    @Override
    public void start() {
        this.agentThread.start();
        this.status = AgentStatus.STARTED;
    }

    @Override
    public void stop() {

        if(isRunning())
            this.agentThread.interrupt();

        this.status = AgentStatus.STOPPED;
    }

    public void process() {

        while (isRunning()) {

            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException interruptedException) {
                cleanResources();
                return;
            }

            doTheMainTask();

            if (agentThread.isInterrupted()) {
                cleanResources();
                return;
            }
        }
    }

    private void doTheMainTask() {

        // create a null list of crypto broker transactions

        // get the list of crypto broker transactions from the crypto broker wallet plug-in

        // save each one of the transactions in database as "InputTransaction's".

    }


    private void cleanResources() {
        /**
         * Disconnect from database and explicitly set all references to null.
         */
    }

}
