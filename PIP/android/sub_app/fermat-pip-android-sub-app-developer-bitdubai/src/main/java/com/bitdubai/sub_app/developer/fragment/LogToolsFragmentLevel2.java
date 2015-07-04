package com.bitdubai.sub_app.developer.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.pip_actor.developer.ClassHierarchyLevels;
import com.bitdubai.fermat_api.layer.pip_actor.developer.LogTool;
import com.bitdubai.fermat_api.layer.pip_actor.developer.ToolManager;
import com.bitdubai.sub_app.developer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class <code>com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments.LogToolsFragment</code>
 * haves all methods for the log tools activity of a developer
 * <p/>
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 25/06/15.
 *
 * @version 1.0
 */
public class LogToolsFragmentLevel2 extends Fragment {

    private Map<String, List<ClassHierarchyLevels>> pluginClasses;

    private static final String ARG_POSITION = "position";
    View rootView;

    private LogTool logTool;

    private static Platform platform = new Platform();

    public static LogToolsFragmentLevel2 newInstance(int position) {
        LogToolsFragmentLevel2 f = new LogToolsFragmentLevel2();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ToolManager toolManager = platform.getToolManager();
            try {
                logTool = toolManager.getLogTool();
            } catch (Exception e) {
                showMessage("CantGetToolManager - " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception ex) {
            showMessage("Unexpected error get tool manager - " + ex.getMessage());
            ex.printStackTrace();
        }

        pluginClasses = new HashMap<String,List<ClassHierarchyLevels>>();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String selectedWord = ((TextView) info.targetView).getText().toString();


        menu.setHeaderTitle(selectedWord);
        menu.add(LogLevel.NOT_LOGGING.toString());
        menu.add(LogLevel.MINIMAL_LOGGING.toString());
        menu.add(LogLevel.MODERATE_LOGGING.toString());
        menu.add(LogLevel.AGGRESSIVE_LOGGING.toString());
    }

    public boolean onContextItemSelected(MenuItem item) {
       /* AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Object item = getListAdapter().getItem(info.position);*/
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedWord = ((TextView) info.targetView).getText().toString();


        if (item.getTitle() == LogLevel.NOT_LOGGING.toString()) {
            changeLogLevel(LogLevel.NOT_LOGGING, selectedWord);
        } else if (item.getTitle() == LogLevel.MINIMAL_LOGGING.toString()) {
            changeLogLevel(LogLevel.MINIMAL_LOGGING, selectedWord);
        } else if (item.getTitle() == LogLevel.MODERATE_LOGGING.toString()) {
            changeLogLevel(LogLevel.MODERATE_LOGGING, selectedWord);
        } else if (item.getTitle() == LogLevel.AGGRESSIVE_LOGGING.toString()) {
            changeLogLevel(LogLevel.AGGRESSIVE_LOGGING, selectedWord);
        } else {
            return false;
        }
        return true;
    }

    private void changeLogLevel(LogLevel logLevel, String resource) {
        try {
            //String name = resource.split(" - ")[0];
           // String type = resource.split(" - ")[1];
           // if (type.equals("Addon")) {
            //    Addons addon = Addons.getByKey(name);
           //     logTool.setLogLevel(addon, logLevel);
           // } else // por ahora no tengo como detectar si es un plug in o no.if (type.equals("Plugin"))
             //{
                Plugins plugin = Plugins.getByKey("Bitcoin Crypto Vault");
                //logTool.setLogLevel(plugin, logLevel);
            /**
             * Now I must look in pluginClasses map the match of the selected class to pass the full path
             */
            HashMap<String, LogLevel> data = new HashMap<String, LogLevel>();
            for (ClassHierarchyLevels c : pluginClasses.get(plugin.getKey())){
                    if (c.getLevel3().equals(resource))
                        data.put(c.getFullPath(), logLevel);
                if (c.getLevel2().equals(resource))
                        data.put(c.getFullPath(), logLevel);
                if (c.getLevel1().equals(resource))
                        data.put(c.getFullPath(), logLevel);
            }
                logTool.setNewLogLevelInClass(plugin, data);

            //}


            LogToolsFragmentLevel2 logToolsFragment = new LogToolsFragmentLevel2();

            FragmentTransaction FT = getFragmentManager().beginTransaction();

            FT.replace(R.id.logContainer, logToolsFragment);

            FT.commit();
        } catch (Exception e) {
            System.out.println("*********** soy un error " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_log_tools, container, false);
        try {
            // Get ListView object from xml
            //final ListView listView = (ListView) rootView.findViewById(R.id.listaLogResources);

            List<Plugins> plugins = logTool.getAvailablePluginList();
            List<Addons> addons = logTool.getAvailableAddonList();

            List<String> list = new ArrayList<>();

            for(Plugins plugin : plugins){
                list.add(plugin.getKey()); //+" - Plugin || LogLevel: "+logTool.getLogLevel(plugin));
                /**
                 * I will get the list of the available classes on the plug in
                 */
                String level1="";
                String level2="";
                String toReplace = "";
                List<ClassHierarchyLevels> newList = new ArrayList<ClassHierarchyLevels>();
                for (ClassHierarchyLevels classes : logTool.getClassesHierarchyPlugins(plugin)){
                    /**
                     * I will acommodate the values to fit the screen
                     */
                    if (classes.getLevel1().length() > 40)
                        toReplace = classes.getLevel1().substring(15, classes.getLevel1().length()-15);
                    else if (classes.getLevel1().length() < 40)
                        toReplace = classes.getLevel1().substring(8, classes.getLevel1().length()-8);
                    else if (classes.getLevel1().length() < 10)
                        toReplace = "   ";
                    classes.setLevel1(classes.getLevel1().replace(toReplace, "..."));

                    if (classes.getLevel2().length() > 40)
                        toReplace = classes.getLevel2().substring(15, classes.getLevel2().length()-15);
                    else if (classes.getLevel2().length() < 40 && classes.getLevel2().length() > 20)
                        toReplace = classes.getLevel2().substring(8, classes.getLevel2().length()-8);
                    else if (classes.getLevel2().length() < 10)
                        toReplace = "  ";

                    classes.setLevel2("-" + classes.getLevel2().replace(toReplace, "..."));
                    classes.setLevel3("--" + classes.getLevel3());

                    /**
                     * I will add the first item to the list. If I already added it, then I will skip it.
                     */
                    if (!level1.contentEquals(classes.getLevel1())){
                        level1 = classes.getLevel1();
                        list.add(classes.getLevel1());

                    }
                    if (!level2.contentEquals(classes.getLevel2())){
                        level2 = classes.getLevel2();
                        list.add(classes.getLevel2());
                    }
                    /**
                     * this level will always be added
                     */
                    list.add(classes.getLevel3());

                    /**
                     * I insert the modified class in a new map with the plug in and the classes.
                     */
                    newList.add(classes);
                }
                pluginClasses.put(plugin.getKey(), newList);


            }
            for(Addons addon : addons){ list.add(addon.getKey() + " - Addon || LogLevel: " + logTool.getLogLevel(addon)); }

            String[] availableResources;
            if (list.size() > 0) {
                availableResources = new String[list.size()];
                for(int i = 0; i < list.size() ; i++) {
                    availableResources[i] = list.get(i);
                }
            } else {
                availableResources = new String[0];
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, availableResources);

            //listView.setAdapter(adapter);

            //registerForContextMenu(listView);
        } catch (Exception e) {
            showMessage("LogTools Fragment onCreateView Exception - " + e.getMessage());
            e.printStackTrace();
        }
        return rootView;
    }

    //show alert
    private void showMessage(String text) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(text);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // aquí puedes añadir funciones
            }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}