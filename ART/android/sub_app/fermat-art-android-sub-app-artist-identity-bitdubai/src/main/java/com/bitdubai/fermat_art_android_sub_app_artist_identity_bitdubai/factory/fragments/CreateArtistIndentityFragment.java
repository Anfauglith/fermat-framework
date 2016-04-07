package com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.factory.fragments;

/**
 * Created by edicson on 23/03/16.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitdubai.fermat_android_api.layer.definition.wallet.AbstractFermatFragment;
import com.bitdubai.fermat_android_api.layer.definition.wallet.utils.ImagesUtils;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_art_android_sub_app_artist_identity_bitdubai.factory.session.ArtistIdentitySubAppSession;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.ArtistIdentityManagerModule;
import com.bitdubai.fermat_art_api.layer.sub_app_module.identity.ArtistIdentitySettings;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_art_api.layer.identity.fan.interfaces.Fanatic;
import com.bitdubai.sub_app.artist_identity.R;

import java.io.ByteArrayOutputStream;

import static android.widget.Toast.makeText;


public class CreateArtistIndentityFragment extends AbstractFermatFragment {
    private static final String TAG = "CreateAssetUserIdentity";

    private static final int CREATE_IDENTITY_FAIL_MODULE_IS_NULL = 0;
    private static final int CREATE_IDENTITY_FAIL_NO_VALID_DATA = 1;
    private static final int CREATE_IDENTITY_FAIL_MODULE_EXCEPTION = 2;
    private static final int CREATE_IDENTITY_SUCCESS = 3;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_LOAD_IMAGE = 2;

    private static final int CONTEXT_MENU_CAMERA = 1;
    private static final int CONTEXT_MENU_GALLERY = 2;

    private byte[] brokerImageByteArray;

    private ArtistIdentityManagerModule moduleManager;
    private ErrorManager errorManager;

    private Button createButton;
    private EditText mIdentityName;
    private ImageView mIdentityImage;

    ArtistIdentitySubAppSession artistIdentitySubAppSession;
    private Fanatic identitySelected;
    private boolean isUpdate = false;

    SettingsManager<ArtistIdentitySettings> settingsManager;
    ArtistIdentitySettings userIdentitySettings = null;

    public static CreateArtistIndentityFragment newInstance() {
        return new CreateArtistIndentityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            artistIdentitySubAppSession = (ArtistIdentitySubAppSession) appSession;
            moduleManager = artistIdentitySubAppSession.getModuleManager();
            errorManager = appSession.getErrorManager();

            settingsManager = appSession.getModuleManager().getSettingsManager();

            try {
                if (appSession.getAppPublicKey() != null) {
                    userIdentitySettings = settingsManager.loadAndGetSettings(appSession.getAppPublicKey());
                }
            } catch (Exception e) {
                userIdentitySettings = null;
            }

            if (userIdentitySettings == null) {
                userIdentitySettings = new ArtistIdentitySettings();
                userIdentitySettings.setIsPresentationHelpEnabled(true);
                if (appSession.getAppPublicKey() != null) {
                    settingsManager.persistSettings(appSession.getAppPublicKey(), userIdentitySettings);
                }
            }

            /*if(moduleManager == null) {
                final UserIdentitySettings userIdentitySettingsTemp = userIdentitySettings;

                Handler handlerTimer = new Handler();
                handlerTimer.postDelayed(new Runnable() {
                    public void run() {
                        if (userIdentitySettingsTemp.isPresentationHelpEnabled()) {
                            setUpPresentation(false);
                        }
                    }
                }, 500);
            }*/

        } catch (Exception ex) {

        }
    }

    /*private void setUpPresentation(boolean checkButton) {
        try {
            PresentationDialog presentationDialog = new PresentationDialog.Builder(getActivity(), appSession)
                    .setBannerRes(R.drawable.banner_asset_user_identity)
                    .setIconRes(R.drawable.asset_user_wallet)
                    .setVIewColor(R.color.dap_identity_user_view_color)
                    .setTitleTextColor(R.color.dap_identity_user_view_color)
                    .setSubTitle(R.string.dap_user_identity_welcome_subTitle)
                    .setBody(R.string.dap_user_identity_welcome_body)
                    .setTemplateType(PresentationDialog.TemplateType.TYPE_PRESENTATION_WITHOUT_IDENTITIES)
                    .setIsCheckEnabled(checkButton)
                    .build();
            presentationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.dap_user_identity_menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

           // if (item.getItemId() == R.id.action_identity_user_help) {
                //setUpPresentation(settingsManager.loadAndGetSettings(appSession.getAppPublicKey()).isPresentationHelpEnabled());
             //   return true;
           // }

        } catch (Exception e) {
            errorManager.reportUnexpectedUIException(UISource.ACTIVITY, UnexpectedUIExceptionSeverity.UNSTABLE, FermatException.wrapException(e));
            makeText(getActivity(), "Identity User system error",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootLayout = inflater.inflate(R.layout.confirm_dialog, container, false);
        initViews(rootLayout);
        setUpIdentity();

        return rootLayout;
    }

    /**
     * Inicializa las vistas de este Fragment
     *
     * @param layout el layout de este Fragment que contiene las vistas
     */
    private void initViews(View layout) {
        createButton = (Button) layout.findViewById(R.id.confirmDialogNoButton);
        mIdentityName = (EditText) layout.findViewById(R.id.confirmDialogNoButton);
        mIdentityImage = (ImageView) layout.findViewById(R.id.confirmDialogContentText);

        createButton.setText((!isUpdate) ? "Create" : "Update");

        mIdentityName.requestFocus();

        mIdentityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CommonLogger.debug(TAG, "Entrando en mIdentityImage.setOnClickListener");
                registerForContextMenu(mIdentityImage);
                getActivity().openContextMenu(mIdentityImage);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CommonLogger.debug(TAG, "Entrando en createButton.setOnClickListener");
                int resultKey = createNewIdentity();
                switch (resultKey) {
                    case CREATE_IDENTITY_SUCCESS:
//                        changeActivity(Activities.DAP_SUB_APP_ASSET_USER_IDENTITY.getCode(), appSession.getAppPublicKey());
                        if (!isUpdate) {
                            Toast.makeText(getActivity(), "Identity created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_EXCEPTION:
                        Toast.makeText(getActivity(), "Error al crear la identidad", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_NO_VALID_DATA:
                        Toast.makeText(getActivity(), "La data no es valida", Toast.LENGTH_LONG).show();
                        break;
                    case CREATE_IDENTITY_FAIL_MODULE_IS_NULL:
                        Toast.makeText(getActivity(), "No se pudo acceder al module manager, es null", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void setUpIdentity() {
        try {

         //   identitySelected = (IdentityAssetUser) userIdentitySubAppSession.getData(SessionConstants.IDENTITY_SELECTED);

            if (identitySelected != null) {
                loadIdentity();
            } else {
            //    List<IdentityAssetUser> lst = moduleManager.getIdentityAssetUsersFromCurrentDeviceUser();
                /*if (!lst.isEmpty()) {
                    identitySelected = lst.get(0);
                }
                if (identitySelected != null) {
                    loadIdentity();
                    isUpdate = true;
                    createButton.setText("Save changes");
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIdentity() {
        if (identitySelected.getProfileImage() != null) {
            Bitmap bitmap = null;
            if (identitySelected.getProfileImage().length > 0) {
                bitmap = BitmapFactory.decodeByteArray(identitySelected.getProfileImage(), 0, identitySelected.getProfileImage().length);
//                bitmap = Bitmap.createScaledBitmap(bitmap, mBrokerImage.getWidth(), mBrokerImage.getHeight(), true);
            } else {
                //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.);

                //Picasso.with(getActivity()).load(R.drawable.ic_profile_male).into(mBrokerImage);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            brokerImageByteArray = toByteArray(bitmap);
            mIdentityImage.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), bitmap));
        }
        mIdentityName.setText(identitySelected.getAlias());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = null;
            ImageView pictureView = mIdentityImage;

            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    break;
                case REQUEST_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    try {
                        if (isAttached) {
                            ContentResolver contentResolver = getActivity().getContentResolver();
                            imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage);
                            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, pictureView.getWidth(), pictureView.getHeight(), true);
                            brokerImageByteArray = toByteArray(imageBitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error cargando la imagen", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            if (pictureView != null && imageBitmap != null)
//                pictureView.setImageDrawable(new BitmapDrawable(getResources(), imageBitmap));
                pictureView.setImageDrawable(ImagesUtils.getRoundedBitmap(getResources(), imageBitmap));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose mode");
        menu.setHeaderIcon(getActivity().getResources().getDrawable(R.drawable.image_round_corners));
        menu.add(Menu.NONE, CONTEXT_MENU_CAMERA, Menu.NONE, "Camera");
        menu.add(Menu.NONE, CONTEXT_MENU_GALLERY, Menu.NONE, "Gallery");

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_CAMERA:
                dispatchTakePictureIntent();
                break;
            case CONTEXT_MENU_GALLERY:
                loadImageFromGallery();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Crea una nueva identidad para un crypto broker
     *
     * @return key con el resultado de la operacion:<br/><br/>
     * <code>CREATE_IDENTITY_SUCCESS</code>: Se creo exitosamente una identidad <br/>
     * <code>CREATE_IDENTITY_FAIL_MODULE_EXCEPTION</code>: Se genero una excepcion cuando se ejecuto el metodo para crear la identidad en el Module Manager <br/>
     * <code>CREATE_IDENTITY_FAIL_MODULE_IS_NULL</code>: No se tiene una referencia al Module Manager <br/>
     * <code>CREATE_IDENTITY_FAIL_NO_VALID_DATA</code>: Los datos ingresados para crear la identidad no son validos (faltan datos, no tiene el formato correcto, etc) <br/>
     */
    private int createNewIdentity() {

        String brokerNameText = mIdentityName.getText().toString();
        boolean dataIsValid = validateIdentityData(brokerNameText, brokerImageByteArray);

        if (dataIsValid) {
            if (moduleManager != null) {
               // try {
                    if (!isUpdate) {
                    //    moduleManager.createNewIdentityAssetUser(brokerNameText, (brokerImageByteArray == null) ? convertImage(R.drawable.asset_user_identity) : brokerImageByteArray);
                    } else {                      //  moduleManager.updateIdentityAssetUser(identitySelected.getPublicKey(), brokerNameText, brokerImageByteArray);
                     }
               // } catch (CantCreateNewIdentityAssetUserException e) {
                //    errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                //} catch (CantUpdateIdentityAssetUserException e) {
                 //   errorManager.reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.UNSTABLE, e);
                //}
                return CREATE_IDENTITY_SUCCESS;
            }
            return CREATE_IDENTITY_FAIL_MODULE_IS_NULL;
        }
        return CREATE_IDENTITY_FAIL_NO_VALID_DATA;

    }

    private byte[] convertImage(int resImage) {
        Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), resImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void dispatchTakePictureIntent() {
        Log.i(TAG, "Opening Camera app to take the picture...");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void loadImageFromGallery() {
        Log.i(TAG, "Loading Image from Gallery...");

        Intent loadImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(loadImageIntent, REQUEST_LOAD_IMAGE);
    }

    private boolean validateIdentityData(String brokerNameText, byte[] brokerImageBytes) {
        if (brokerNameText.isEmpty())
            return false;
        if (brokerImageBytes == null)
            return true;
        if (brokerImageBytes.length > 0)
            return true;

        return true;
    }

    /**
     * Bitmap to byte[]
     *
     * @param bitmap Bitmap
     * @return byte array
     */
    private byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}