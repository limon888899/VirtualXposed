package com.lody.virtual.client.hook.proxies.pm;

import android.os.Build;
import android.os.IInterface;

import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.ResultStaticMethodProxy;
import com.lody.virtual.helper.compat.BuildConfig;

import java.lang.reflect.Method;

import mirror.android.app.ActivityThread;

/**
 * @Author Lody & Stealth Anti-Detection Updated
 * @Description Stealth Package Manager Hook to hide Virtual Environment & Xposed/Hook Signatures from IMO & WhatsApp
 */
@Inject(MethodProxies.class)
public final class PackageManagerStub extends MethodInvocationProxy<MethodInvocationStub<IInterface>> {

    public PackageManagerStub() {
        super(new MethodInvocationStub<>(ActivityThread.sPackageManager.get()));
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        
        try {
            addMethodProxy(new ResultStaticMethodProxy("addPermissionAsync", true));
            addMethodProxy(new ResultStaticMethodProxy("addPermission", true));
            addMethodProxy(new ResultStaticMethodProxy("performDexOpt", true));
            addMethodProxy(new ResultStaticMethodProxy("performDexOptIfNeeded", false));
            addMethodProxy(new ResultStaticMethodProxy("performDexOptSecondary", true));
            addMethodProxy(new ResultStaticMethodProxy("addOnPermissionsChangeListener", 0));
            addMethodProxy(new ResultStaticMethodProxy("removeOnPermissionsChangeListener", 0));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addMethodProxy(new ResultStaticMethodProxy("checkPackageStartable", 0));
            }
            if (BuildConfig.isOreo()) {
                addMethodProxy(new ResultStaticMethodProxy("notifyDexLoad", 0));
                addMethodProxy(new ResultStaticMethodProxy("notifyPackageUse", 0));
                addMethodProxy(new ResultStaticMethodProxy("setInstantAppCookie", false));
                addMethodProxy(new ResultStaticMethodProxy("isInstantApp", false));
            }

            // অ্যান্টি-ডিটেকশন বাইপাস: হুক বা ভার্চুয়াল প্যাকেজ সিগনেচার হাইড করার জন্য সেফটি ওভাররাইড
            addMethodProxy(new ResultStaticMethodProxy("getPackageInfo", null));
            addMethodProxy(new ResultStaticMethodProxy("getApplicationInfo", null));
            addMethodProxy(new ResultStaticMethodProxy("getInstalledPackages", null));
            addMethodProxy(new ResultStaticMethodProxy("getInstalledApplications", null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inject() throws Throwable {
        final IInterface hookedPM = getInvocationStub().getProxyInterface();
        ActivityThread.sPackageManager.set(hookedPM);
        BinderInvocationStub pmHookBinder = new BinderInvocationStub(getInvocationStub().getBaseInterface());
        pmHookBinder.copyMethodProxies(getInvocationStub());
        pmHookBinder.replaceService("package");
    }

    @Override
    public boolean isEnvBad() {
        return getInvocationStub().getProxyInterface() != ActivityThread.sPackageManager.get();
    }
}
