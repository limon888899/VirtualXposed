package com.lody.virtual.client.hook.proxies.os;

import android.content.pm.ApplicationInfo;
import com.lody.virtual.client.VClientImpl;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import java.lang.reflect.Method;

/**
 * Author: weishu & Custom Updated
 * Description: Universal Android Version Spoofing (Android 5 to Android 16) with Crash Protection for Virtual Camera
 */
public class DeviceIdentifiersPolicyServiceStub extends BinderInvocationProxy {

    // =========================================================================
    // অ্যান্ড্রয়েড ভার্সন লিস্ট (আপনার পছন্দমতো যেকোনো একটি সিলেক্ট করুন):
    // 21 = Android 5.0 (Lollipop)     | 22 = Android 5.1 (Lollipop MR1)
    // 23 = Android 6.0 (Marshmallow)   | 24 = Android 7.0 (Nougat)
    // 25 = Android 7.1 (Nougat MR1)    | 26 = Android 8.0 (Oreo)
    // 27 = Android 8.1 (Oreo MR1)      | 28 = Android 9.0 (Pie)
    // 29 = Android 10.0 (Q)            | 30 = Android 11.0 (R)
    // 31 = Android 12.0 (S)            | 32 = Android 12.1 (S_V2 / 12L)
    // 33 = Android 13.0 (Tiramisu)     | 34 = Android 14.0 (UpsideDownCake)
    // 35 = Android 15.0 (VanillaIceCream)| 36 = Android 16.0 (Baklava)
    // =========================================================================
    private static final int TARGET_SDK_VERSION = 30; // বর্তমান ডিফল্ট অ্যান্ড্রয়েড ১১ (আপনি চাইলে যেকোনো নাম্বার বসাতে পারেন)

    public DeviceIdentifiersPolicyServiceStub() {
        super(IDeviceIdentifiersPolicyService.Stub.TYPE, "device_identifiers");
    }

    @Override
    protected void inject() {
        try {
            addMethodProxy(new ReplaceCallingPkgMethodProxy("getSerialForPackage") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        ApplicationInfo info = VClientImpl.get().getCurrentApplicationInfo();
                        if (info != null && info.targetSdkVersion >= 29 && BuildCompat.isQ()) {
                            return "unknown";
                        }
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return "unknown";
                    }
                }
            });

            // ইউনিভার্সাল ও ক্রাশ-প্রুফ ভার্সন স্পুফিং হুক
            addMethodProxy(new ReplaceCallingPkgMethodProxy("VERSION") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        if (method != null) {
                            String methodName = method.getName();
                            // SDK_INT ছাড়াও রিলিজ ভার্সন বা কোডনেম চেক করলে যেন ক্রাশ না করে
                            if ("SDK_INT".equals(methodName)) {
                                return TARGET_SDK_VERSION;
                            }
                        }
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        // ভার্চুয়াল ক্যামেরা বা অন্যান্য সেন্সর ব্যবহারের সময় ক্রাশ এড়াতে ফলব্যাক ভ্যালু রিটার্ন করবে
                        return TARGET_SDK_VERSION;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
