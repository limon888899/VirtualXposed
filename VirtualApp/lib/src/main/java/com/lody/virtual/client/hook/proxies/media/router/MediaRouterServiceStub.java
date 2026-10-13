package com.lody.virtual.client.hook.proxies.media.router;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;

import java.lang.reflect.Method;

import mirror.android.media.IMediaRouterService;

/**
 * @Author Lody & Custom Updated
 * @Description Crash-Proof Media/Camera Router Service Hook to support Virtual Camera feed seamlessly
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MediaRouterServiceStub extends BinderInvocationProxy {

    public MediaRouterServiceStub() {
        super(IMediaRouterService.Stub.asInterface, Context.MEDIA_ROUTER_SERVICE);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        
        try {
            addMethodProxy(new ReplaceCallingPkgMethodProxy("registerClientAsUser") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        // ফেক ক্যামেরা বা মিডিয়া স্ট্রিম রেন্ডার করার সময় ক্রাশ এড়াতে সেফটি চেক
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        // কোনো এক্সেপশন ঘটলে অ্যাপ ক্রাশ না করে সেভাবে হ্যান্ডেল করবে
                        return null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
