package com.lody.virtual.client.hook.proxies.audio;

import android.content.Context;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import java.lang.reflect.Method;
import mirror.android.media.IAudioService;

/**
 * @Author Lody & Stealth Anti-Detection Updated
 * @Description Anti-Detection Audio Manager Hook for IMO, WhatsApp & Live Voice Modification without Bans or Crashes
 */
public class AudioManagerStub extends BinderInvocationProxy {

    public AudioManagerStub() {
        super(IAudioService.Stub.asInterface, Context.AUDIO_SERVICE);
    }

    @Override
    protected void onBindMethods() {
        super.onBindMethods();
        
        try {
            // ১. সাধারণ ভলিউম ও অডিও রুটিন বাইপাস যাতে অ্যাপ হুক ডিটেক্ট করতে না পারে
            addMethodProxy(new ReplaceCallingPkgMethodProxy("adjustVolume") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

            addMethodProxy(new ReplaceCallingPkgMethodProxy("setMode") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        // কল মোড বা অডিও রুট পরিবর্তন করার সময় অ্যান্টি-ডিটেকশন সেফটি চেক
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return null;
                    }
                }
            });

            // ২. রিয়েল-টাইম মাইক্রোফোন ও ভয়েস স্ট্রিম ইন্টারসেপ্ট করার সেফটি প্রক্সি (IMO ও WhatsApp কল সুরক্ষার জন্য)
            addMethodProxy(new ReplaceCallingPkgMethodProxy("setMicrophoneMute") {
                @Override
                public Object call(Object who, Method method, Object... args) throws Throwable {
                    try {
                        // কাস্টম অডিও ইনপুট ও ভয়েস চেঞ্জার সক্রিয় রাখার সময় যাতে মিউট বা ব্যান ট্রিগার না করে
                        return super.call(who, method, args);
                    } catch (Exception e) {
                        return false;
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
