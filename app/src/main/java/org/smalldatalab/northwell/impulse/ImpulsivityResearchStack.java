package org.smalldatalab.northwell.impulse;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import org.researchstack.backbone.storage.database.AppDatabase;
import org.researchstack.backbone.storage.database.sqlite.SqlCipherDatabaseHelper;
import org.researchstack.backbone.storage.database.sqlite.UpdatablePassphraseProvider;
import org.researchstack.backbone.storage.file.EncryptionProvider;
import org.researchstack.backbone.storage.file.FileAccess;
import org.researchstack.backbone.storage.file.PinCodeConfig;
import org.researchstack.backbone.storage.file.aes.AesProvider;
import org.sagebionetworks.bridge.android.manager.BridgeManagerProvider;
//import org.smalldatalab.northwell.impulse.bridge.BridgeEncryptedDatabase;
import org.researchstack.skin.AppPrefs;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.PermissionRequestManager;
import org.researchstack.skin.ResearchStack;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.TaskProvider;
import org.researchstack.skin.UiManager;
import org.researchstack.skin.notification.NotificationConfig;
import org.researchstack.skin.notification.SimpleNotificationConfig;

//import org.sagebionetworks.bridge.android.

public class ImpulsivityResearchStack extends ResearchStack
{



    public static void init(Context context, ImpulsivityResearchStack concreteResearchStack)
    {

        concreteResearchStack.createBridgeManagerProvider(context);


        ResearchStack.init(context, concreteResearchStack);

        //initialize RSTB
        CTFTaskBuilderManager.init(context, ResourceManager.getInstance(), ImpulsivityAppStateManager.getInstance());

        //initialize RSRP
        ImpulsivityBridgeManagerProvider provider = new ImpulsivityBridgeManagerProvider(
                concreteResearchStack.bridgeManagerProvider.getBridgeConfig(),
                concreteResearchStack.bridgeManagerProvider.getUploadManager()
        );

        CTFResultsProcessorManager.init(context, provider);

        //first, create BridgeManagerProvider
        //inject into
//        concreteResearchStack.createBridgeManagerProvider(context);


    }






    private BridgeManagerProvider bridgeManagerProvider;

    protected BridgeManagerProvider createBridgeManagerProvider(Context context) {

        BridgeManagerProvider.init(context);
        bridgeManagerProvider = BridgeManagerProvider.getInstance();
        return BridgeManagerProvider.getInstance();

    }

    @Override
    protected AppDatabase createAppDatabaseImplementation(Context context)
    {
        SQLiteDatabase.loadLibs(context);

        return new SqlCipherDatabaseHelper(
                context,
                SqlCipherDatabaseHelper.DEFAULT_NAME,
                null,
                SqlCipherDatabaseHelper.DEFAULT_VERSION,
                new UpdatablePassphraseProvider()
        );

//        return new BridgeEncryptedDatabase(context,
//                SqlCipherDatabaseHelper.DEFAULT_NAME,
//                null,
//                SqlCipherDatabaseHelper.DEFAULT_VERSION,
//                new UpdatablePassphraseProvider());
    }

    @Override
    protected FileAccess createFileAccessImplementation(Context context)
    {
        String pathName = context.getString(R.string.ctf_state_helper_path);
        return new ImpulsivityAppStateManager(pathName);
    }

    @Override
    protected PinCodeConfig getPinCodeConfig(Context context)
    {
        long autoLockTime = AppPrefs.getInstance(context).getAutoLockTime();
        return new PinCodeConfig(autoLockTime);
    }

    @Override
    protected EncryptionProvider getEncryptionProvider(Context context)
    {
        return new AesProvider();
    }

    @Override
    protected ResourceManager createResourceManagerImplementation(Context context)
    {
        return new ImpulsivityResourceManager();
    }

    @Override
    protected UiManager createUiManagerImplementation(Context context)
    {
        return new ImpulseUiManager();
    }

    @Override
    protected DataProvider createDataProviderImplementation(Context context)
    {
        return new ImpulsivityDataProvider(bridgeManagerProvider);
    }

    @Override
    protected TaskProvider createTaskProviderImplementation(Context context)
    {

        return new ImpulsivityTaskProvider(context);
    }

    @Override
    protected NotificationConfig createNotificationConfigImplementation(Context context)
    {
        return new SimpleNotificationConfig();
    }

    @Override
    protected PermissionRequestManager createPermissionRequestManagerImplementation(Context context)
    {
        return new ImpulsivityPermissionResultManager();
    }
}
