package com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.database;

import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_api.layer.world.interfaces.CurrencyHelper;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.exceptions.CantAssociatePairException;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.exceptions.CantListEarningsPairsException;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.exceptions.PairAlreadyAssociatedException;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.interfaces.EarningsPair;
import com.bitdubai.fermat_cbp_api.layer.middleware.matching_engine.utils.WalletReference;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.exceptions.CantGetEarningsPairException;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.exceptions.CantInitializeDatabaseException;
import com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.structure.MatchingEngineMiddlewareEarningsPair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The class <code>com.bitdubai.fermat_cbp_plugin.layer.middleware.matching_engine.developer.bitdubai.version_1.database.MatchingEngineMiddlewareDao</code>
 * contains all the methods that interact with the database.
 * Manages the actor connection requests by storing them on a Database Table.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 09/02/2016.
 *
 * @author lnacosta
 * @version 1.0
 */
public final class MatchingEngineMiddlewareDao {

    private final PluginDatabaseSystem pluginDatabaseSystem;
    private final UUID                 pluginId            ;

    private Database database;

    public MatchingEngineMiddlewareDao(final PluginDatabaseSystem pluginDatabaseSystem,
                                       final UUID                 pluginId            ) {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId             = pluginId            ;
    }

    public void initialize() throws CantInitializeDatabaseException {

        try {

            database = this.pluginDatabaseSystem.openDatabase(
                    this.pluginId,
                    MatchingEngineMiddlewareDatabaseConstants.MATCHING_ENGINE_MIDDLEWARE_DATABASE_NAME
            );

        } catch (final DatabaseNotFoundException e) {

            try {

                MatchingEngineMiddlewareDatabaseFactory cryptoBrokerActorNetworkServiceDatabaseFactory = new MatchingEngineMiddlewareDatabaseFactory(pluginDatabaseSystem);
                database = cryptoBrokerActorNetworkServiceDatabaseFactory.createDatabase(
                        pluginId,
                        MatchingEngineMiddlewareDatabaseConstants.MATCHING_ENGINE_MIDDLEWARE_DATABASE_NAME
                );

            } catch (final CantCreateDatabaseException f) {

                throw new CantInitializeDatabaseException(f, "", "There was a problem and we cannot create the database.");
            } catch (final Exception z) {

                throw new CantInitializeDatabaseException(z, "", "Unhandled Exception.");
            }

        } catch (final CantOpenDatabaseException e) {

            throw new CantInitializeDatabaseException(e, "", "Exception not handled by the plugin, there was a problem and we cannot open the database.");
        } catch (final Exception e) {

            throw new CantInitializeDatabaseException(e, "", "Unhandled Exception.");
        }
    }

    public final EarningsPair associateEarningsPair(final UUID            id             ,
                                                    final Currency        earningCurrency,
                                                    final Currency        linkedCurrency ,
                                                    final WalletReference walletReference) throws CantAssociatePairException     ,
                                                                                                  PairAlreadyAssociatedException {

        try {

            if (existsEarningsPair(earningCurrency, linkedCurrency))
                throw new PairAlreadyAssociatedException("earningCurrency: "+earningCurrency+ " - linkedCurrency: "+linkedCurrency, "The pair already exists in database.");

            final EarningsPair earningsPair = new MatchingEngineMiddlewareEarningsPair(
                    id,
                    earningCurrency,
                    linkedCurrency,
                    walletReference
            );

            final DatabaseTable earningsPairTable = database.getTable(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_ID_COLUMN_NAME);

            DatabaseTableRecord entityRecord = earningsPairTable.getEmptyRecord();

            entityRecord = buildEarningsPairDatabaseRecord(entityRecord, earningsPair);

            earningsPairTable.insertRecord(entityRecord);

            return earningsPair;

        } catch (final CantInsertRecordException e) {

            throw new CantAssociatePairException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot insert the record.");
        } catch (CantGetEarningsPairException cantGetEarningsPairException) {

            throw new CantAssociatePairException(cantGetEarningsPairException, "", "Exception not handled by the plugin, there is a problem in database and i cannot validate if the record already exists.");
        }
    }

    public final List<EarningsPair> listEarningPairs(final WalletReference walletReference) throws CantListEarningsPairsException {

        try {

            final DatabaseTable earningsPairTable = database.getTable(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_ID_COLUMN_NAME);

            earningsPairTable.addStringFilter(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_WALLET_PUBLIC_KEY_COLUMN_NAME, walletReference.getPublicKey(), DatabaseFilterType.EQUAL);

            earningsPairTable.loadToMemory();

            final List<DatabaseTableRecord> records = earningsPairTable.getRecords();

            final List<EarningsPair> earningsPairList = new ArrayList<>();

            for (final DatabaseTableRecord record : records)
                earningsPairList.add(buildEarningPairRecord(record));

            return earningsPairList;

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantListEarningsPairsException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        } catch (final InvalidParameterException e) {

            throw new CantListEarningsPairsException(e, "", "Exception reading records of the table Cannot recognize the codes of the currencies.");
        }
    }

    public boolean existsEarningsPair(final Currency earningCurrency,
                                      final Currency linkedCurrency ) throws CantGetEarningsPairException {

        if (earningCurrency == null)
            throw new CantGetEarningsPairException(null, "", "The earningCurrency is required, can not be null");

        if (linkedCurrency == null)
            throw new CantGetEarningsPairException(null, "", "The linkedCurrency is required, can not be null");

        try {

            final DatabaseTable earningsPairTable = database.getTable(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_ID_COLUMN_NAME);

            earningsPairTable.addFermatEnumFilter(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_COLUMN_NAME     , earningCurrency          , DatabaseFilterType.EQUAL);
            earningsPairTable.addFermatEnumFilter(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_TYPE_COLUMN_NAME, earningCurrency.getType(), DatabaseFilterType.EQUAL);
            earningsPairTable.addFermatEnumFilter(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_COLUMN_NAME      , linkedCurrency           , DatabaseFilterType.EQUAL);
            earningsPairTable.addFermatEnumFilter(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_TYPE_COLUMN_NAME , linkedCurrency.getType() , DatabaseFilterType.EQUAL);

            earningsPairTable.loadToMemory();

            final List<DatabaseTableRecord> records = earningsPairTable.getRecords();

            return !records.isEmpty();

        } catch (final CantLoadTableToMemoryException e) {

            throw new CantGetEarningsPairException(e, "", "Exception not handled by the plugin, there is a problem in database and i cannot load the table.");
        }
    }

    private DatabaseTableRecord buildEarningsPairDatabaseRecord(final DatabaseTableRecord record      ,
                                                                final EarningsPair        earningsPair) {

        record.setUUIDValue  (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_ID_COLUMN_NAME                   , earningsPair.getId()                            );
        record.setFermatEnum (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_COLUMN_NAME     , earningsPair.getEarningCurrency()               );
        record.setFermatEnum (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_TYPE_COLUMN_NAME, earningsPair.getEarningCurrency().getType()     );
        record.setFermatEnum (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_COLUMN_NAME      , earningsPair.getLinkedCurrency()                );
        record.setFermatEnum (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_TYPE_COLUMN_NAME , earningsPair.getLinkedCurrency().getType()      );
        record.setStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_WALLET_PUBLIC_KEY_COLUMN_NAME    , earningsPair.getWalletReference().getPublicKey());

        return record;
    }

    private EarningsPair buildEarningPairRecord(final DatabaseTableRecord record) throws InvalidParameterException {

        UUID   requestId                 = record.getUUIDValue  (MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_ID_COLUMN_NAME                   );
        String earningCurrencyString     = record.getStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_COLUMN_NAME     );
        String earningCurrencyTypeString = record.getStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_EARNING_CURRENCY_TYPE_COLUMN_NAME);
        String linkedCurrencyString      = record.getStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_COLUMN_NAME      );
        String linkedCurrencyTypeString  = record.getStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_LINKED_CURRENCY_TYPE_COLUMN_NAME );
        String walletPublicKey           = record.getStringValue(MatchingEngineMiddlewareDatabaseConstants.EARNING_PAIR_WALLET_PUBLIC_KEY_COLUMN_NAME    );

        Currency earningCurrency = CurrencyHelper.getCurrency(earningCurrencyString, earningCurrencyTypeString);
        Currency linkedCurrency  = CurrencyHelper.getCurrency(linkedCurrencyString , linkedCurrencyTypeString );

        WalletReference walletReference = new WalletReference(walletPublicKey);

        return new MatchingEngineMiddlewareEarningsPair(
                requestId,
                earningCurrency,
                linkedCurrency,
                walletReference
        );
    }

}
