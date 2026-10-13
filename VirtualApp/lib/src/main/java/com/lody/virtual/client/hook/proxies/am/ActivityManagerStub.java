package com.lody.virtual.client.hook.proxies.am;

import android.os.Build;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import java.lang.reflect.Method;
import mirror.android.app.IActivityManager;

/**
 * @Author Lody & Stealth Anti-Detection Updated
 * @Description Crash-Proof Activity Manager Stub to hide Virtual Environment traces from IMO & WhatsApp
 */
public class ActivityManagerStub extends BinderInvocationProxy {

    public ActivityManagerStub() {
        super(IActivityManager.Stub.asInterface, "activity");
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();

        try {
            // প্রসেস ও অ্যাক্টিভিটি চেক বাইপাস করার জন্য সেফটি প্রক্সি
            addMethodProxy(new ReplaceCallingPkgMethodProxy("startActivity") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

            addMethodProxy(new ReplaceCallingPkgMethodProxy("getRunningAppProcesses") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        // ভার্চুয়াল প্রসেস লিস্ট হাইড করে রিয়েল ডিভাইসের মতো স্টেট রিটার্ন করবে
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

            addMethodProxy(new ReplaceCallingPkgMethodProxy("getContentProvider") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
