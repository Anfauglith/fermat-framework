package com.mati.fermat_preference_settings.settings.dialog;

import android.content.Context;
import android.view.View;

import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.mati.fermat_preference_settings.R;
import com.mati.fermat_preference_settings.settings.models.PreferenceSettingsDialogItem;

import java.util.List;

/**
 * Created by mati on 2016.02.08..
 */
public class ContextMenuAdapter extends FermatAdapter<PreferenceSettingsDialogItem,ContextMenuHolder> {



    protected ContextMenuAdapter(Context context) {
        super(context);
    }

    protected ContextMenuAdapter(Context context, List<PreferenceSettingsDialogItem> dataSet) {
        super(context, dataSet);
    }

    @Override
    protected ContextMenuHolder createHolder(View itemView, int type) {
        return new ContextMenuHolder(itemView);
    }

    @Override
    protected int getCardViewResource() {
        return R.layout.context_menu_row;
    }

    @Override
    protected void bindHolder(ContextMenuHolder holder, PreferenceSettingsDialogItem data, int position) {
        holder.getFermatTextView().setText(data.getText());
    }
}
