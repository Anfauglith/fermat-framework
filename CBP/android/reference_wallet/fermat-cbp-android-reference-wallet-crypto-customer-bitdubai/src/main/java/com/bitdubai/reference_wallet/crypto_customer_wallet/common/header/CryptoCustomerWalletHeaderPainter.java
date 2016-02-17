package com.bitdubai.reference_wallet.crypto_customer_wallet.common.header;

import android.app.Activity;
import android.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bitdubai.fermat_android_api.engine.HeaderViewPainter;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatWorkerCallBack;
import com.bitdubai.fermat_android_api.ui.util.FermatWorker;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.IndexInfoSummary;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.exceptions.CantGetCryptoCustomerWalletException;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.reference_wallet.crypto_customer_wallet.R;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.adapters.MarketExchangeRatesPageAdapter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.session.CryptoCustomerWalletSession;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by nelson on 17/12/15.
 */
public class CryptoCustomerWalletHeaderPainter implements HeaderViewPainter {
    private final String TAG = "CustomerWalletHeader";

    private final CryptoCustomerWalletSession session;
    private final Activity activity;
    private CryptoCustomerWalletManager walletManager;
    private ErrorManager errorManager;


    public CryptoCustomerWalletHeaderPainter(Activity activity, CryptoCustomerWalletSession session) {
        this.session = session;
        this.activity = activity;

        try {
            errorManager = session.getErrorManager();
            walletManager = session.getModuleManager().getCryptoCustomerWallet(session.getAppPublicKey());
        } catch (CantGetCryptoCustomerWalletException e) {
            if (errorManager != null)
                errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_CUSTOMER_WALLET, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            else
                Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void addExpandableHeader(ViewGroup viewGroup) {
        View container = activity.getLayoutInflater().inflate(R.layout.ccw_header_layout, viewGroup, true);
        ProgressBar progressBar = (ProgressBar) container.findViewById(R.id.ccw_header_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getAndShowMarketExchangeRateData(container, progressBar);
    }

    private void getAndShowMarketExchangeRateData(final View container, final ProgressBar progressBar) {

        FermatWorker fermatWorker = new FermatWorker(activity) {
            @Override
            protected Object doInBackground() throws Exception {
                List<IndexInfoSummary> data = new ArrayList<>();
                data.addAll(walletManager.getProvidersCurrentExchangeRates(session.getAppPublicKey()));

                return data;
            }
        };

        fermatWorker.setCallBack(new FermatWorkerCallBack() {
            @Override
            public void onPostExecute(Object... result) {
                if (result != null && result.length > 0) {
                    List<IndexInfoSummary> summaries = (List<IndexInfoSummary>) result[0];
                    session.setActualExchangeRates(summaries);

                    progressBar.setVisibility(View.GONE);

                    if (summaries.isEmpty()) {
                        FermatTextView noMarketRateTextView = (FermatTextView) container.findViewById(R.id.ccw_no_market_rate);
                        noMarketRateTextView.setVisibility(View.VISIBLE);

                    } else {
                        View marketRateViewPagerContainer = container.findViewById(R.id.ccw_market_rate_view_pager_container);
                        marketRateViewPagerContainer.setVisibility(View.VISIBLE);

                        final FragmentManager fragmentManager = activity.getFragmentManager();
                        MarketExchangeRatesPageAdapter pageAdapter = new MarketExchangeRatesPageAdapter(fragmentManager, summaries);

                        ViewPager viewPager = (ViewPager) container.findViewById(R.id.ccw_exchange_rate_view_pager);
                        viewPager.setOffscreenPageLimit(3);
                        viewPager.setAdapter(pageAdapter);

                        LinePageIndicator indicator = (LinePageIndicator) container.findViewById(R.id.ccw_exchange_rate_view_pager_indicator);
                        indicator.setViewPager(viewPager);
                    }
                }
            }

            @Override
            public void onErrorOccurred(Exception ex) {
                progressBar.setVisibility(View.GONE);

                FermatTextView noMarketRateTextView = (FermatTextView) container.findViewById(R.id.ccw_no_market_rate);
                noMarketRateTextView.setVisibility(View.VISIBLE);

                ErrorManager errorManager = session.getErrorManager();
                if (errorManager != null)
                    errorManager.reportUnexpectedWalletException(Wallets.CBP_CRYPTO_BROKER_WALLET,
                            UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, ex);
                else
                    Log.e(TAG, ex.getMessage(), ex);
            }
        });

        Executors.newSingleThreadExecutor().execute(fermatWorker);
    }
}
