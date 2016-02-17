package com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDaoTest;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_cbp_plugin.layer.business_transaction.customer_online_payment.developer.bitdubai.version_1.database.CustomerOnlinePaymentBusinessTransactionDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import java.util.UUID;

/**
 * Created by alexander jimenez (alex_jimenez76@hotmail.com) on 05/02/16.
 */
public class getPendingEventsTest {
    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;
    @Mock
    private Database mockDatabase;
    @Mock
    DatabaseTable databaseTable;
    private UUID testId;
    private CustomerOnlinePaymentBusinessTransactionDao customerOnlinePaymentBusinessTransactionDao;
    private CustomerOnlinePaymentBusinessTransactionDao customerOnlinePaymentBusinessTransactionDaoSpy;


    @Before
    public void setup()throws Exception{
        testId = UUID.randomUUID();
        customerOnlinePaymentBusinessTransactionDao = new CustomerOnlinePaymentBusinessTransactionDao(mockPluginDatabaseSystem,testId, mockDatabase);
        customerOnlinePaymentBusinessTransactionDaoSpy = PowerMockito.spy(customerOnlinePaymentBusinessTransactionDao);
        MockitoAnnotations.initMocks(this);
        PowerMockito.doReturn(databaseTable).when(customerOnlinePaymentBusinessTransactionDaoSpy, "getDatabaseEventsTable");
    }

    @Test
    public void getPendingEventsTest_Should_Return_Empty_List() throws Exception{
        customerOnlinePaymentBusinessTransactionDaoSpy.getPendingEvents();
    }
    @Test(expected = Exception.class)
    public void getPendingEventsTest_Should_Throw_Exception() throws Exception{
        customerOnlinePaymentBusinessTransactionDao.getPendingEvents();
    }
}
