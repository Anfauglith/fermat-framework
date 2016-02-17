package com.bitdubai.reference_wallet.crypto_customer_wallet.fragments.open_negotiation_details;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.FiatCurrency;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.pip_engine.interfaces.ResourceProviderManager;
import com.bitdubai.fermat_api.layer.world.interfaces.Currency;
import com.bitdubai.fermat_bnk_api.layer.bnk_wallet.bank_money.interfaces.BankAccountNumber;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseStatus;
import com.bitdubai.fermat_cbp_api.all_definition.enums.ClauseType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.MoneyType;
import com.bitdubai.fermat_cbp_api.all_definition.enums.NegotiationStepStatus;
import com.bitdubai.fermat_cbp_api.all_definition.identity.ActorIdentity;
import com.bitdubai.fermat_cbp_api.layer.negotiation_transaction.customer_broker_update.exceptions.CantCancelNegotiationException;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.exceptions.CouldNotCancelNegotiationException;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.ClauseInformation;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.CustomerBrokerNegotiationInformation;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.common.interfaces.MerchandiseExchangeRate;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletManager;
import com.bitdubai.fermat_cbp_api.layer.wallet_module.crypto_customer.interfaces.CryptoCustomerWalletModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.reference_wallet.crypto_customer_wallet.R;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.adapters.OpenNegotiationAdapter;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.dialogs.SingleTextDialog;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.dialogs.ClauseDateTimeDialog;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.dialogs.ClauseTextDialog;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.holders.open_negotiation.ClauseViewHolder;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.holders.open_negotiation.FooterViewHolder;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.models.BrokerCurrencyQuotation;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.models.BrokerCurrencyQuotationImpl;
import com.bitdubai.reference_wallet.crypto_customer_wallet.common.models.TestData;
import com.bitdubai.reference_wallet.crypto_customer_wallet.fragments.common.SimpleListDialogFragment;
import com.bitdubai.reference_wallet.crypto_customer_wallet.session.CryptoCustomerWalletSession;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Modified by Yordin Alayn 22.01.16
 */
//FermatWalletExpandableListFragment<GrouperItem>
public class OpenNegotiationDetailsFragment extends AbstractFermatFragment<CryptoCustomerWalletSession, ResourceProviderManager>
//public class OpenNegotiationDetailsFragment extends FermatWalletExpandableListFragment<CryptoCustomerWalletSession, ResourceProviderManager>
        implements FooterViewHolder.OnFooterButtonsClickListener, ClauseViewHolder.Listener/*, ClauseViewHolder.ListenerConfirm*/{

    private static final String TAG = "OpenNegotiationFrag";

    private ImageView brokerImage;
    private FermatTextView sellingDetails;
    private FermatTextView exchangeRateSummary;
    private FermatTextView brokerName;
    private RecyclerView recyclerView;
    private boolean valuesHasChanged;
    private ClauseType clauseInformationType;

    private CryptoCustomerWalletManager walletManager;
    private ErrorManager errorManager;
    private OpenNegotiationAdapter adapter;
    private CustomerBrokerNegotiationInformation negotiationInfo;
    private BrokerCurrencyQuotation brokerCurrencyQuotation;
    private List<MerchandiseExchangeRate> quotes;

    private ArrayList<String> paymentMethods;
    private ArrayList<String> receptionMethods;
    private ArrayList<Currency> currencies; // test data
    private List<BankAccountNumber> bankAccountList;
    private List<String> locationList;
    private List<BrokerCurrencyQuotationImpl> brokerCurrencyQuotationlist;

    public OpenNegotiationDetailsFragment() {
        // Required empty public constructor
    }

    public static OpenNegotiationDetailsFragment newInstance() {
        return new OpenNegotiationDetailsFragment();
    }


    /*FermatWalletExpandableListFragment IMPLEMENTATION*/
    /*
    @Override
    public ExpandableRecyclerAdapter getAdapter(){
        return null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(){
        return null;
    }

    @Override
    public void onRefresh(){

    }

    @Override
    public void onPostExecute(Object... result){

    }

    public void onErrorOccurred(Exception ex){

    }
    */
    /*END FermatWalletExpandableListFragment IMPLEMENTATION*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        currencies = new ArrayList<>();
        currencies.add(FiatCurrency.VENEZUELAN_BOLIVAR);
        currencies.add(FiatCurrency.US_DOLLAR);
        currencies.add(FiatCurrency.ARGENTINE_PESO);
        currencies.add(CryptoCurrency.BITCOIN);
        currencies.add(CryptoCurrency.LITECOIN);

        try {

            CryptoCustomerWalletModuleManager moduleManager = appSession.getModuleManager();
            walletManager = moduleManager.getCryptoCustomerWallet(appSession.getAppPublicKey());
            errorManager = appSession.getErrorManager();
            valuesHasChanged = false;

            //LIST OF MAKET RATE OF BROKER
            brokerCurrencyQuotationlist = TestData.getMarketRateForCurrencyTest();
            brokerCurrencyQuotation = new BrokerCurrencyQuotation(appSession.getActualExchangeRates());

            Object data = appSession.getData(CryptoCustomerWalletSession.BANK_ACCOUNT_LIST);
            if (data == null) {
                bankAccountList = new ArrayList<>();
                appSession.setData(CryptoCustomerWalletSession.BANK_ACCOUNT_LIST, bankAccountList);
            } else {
                bankAccountList = (List<BankAccountNumber>) data;
            }

            data = appSession.getData(CryptoCustomerWalletSession.LOCATION_LIST);
            if (data == null) {
                locationList = new ArrayList<>();
                appSession.setData(CryptoCustomerWalletSession.LOCATION_LIST, locationList);
            } else {
                locationList = (List<String>) data;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.ccw_fragment_open_negotiation_details_activity, container, false);

        configureToolbar();
        initViews(layout);
        bindData();

        return layout;
    }

    @Override
    public void onConfirmCLicked(final ClauseInformation clause){
        if ((clause.getStatus().getCode() != NegotiationStepStatus.ACCEPTED.getCode()) && (clause.getStatus().getCode() != NegotiationStepStatus.CHANGED.getCode())){

            if(clauseInformationType != clause.getType())
                valuesHasChanged = false;

            if (valuesHasChanged)
                putClause(clause, ClauseStatus.CHANGED);
            else
                putClause(clause, ClauseStatus.ACCEPTED);

            valuesHasChanged = false;
        }

        adapter.changeDataSet(negotiationInfo);
    }

    @Override
    public void onClauseCLicked(final Button triggerView, final ClauseInformation clause, final int position) {
        SimpleListDialogFragment dialogFragment;
        final ClauseType type = clause.getType();
        ClauseTextDialog clauseTextDialog = null;
        ClauseDateTimeDialog clauseDateTimeDialog = null;
        clauseInformationType = clause.getType();

        switch (type) {
            case BROKER_CURRENCY:
//                List<Currency> currencies = getCurrenciesFromQuotes(quotes);
                dialogFragment = new SimpleListDialogFragment<>();
                dialogFragment.configure("Currencies", currencies);
                dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<Currency>() {
                    @Override
                    public void onItemSelected(Currency newValue) { actionListenerBrokerCurrency(clause, newValue); }
                });
                dialogFragment.show(getFragmentManager(), "brokerCurrenciesDialog");
                break;

            case BROKER_CURRENCY_QUANTITY:
                clauseTextDialog = new ClauseTextDialog(getActivity(), appSession, appResourcesProviderManager);
                clauseTextDialog.setAcceptBtnListener(new ClauseTextDialog.OnClickAcceptListener() {
                    @Override
                    public void onClick(String newValue) { actionListenerBrokerCurrencyQuantity(clause, newValue); }
                });

                clauseTextDialog.setEditTextValue(clause.getValue());
                clauseTextDialog.configure(
                        type.equals(ClauseType.EXCHANGE_RATE) ? R.string.ccw_your_exchange_rate : R.string.ccw_amount_to_buy,
                        type.equals(ClauseType.EXCHANGE_RATE) ? R.string.amount : R.string.ccw_value);
                clauseTextDialog.show();
                break;

            case CUSTOMER_PAYMENT_METHOD:
                dialogFragment = new SimpleListDialogFragment<>();
                dialogFragment.configure("Payment Methods", paymentMethods);
                dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(String newValue) { actionListenerCustomerPaymentMethod(clause, newValue); }
                });
                dialogFragment.show(getFragmentManager(), "paymentMethodsDialog");
                break;

            case BROKER_PAYMENT_METHOD:
                dialogFragment = new SimpleListDialogFragment<>();
                dialogFragment.configure("Reception Methods", receptionMethods);
                dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(String newValue) { actionListenerBrokerPaymentMethod(clause, newValue); }
                });
                dialogFragment.show(getFragmentManager(), "receptionMethodsDialog");
                break;

            case CUSTOMER_DATE_TIME_TO_DELIVER:
                clauseDateTimeDialog = new ClauseDateTimeDialog(getActivity(), Long.valueOf(clause.getValue()));
                if(triggerView.getId() == R.id.ccw_date_value)
                    clauseDateTimeDialog.getDateDialog();
                else
                    clauseDateTimeDialog.getTimeDialog();

                clauseDateTimeDialog.setAcceptBtnListener(new ClauseDateTimeDialog.OnClickAcceptListener() {
                    @Override
                    public void getDate(long newValue) { actionListenerDatetime(clause, String.valueOf(newValue)); }
                });

                break;

            case BROKER_DATE_TIME_TO_DELIVER:
                clauseDateTimeDialog = new ClauseDateTimeDialog(getActivity(),Long.valueOf(clause.getValue()));
                if(triggerView.getId() == R.id.ccw_date_value)
                    clauseDateTimeDialog.getDateDialog();
                else
                    clauseDateTimeDialog.getTimeDialog();

                clauseDateTimeDialog.setAcceptBtnListener(new ClauseDateTimeDialog.OnClickAcceptListener() {
                    @Override
                    public void getDate(long newValue) { actionListenerDatetime(clause, String.valueOf(newValue));}
                });

                break;

            case CUSTOMER_BANK_ACCOUNT:

                if(bankAccountList.size() > 0 ) {
                    dialogFragment = new SimpleListDialogFragment<>();
                    dialogFragment.configure("bankAccount", bankAccountList);
                    dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<BankAccountNumber>() {
                        @Override
                        public void onItemSelected(BankAccountNumber newValue) {
                        putClause(clause, newValue.getAccount(), getStatusClauseChange(clause.getStatus().getCode()));
                        adapter.changeDataSet(negotiationInfo);
                        }
                    });
                    dialogFragment.show(getFragmentManager(), "bankAccountDialog");
                }else{
                    Toast.makeText(getActivity(), "The Bank Account List is Empty. Add Your Bank Account in the Settings Wallet.", Toast.LENGTH_LONG).show();
                }
                break;

            case CUSTOMER_PLACE_TO_DELIVER:
                if(locationList.size() > 0) {
                    dialogFragment = new SimpleListDialogFragment<>();
                    dialogFragment.configure("placeToDelivery", locationList);
                    dialogFragment.setListener(new SimpleListDialogFragment.ItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(String newValue) {
                            putClause(clause, newValue, getStatusClauseChange(clause.getStatus().getCode()));
                            adapter.changeDataSet(negotiationInfo);
                        }
                    });
                    dialogFragment.show(getFragmentManager(), "placeToDeliveryDialog");
                }else{
                    Toast.makeText(getActivity(), "The Locations List is Empty. Add Your Locations in the Settings Wallet.", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                clauseTextDialog = new ClauseTextDialog(getActivity(), appSession, appResourcesProviderManager);
                clauseTextDialog.setAcceptBtnListener(new ClauseTextDialog.OnClickAcceptListener() {
                    @Override
                    public void onClick(String newValue) { actionListener(clause, newValue); }
                });

                clauseTextDialog.setEditTextValue(clause.getValue());
                clauseTextDialog.configure(
                        type.equals(ClauseType.EXCHANGE_RATE) ? R.string.ccw_your_exchange_rate : R.string.ccw_amount_to_buy,
                        type.equals(ClauseType.EXCHANGE_RATE) ? R.string.amount : R.string.ccw_value);

                clauseTextDialog.show();
                break;
        }
    }

    @Override
    public void onSendButtonClicked() {
        Map<ClauseType, ClauseInformation> mapClauses = negotiationInfo.getClauses();
        String contClause = Integer.toString(getTotalSteps(mapClauses));
        Toast.makeText(getActivity(), "PROCESS SEND. TOT: " + contClause, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddNoteButtonClicked() {
        changeActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_OPEN_NEGOTIATION_ADD_NOTE, this.appSession.getAppPublicKey());
    }

    @Override
    public void onCancelNegotiationClicked() {

        SingleTextDialog singleTextDialog = null;

        singleTextDialog = new SingleTextDialog(getActivity(), appSession, appResourcesProviderManager);
        singleTextDialog.setAcceptBtnListener(new SingleTextDialog.OnClickAcceptListener() {
            @Override
            public void onClick(String newValue) {

                try {

                    CustomerBrokerNegotiationInformation negotiation = walletManager.cancelNegotiation(negotiationInfo,newValue);
                    Toast.makeText(getActivity(), "NEGOTIATION IS CANCELATED. REASON: " + negotiation.getCancelReason(), Toast.LENGTH_LONG).show();
                    changeActivity(Activities.CBP_CRYPTO_CUSTOMER_WALLET_HOME, appSession.getAppPublicKey());

                } catch (CouldNotCancelNegotiationException | CantCancelNegotiationException e){
                    Toast.makeText(getActivity(), "ERROR IN CANCELLATION OF NEGOTIATION: "+ e.DEFAULT_MESSAGE, Toast.LENGTH_LONG).show();
                }

            }
        });

        singleTextDialog.setEditTextValue("");
        singleTextDialog.configure(R.string.ccw_cancellation_negotiation, R.string.ccw_cancellation_reason_title);
        singleTextDialog.show();

    }
    /*-------------------------------------------------------------------------------------------------
                                            VIEW METHODS
    ---------------------------------------------------------------------------------------------------*/

    //VIEW TOOLBAR
    private void configureToolbar() {

        Toolbar toolbar = getToolbar();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setBackground(getResources().getDrawable(R.drawable.ccw_action_bar_gradient_colors, null));
        else
            toolbar.setBackground(getResources().getDrawable(R.drawable.ccw_action_bar_gradient_colors));

        toolbar.setTitleTextColor(Color.WHITE);
        if (toolbar.getMenu() != null) toolbar.getMenu().clear();

    }

    //VIEW INIT
    private void initViews(View rootView) {

        brokerImage = (ImageView) rootView.findViewById(R.id.ccw_customer_image);
        brokerName = (FermatTextView) rootView.findViewById(R.id.ccw_broker_name);
        sellingDetails = (FermatTextView) rootView.findViewById(R.id.ccw_selling_summary);
        exchangeRateSummary = (FermatTextView) rootView.findViewById(R.id.ccw_buying_exchange_rate);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.ccw_open_negotiation_details_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }

    //VIEW DATE
    private void bindData() {

        quotes = appSession.getQuotes();

        long timeInMillisVal = System.currentTimeMillis();
        String timeInMillisStr = String.valueOf(timeInMillisVal);

        negotiationInfo = (CustomerBrokerNegotiationInformation) appSession.getData(CryptoCustomerWalletSession.NEGOTIATION_DATA);
        ActorIdentity broker = negotiationInfo.getBroker();
        Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        //CLAUSES DATE
        String merchandise = clauses.get(ClauseType.CUSTOMER_CURRENCY).getValue();
        String exchangeAmount = clauses.get(ClauseType.EXCHANGE_RATE).getValue();
        String payment = clauses.get(ClauseType.BROKER_CURRENCY).getValue();
        String amount = clauses.get(ClauseType.CUSTOMER_CURRENCY_QUANTITY).getValue();
        Drawable brokerImg = getImgDrawable(broker.getProfileImage());

        //LIST MERCHANDISE TYPE
        paymentMethods = getPaymentMethod(payment);

        //LIST PAYMENT METHOD TYPE
        receptionMethods = getPaymentMethod(merchandise);

        //VALUE DEFAULT CUSTOMER PAYMENT METHOD
        if(clauses.get(ClauseType.CUSTOMER_PAYMENT_METHOD) == null){
            putClause(ClauseType.CUSTOMER_PAYMENT_METHOD,paymentMethods.get(0));
        }

        //VALUE DEFAULT BROKER PAYMENT METHOD
        if(clauses.get(ClauseType.BROKER_PAYMENT_METHOD) == null) {
            putClause(ClauseType.BROKER_PAYMENT_METHOD, receptionMethods.get(0));
        }

        //VALUE DEFAULT INFO PAYMENT
        putPaymentInfo(clauses);

        //VALUE DEFAULT INFO RECEPTION
        putReceptionInfo(clauses);

        if(clauses.get(ClauseType.CUSTOMER_DATE_TIME_TO_DELIVER) == null)
            putClause(ClauseType.CUSTOMER_DATE_TIME_TO_DELIVER,timeInMillisStr);

        if(clauses.get(ClauseType.BROKER_DATE_TIME_TO_DELIVER) == null)
            putClause(ClauseType.BROKER_DATE_TIME_TO_DELIVER,timeInMillisStr);

        brokerImage.setImageDrawable(brokerImg);
        brokerName.setText(broker.getAlias());
        sellingDetails.setText(getResources().getString(R.string.ccw_selling_details, amount, merchandise));
        exchangeRateSummary.setText(getResources().getString(R.string.ccw_exchange_rate_summary, merchandise, exchangeAmount, payment));
        
        adapter = new OpenNegotiationAdapter(getActivity(), negotiationInfo);
        adapter.setClauseListener(this);
        adapter.setFooterListener(this);
        adapter.setMarketRateList(appSession.getActualExchangeRates());

        recyclerView.setAdapter(adapter);
    }

    private Drawable getImgDrawable(byte[] customerImg) {

        Resources res = getResources();

        if (customerImg != null && customerImg.length > 0)
            return ImagesUtils.getRoundedBitmap(res, customerImg);

        return ImagesUtils.getRoundedBitmap(res, R.drawable.person);

    }

    private int getTotalSteps(Map<ClauseType, ClauseInformation> mapClauses){

        int cont = 0;
        if(mapClauses != null)
            for (Map.Entry<ClauseType, ClauseInformation> clauseInformation : mapClauses.entrySet()) cont++;

        return cont;

    }
    /*-------------------------------------------------------------------------------------------------
                                            VIEW METHODS
    ---------------------------------------------------------------------------------------------------*/

    /*-------------------------------------------------------------------------------------------------
                                            ACTION LISTENER
    ---------------------------------------------------------------------------------------------------*/

    //ACTION LISTENER FOR CUSTOMER PAYMENT METHOD
    private void actionListenerCustomerPaymentMethod(ClauseInformation clause, String selectedItem){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        //VALIDATE CHANGE
        validateChange(clause.getValue(), selectedItem);

        //ADD SELECTED ITEN
        putClause(clause, selectedItem, getStatusClauseChange(clause.getStatus().getCode()));

        //ADD CLAUSE OF THE INFO THE PAYMENT
        putPaymentInfo(clauses);

        adapter.changeDataSet(negotiationInfo);
    }

    //ACTION LISTENER FOR CUSTOMER PAYMENT METHOD
    private void actionListenerBrokerPaymentMethod(ClauseInformation clause, String selectedItem){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        //VALIDATE CHANGE
        validateChange(clause.getValue(), selectedItem);

        //ADD SELECTED ITEN
        putClause(clause, selectedItem, getStatusClauseChange(clause.getStatus().getCode()));

        //ADD CLAUSE OF THE INFO THE PAYMENT
        putReceptionInfo(clauses);

        adapter.changeDataSet(negotiationInfo);
    }

    //ACTION LISTENER FOR CLAUSE BROKER CURRNCY QUANTTY
    private void actionListenerBrokerCurrencyQuantity(ClauseInformation clause, String newValue){

        if(validateExchangeRate()) {

            final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

            //VALIDATE CHANGE
            validateChange(clause.getValue(), newValue);

            //ASIGNAMENT NEW VALUE
            newValue = getDecimalFormat(getBigDecimal(newValue));
            putClause(clause, newValue, getStatusClauseChange(clause.getStatus().getCode()));

            //CALCULATE CUSTOMER CURRENCY QUANTITY
            final BigDecimal exchangeRate   = getBigDecimal(clauses.get(ClauseType.EXCHANGE_RATE).getValue());
            final BigDecimal amountToPay    = getBigDecimal(clauses.get(ClauseType.BROKER_CURRENCY_QUANTITY).getValue());
            final BigDecimal amountToBuy    = amountToPay.divide(exchangeRate,8, RoundingMode.HALF_UP);

            //ASIGNAMENT CUSTOMER CURRENCY QUANTITY
            final String amountToBuyStr = getDecimalFormat(amountToBuy);
            final ClauseInformation brokerCurrencyQuantity = clauses.get(ClauseType.CUSTOMER_CURRENCY_QUANTITY);
            putClause(brokerCurrencyQuantity, amountToBuyStr);

            adapter.changeDataSet(negotiationInfo);

        }

    }

    //ACTION LISTENER FOR CLAUSE BROKER CURRENCY
    private void actionListenerBrokerCurrency(ClauseInformation clause, Currency selectedItem){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();
        String payment = selectedItem.getCode();
        String merchandise = clauses.get(ClauseType.CUSTOMER_CURRENCY).getValue();

        if (merchandise != payment) {

            //VALIDATE CHANGE
            validateChange(clause.getValue(), payment);

            //ASIGNAMENT NEW VALUE
            putClause(clause, payment, getStatusClauseChange(clause.getStatus().getCode()));

            //UPDATE LIST OF PAYMENT
            paymentMethods = getPaymentMethod(payment);

            //UPDATE CLAUSE PAYMENT METHOD
            putClause(ClauseType.CUSTOMER_PAYMENT_METHOD,paymentMethods.get(0));

            //UPDATE CLAUSE OF THE INFO THE PAYMENT
            putPaymentInfo(clauses);

            //GET MARKET RATE
            String brokerMarketRate = brokerCurrencyQuotation.getExchangeRate(merchandise,payment);

            if (brokerMarketRate != null) {

                BigDecimal exchangeRate = getBigDecimal(brokerMarketRate);

                //CALCULATE NEW PAY
                final BigDecimal amountToBuy = getBigDecimal(clauses.get(ClauseType.CUSTOMER_CURRENCY_QUANTITY).getValue());
                final BigDecimal amountToPay = amountToBuy.multiply(exchangeRate);

                //ASINAMENT NEW EXCHANGE RATE
                final String amountToexchangeRateStr = getDecimalFormat(exchangeRate);
                final ClauseInformation exchangeRateClause = clauses.get(ClauseType.EXCHANGE_RATE);
                putClause(exchangeRateClause, amountToexchangeRateStr);

                //ASIGNAMENT NEW PAY
                final String amountToPayStr = getDecimalFormat(amountToPay);
                final ClauseInformation brokerCurrencyQuantityClause = clauses.get(ClauseType.BROKER_CURRENCY_QUANTITY);
                putClause(brokerCurrencyQuantityClause, amountToPayStr);

            } else {
                Toast.makeText(getActivity(), "The exchange rate not fount for the currency to pay selected.", Toast.LENGTH_LONG).show();
            }

            adapter.changeDataSet(negotiationInfo);

        }else{
            Toast.makeText(getActivity(), "The currency to pay is equal to currency buy.", Toast.LENGTH_LONG).show();
        }

    }

    //ACCTION LISTENER FOR CLAUSE DATETIME
    private void actionListenerDatetime(ClauseInformation clause, String newValue){

        validateChange(clause.getValue(), String.valueOf(newValue));
        putClause(clause, String.valueOf(newValue), getStatusClauseChange(clause.getStatus().getCode()));
        adapter.changeDataSet(negotiationInfo);

    }

    //ACTION LISTENER FOR CLAUSE DEFAULT
    private void actionListener(ClauseInformation clause, String newValue){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        //VALIDATE CHANGE
        validateChange(clause.getValue(), newValue);

        //ASIGNAMENT NEW VALUE
        newValue = getDecimalFormat(getBigDecimal(newValue));
        putClause(clause, newValue, getStatusClauseChange(clause.getStatus().getCode()));

        //CALCULATE BROKER CURRENCY
        final BigDecimal exchangeRate   = new BigDecimal(clauses.get(ClauseType.EXCHANGE_RATE).getValue().replace(",", ""));
        final BigDecimal amountToBuy    = new BigDecimal(clauses.get(ClauseType.CUSTOMER_CURRENCY_QUANTITY).getValue().replace("," ,""));
        final BigDecimal amountToPay    = amountToBuy.multiply(exchangeRate);

        //ASIGNAMENT BROKER CURRENCY
        final String amountToPayStr = DecimalFormat.getInstance().format(amountToPay.doubleValue());
        final ClauseInformation brokerCurrencyQuantityClause = clauses.get(ClauseType.BROKER_CURRENCY_QUANTITY);
        putClause(brokerCurrencyQuantityClause, amountToPayStr);

        adapter.changeDataSet(negotiationInfo);
    }

    /*-------------------------------------------------------------------------------------------------
                                                END ACTION LISTENER
    ---------------------------------------------------------------------------------------------------*/

    /*-------------------------------------------------------------------------------------------------
                                                VALIDATE OF DATE
    --------------------------------------------------------------------------------------------------*/

    //VALIDATE CLAUSE
    private Boolean validateClauses(Map<ClauseType, ClauseInformation> clauses){

        if(clauses != null) {

            final BigDecimal exchangeRate   = getBigDecimal(clauses.get(ClauseType.EXCHANGE_RATE).getValue());
            final BigDecimal amountToBuy    = getBigDecimal(clauses.get(ClauseType.CUSTOMER_CURRENCY_QUANTITY).getValue());
            final BigDecimal amountToPay    = getBigDecimal(clauses.get(ClauseType.BROKER_CURRENCY_QUANTITY).getValue());

            //VALIDATE STATUS CLAUSE

            //VALIDATE CLAUSE PAYMENT-INFO

            //VALIDATE QUANTITY
            if(exchangeRate.compareTo(BigDecimal.ZERO) <= 0){
                Toast.makeText(getActivity(), "The exchange must be greater than zero.", Toast.LENGTH_LONG).show();
                return false;
            }

            if(amountToBuy.compareTo(BigDecimal.ZERO) <= 0){
                Toast.makeText(getActivity(), "The  buying must be greater than zero.", Toast.LENGTH_LONG).show();
                return false;
            }

            if(amountToPay.compareTo(BigDecimal.ZERO) <= 0){
                Toast.makeText(getActivity(), "The  paying must be greater than zero.", Toast.LENGTH_LONG).show();
                return false;
            }

            if ((clauses.get(ClauseType.CUSTOMER_CURRENCY).getValue()) == (clauses.get(ClauseType.BROKER_CURRENCY).getValue())) {
                Toast.makeText(getActivity(), "The currency to pay is equal to currency buy.", Toast.LENGTH_LONG).show();
                return false;
            }

        } else {
            Toast.makeText(getActivity(), "Error. Information is null.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    //VALIDATE EXCHANGE RATE NOT IS ZERO
    private boolean validateExchangeRate(){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        final BigDecimal exchangeRate = getBigDecimal(clauses.get(ClauseType.EXCHANGE_RATE).getValue());

        if(exchangeRate.compareTo(BigDecimal.ZERO) <= 0){
            Toast.makeText(getActivity(), "The exchange rate must be greater than zero.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
    /*------------------------------------------ END VALIDATE OF DATE -------------------------------------*/

    /*------------------------------------------ OTHER METHODS -------------------------------------*/
//    private List<Currency> getCurrenciesFromQuotes(List<MerchandiseExchangeRate> quotes) {
//        List<Currency> data = new ArrayList<>();
//
//        for (MerchandiseExchangeRate exchangeRate : quotes)
//            data.add(exchangeRate.getPaymentCurrency());
//
//        return data;
//    }

    //ARRAY PAYMENT
    private ArrayList<String> getPaymentMethod(String currency){

        ArrayList<String> paymentMethods = new ArrayList<>();

        //ADD FIAT CURRENCY IF IS FIAT
        if(FiatCurrency.codeExists(currency)){
            paymentMethods.add(MoneyType.BANK.getFriendlyName());
            paymentMethods.add(MoneyType.CASH_DELIVERY.getFriendlyName());
            paymentMethods.add(MoneyType.CASH_ON_HAND.getFriendlyName());
        }

        //ADD CRYPTO CURRENCY IF IS CRYPTO
        if(CryptoCurrency.codeExists(currency)){
            paymentMethods.add(MoneyType.CRYPTO.getFriendlyName());
        }

        return paymentMethods;
    }

    //REMOVE CURRENCY TO PAY
    private void removeCurrency(){

        final Map<ClauseType, ClauseInformation> clauses = negotiationInfo.getClauses();

        String currencyPay = clauses.get(ClauseType.CUSTOMER_CURRENCY).getValue();

        for (Currency item: currencies)
            if(currencyPay.equals(item.getCode())) currencies.remove(item);

    }

    //GET CLAUSE OF INFO OF RECEIVED
    private void putPaymentInfo(Map<ClauseType, ClauseInformation> clauses){

        String currencyType = clauses.get(ClauseType.CUSTOMER_PAYMENT_METHOD).getValue();
        if(currencyType != null) {
            if (currencyType.equals(MoneyType.CRYPTO.getFriendlyName())) {
                if (clauses.get(ClauseType.BROKER_CRYPTO_ADDRESS) == null) {
                    putClause(ClauseType.BROKER_CRYPTO_ADDRESS, "Crypto Address is Generate Automatic");
                }

            } else if (currencyType.equals(MoneyType.BANK.getFriendlyName())) {
                if (clauses.get(ClauseType.BROKER_BANK_ACCOUNT) == null) {
                    putClause(ClauseType.BROKER_BANK_ACCOUNT, "The Bank Info is Intro For The Broker");
                }

            } else if (currencyType.equals(MoneyType.CASH_DELIVERY.getFriendlyName()) || (currencyType.equals(MoneyType.CASH_ON_HAND.getFriendlyName()))) {
                if (clauses.get(ClauseType.BROKER_PLACE_TO_DELIVER) == null) {
                    putClause(ClauseType.BROKER_PLACE_TO_DELIVER, "The Delivery Info is Intro For The Broker");
                }
            }
        }

    }

    //GET CLAUSE OF INFO OF PAYMENT
    private void putReceptionInfo(Map<ClauseType, ClauseInformation> clauses){

        String currencyType = clauses.get(ClauseType.BROKER_PAYMENT_METHOD).getValue();
        if(currencyType != null) {
            if (currencyType.equals(MoneyType.CRYPTO.getFriendlyName())) {
                if (clauses.get(ClauseType.CUSTOMER_CRYPTO_ADDRESS) == null) {
                    putClause(ClauseType.CUSTOMER_CRYPTO_ADDRESS, "Crypto Address is Generate Automatic");
                }

            } else if (currencyType.equals(MoneyType.BANK.getFriendlyName())) {
                if (clauses.get(ClauseType.CUSTOMER_BANK_ACCOUNT) == null) {
                    String bankAccount = "INSERT BANK ACCOUNT IN SETTINGS WALLET.";
                    if(bankAccountList.size() > 0)
                        bankAccount = bankAccountList.get(0).getAccount();
                    putClause(ClauseType.CUSTOMER_BANK_ACCOUNT, bankAccount);
                }

            } else if (currencyType.equals(MoneyType.CASH_DELIVERY.getFriendlyName()) || (currencyType.equals(MoneyType.CASH_ON_HAND.getFriendlyName()))) {
                if (clauses.get(ClauseType.CUSTOMER_PLACE_TO_DELIVER) == null) {
                    String infoDelivery = "INSERT LOCATION IN SETTINGS WALLET.";
                    if(locationList.size() > 0)
                        infoDelivery = locationList.get(0);
                    putClause(ClauseType.CUSTOMER_PLACE_TO_DELIVER, infoDelivery);
                }
            }
        }

    }

    //PUT CLAUSE CLAUSE AND VALUE
    public void putClause(final ClauseInformation clause, final String value) {

        final ClauseType type = clause.getType();
        ClauseInformation clauseInformation = new ClauseInformation() {
            @Override
            public UUID getClauseID() {
                return clause.getClauseID();
            }

            @Override
            public ClauseType getType() {
                return type;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public ClauseStatus getStatus() { return clause.getStatus(); }
        };

        negotiationInfo.getClauses().put(type, clauseInformation);
    }

    //PUT CLAUSE CLAUSE, VALUE AND STATUS
    public void putClause(final ClauseInformation clause, final String value, final ClauseStatus status) {

        final ClauseType type = clause.getType();
        ClauseInformation clauseInformation = new ClauseInformation() {
            @Override
            public UUID getClauseID() { return clause.getClauseID(); }

            @Override
            public ClauseType getType() { return type; }

            @Override
            public String getValue() { return value; }

            @Override
            public ClauseStatus getStatus() { return (status != null) ? status : clause.getStatus(); }
        };

        negotiationInfo.getClauses().put(type, clauseInformation);
    }

    //PUT CLAUSE CLAUSE TYPE, VALUE
    public void putClause(final ClauseType clauseType, final String value) {

        ClauseInformation clauseInformation = new ClauseInformation() {
            @Override
            public UUID getClauseID() { return UUID.randomUUID(); }

            @Override
            public ClauseType getType() { return clauseType; }

            @Override
            public String getValue() { return (value != null) ? value : ""; }

            @Override
            public ClauseStatus getStatus() { return ClauseStatus.DRAFT; }
        };

        negotiationInfo.getClauses().put(clauseType, clauseInformation);
    }

    //PUT CLAUSE CLAUSE AND STATUS
    public void putClause(final ClauseInformation clause, final ClauseStatus status) {

        final ClauseType type = clause.getType();
        ClauseInformation clauseInformation = new ClauseInformation() {
            @Override
            public UUID getClauseID() { return clause.getClauseID(); }

            @Override
            public ClauseType getType() { return type; }

            @Override
            public String getValue() { return clause.getValue(); }

            @Override
            public ClauseStatus getStatus() { return status; }
        };

        negotiationInfo.getClauses().put(type, clauseInformation);
    }

    private void validateChange(String oldValue, String newValue) {

        valuesHasChanged = false;
        if (oldValue != newValue)
            valuesHasChanged = true;

    }

    private ClauseStatus getStatusClauseChange(String statusClauseCode){

        ClauseStatus statusClause = null;
        if(valuesHasChanged && statusClauseCode.equals(NegotiationStepStatus.ACCEPTED.getCode()))
            statusClause = ClauseStatus.CHANGED;

        return statusClause;
    }

    private BigDecimal getBigDecimal(String value){
        return new BigDecimal(value.replace(",", ""));
    }

    private String getDecimalFormat(BigDecimal value){
        return DecimalFormat.getInstance().format(value.doubleValue());
    }
    /*------------------------------------------ END OTHER METHODS -------------------------------------*/
    /*END PRIVATE METHOD*/


}
