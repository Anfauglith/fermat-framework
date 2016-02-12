package com.mati.fermat_preference_settings.settings;

import android.app.Activity;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapterImproved;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.mati.fermat_preference_settings.R;
import com.mati.fermat_preference_settings.settings.dialog.SettingsDialog;
import com.mati.fermat_preference_settings.settings.holders.SettingSwitchViewHolder;
import com.mati.fermat_preference_settings.settings.holders.SettingTextOpenDialogViewHolder;
import com.mati.fermat_preference_settings.settings.interfaces.DialogCallback;
import com.mati.fermat_preference_settings.settings.interfaces.PreferenceSettingsItem;
import com.mati.fermat_preference_settings.settings.models.PreferenceSettingsOpenDialogText;
import com.mati.fermat_preference_settings.settings.models.PreferenceSettingsSwithItem;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by mati on 2016.02.08..
 */
public class FermatSettingsAdapter extends FermatAdapterImproved<PreferenceSettingsItem, FermatViewHolder> implements DialogCallback {


    final int SWITH_TYPE = 1;
    final int OPEN_DIALOG_TEXT_TYPE = 2;

    WeakReference<FermatPreferenceFragment> fragmentWeakReference;

    protected FermatSettingsAdapter(Activity context) {
        super(context);
    }

    protected FermatSettingsAdapter(Activity context,FermatPreferenceFragment fermatPreferenceFragment ,List<PreferenceSettingsItem> dataSet) {
        super(context, dataSet);
        this.fragmentWeakReference = new WeakReference<FermatPreferenceFragment>(fermatPreferenceFragment);
    }

    @Override
    protected FermatViewHolder createHolder(View itemView, int type) {
        FermatViewHolder fermatViewHolder = null;
        switch (type){
            case SWITH_TYPE:
                fermatViewHolder = new SettingSwitchViewHolder(itemView,SWITH_TYPE);
                break;
            case OPEN_DIALOG_TEXT_TYPE:
                fermatViewHolder = new SettingTextOpenDialogViewHolder(itemView,OPEN_DIALOG_TEXT_TYPE);
                break;
        }
        return fermatViewHolder;
    }

    @Override
    protected int getCardViewResource(int type) {
        int ret = 0;
        switch (type){
            case SWITH_TYPE:
                ret = R.layout.preference_settings_switch_row;
                break;
            case OPEN_DIALOG_TEXT_TYPE:
                ret = R.layout.preference_settings_dialog_row;
                break;
        }
        return ret;
    }

    @Override
    protected void bindHolder(FermatViewHolder holder, PreferenceSettingsItem data, int position) {

        switch (holder.getHolderType()){
            case SWITH_TYPE:
                SettingSwitchViewHolder settingSwitchViewHolder = (SettingSwitchViewHolder) holder;
                PreferenceSettingsSwithItem preferenceSettingsSwithItem = (PreferenceSettingsSwithItem) data;
                settingSwitchViewHolder.getSettings_switch().setChecked(preferenceSettingsSwithItem.getSwitchChecked());
                settingSwitchViewHolder.getTextView().setText(preferenceSettingsSwithItem.getText());
                break;
            case OPEN_DIALOG_TEXT_TYPE:
                SettingTextOpenDialogViewHolder settingTextOpenDialogViewHolder = (SettingTextOpenDialogViewHolder) holder;
                final PreferenceSettingsOpenDialogText preferenceSettingsOpenDialogText = (PreferenceSettingsOpenDialogText) data;
                settingTextOpenDialogViewHolder.getTextView().setText(preferenceSettingsOpenDialogText.getText());
                settingTextOpenDialogViewHolder.getTextView();
//                context.registerForContextMenu(lblMensaje);
                settingTextOpenDialogViewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SettingsDialog(context,getCallback(),preferenceSettingsOpenDialogText.getOptionList()).show();
                    }
                });
                break;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) instanceof PreferenceSettingsSwithItem) {
            return SWITH_TYPE;
        } else if (dataSet.get(position) instanceof PreferenceSettingsOpenDialogText) {
            return OPEN_DIALOG_TEXT_TYPE;
        }
        return -1;
    }

    private DialogCallback getCallback(){
        return this;
    }

    @Override
    public void optionSelected(PreferenceSettingsItem preferenceSettingsItem, int position) {
        fragmentWeakReference.get().onSettingsTouched(preferenceSettingsItem,position);
    }

    public void clear(){
        fragmentWeakReference.clear();
    }
}
