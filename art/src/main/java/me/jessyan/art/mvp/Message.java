/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.art.mvp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Defines a message containing a description and arbitrary data object that can be
 * sent to a {@link Handler}.  This object contains two extra int fields and an
 * extra object field that allow you to not do allocations in many cases.
 * <p>
 * <p class="note">While the constructor of Message is public, the best way to get
 * one of these is to call {@link #obtain Message.obtain()} or one of the
 * {@link Handler#obtainMessage Handler.obtainMessage()} methods, which will pull
 * them from a pool of recycled objects.</p>
 *
 * Created by JessYan on 24/02/2017 10:15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 */
public final class Message implements Parcelable {
    /**
     * User-defined message code so that the recipient can identify
     * what this message is about. Each {@link Handler} has its own name-space
     * for message codes, so you do not need to worry about yours conflicting
     * with other handlers.
     */
    public int what;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg1;

    /**
     * arg1 and arg2 are lower-cost alternatives to using
     * {@link #setData(Bundle) setData()} if you only need to store a
     * few integer values.
     */
    public int arg2;


    public String str;

    /**
     * 记录presenter的类名,当一个页面需要持有多个Presenter时使用
     */
    public String presenter;

    /**
     * An arbitrary object to send to the recipient.  When using
     * {@link Messenger} to send the message across processes this can only
     * be non-null if it contains a Parcelable of a framework class (not one
     * implemented by the application).   For other data transfer use
     * {@link #setData}.
     * <p>
     * <p>Note that Parcelable objects here are not supported prior to
     * the {@link Build.VERSION_CODES#FROYO} release.
     */
    public Object obj;


    public Object[] objs;

    /**
     * Optional Messenger where replies to this message can be sent.  The
     * semantics of exactly how this is used are up to the sender and
     * receiver.
     */
    public Messenger replyTo;

    /**
     * Optional field indicating the uid that sent the message.  This is
     * only valid for messages posted by a {@link Messenger}; otherwise,
     * it will be -1.
     */
    public int sendingUid = -1;

    /**
     * If set message is in use.
     * This flag is set when the message is enqueued and remains set while it
     * is delivered and afterwards when it is recycled.  The flag is only cleared
     * when a new message is created or obtained since that is the only time that
     * applications are allowed to modify the contents of the message.
     * <p>
     * It is an error to attempt to enqueue or recycle a message that is already in use.
     */
    /*package*/ static final int FLAG_IN_USE = 1 << 0;

    /**
     * If set message is asynchronous
     */
    /*package*/ static final int FLAG_ASYNCHRONOUS = 1 << 1;

    /**
     * Flags to clear in the copyFrom method
     */
    /*package*/ static final int FLAGS_TO_CLEAR_ON_COPY_FROM = FLAG_IN_USE;

    /*package*/ int flags;


    /*package*/ Bundle data;

    /*package*/ IView target;

    // sometimes we store linked lists of these things
    /*package*/ Message next;

    private static final Object sPoolSync = new Object();
    private static Message sPool;
    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;

    private static boolean gCheckRecycle = true;

    /**
     * Return a new Message instance from the global pool. Allows us to
     * avoid allocating new objects in many cases.
     */
    public static Message obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new Message();
    }

    /**
     * Same as {@link #obtain()}, but copies the values of an existing
     * message (including its target) into the new one.
     *
     * @param orig Original message to copy.
     * @return A Message object from the global pool.
     */
    public static Message obtain(Message orig) {
        Message m = obtain();
        m.what = orig.what;
        m.str = orig.str;
        m.presenter = orig.presenter;
        m.arg1 = orig.arg1;
        m.arg2 = orig.arg2;
        m.obj = orig.obj;
        m.objs = orig.objs;
        m.replyTo = orig.replyTo;
        m.sendingUid = orig.sendingUid;
        if (orig.data != null) {
            m.data = new Bundle(orig.data);
        }
        m.target = orig.target;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the value for the <em>target</em> member on the Message returned.
     *
     * @param v Handler to assign to the returned Message object's <em>target</em> member.
     * @return A Message object from the global pool.
     */
    public static Message obtain(IView v) {
        Message m = obtain();
        m.target = v;
        return m;
    }

    public static Message obtain(IView v, Object obj) {
        Message m = obtain();
        m.target = v;
        m.obj = obj;
        return m;
    }

    public static Message obtain(IView v, Object[] objs) {
        Message m = obtain();
        m.target = v;
        m.objs = objs;
        return m;
    }


    public static Message obtain(IView v, Class presenter) {
        Message m = obtain();
        m.target = v;
        m.presenter = presenter.getSimpleName();
        return m;
    }


    public static Message obtain(IView v, Object obj, Class presenter) {
        Message m = obtain();
        m.target = v;
        m.obj = obj;
        m.presenter = presenter.getSimpleName();
        return m;
    }

    public static Message obtain(IView v, Object[] objs, Class presenter) {
        Message m = obtain();
        m.target = v;
        m.objs = objs;
        m.presenter = presenter.getSimpleName();
        return m;
    }


    /**
     * Same as {@link #obtain()}, but sets the values for both <em>target</em> and
     * <em>what</em> members on the Message.
     *
     * @param v    Value to assign to the <em>target</em> member.
     * @param what Value to assign to the <em>what</em> member.
     * @return A Message object from the global pool.
     */
    public static Message obtain(IView v, int what) {
        Message m = obtain();
        m.target = v;
        m.what = what;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>, and <em>obj</em>
     * members.
     *
     * @param v    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param obj  The <em>object</em> method to set.
     * @return A Message object from the global pool.
     */
    public static Message obtain(IView v, int what, Object obj) {
        Message m = obtain();
        m.target = v;
        m.what = what;
        m.obj = obj;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>,
     * <em>arg1</em>, and <em>arg2</em> members.
     *
     * @param v    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @return A Message object from the global pool.
     */
    public static Message obtain(IView v, int what, int arg1, int arg2) {
        Message m = obtain();
        m.target = v;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;

        return m;
    }

    /**
     * Same as {@link #obtain()}, but sets the values of the <em>target</em>, <em>what</em>,
     * <em>arg1</em>, <em>arg2</em>, and <em>obj</em> members.
     *
     * @param v    The <em>target</em> value to set.
     * @param what The <em>what</em> value to set.
     * @param arg1 The <em>arg1</em> value to set.
     * @param arg2 The <em>arg2</em> value to set.
     * @param obj  The <em>obj</em> value to set.
     * @return A Message object from the global pool.
     */
    public static Message obtain(IView v, int what,
                                 int arg1, int arg2, Object obj) {
        Message m = obtain();
        m.target = v;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;

        return m;
    }

    /**
     * 这个消息是否是这个Presenter发送的
     *
     * @param presenter
     * @return
     */
    public boolean isFromThisPresenter(Class presenter) {
        return this.presenter.equals(presenter.getSimpleName());
    }

    /**
     * @hide
     */
    public static void updateCheckRecycle(int targetSdkVersion) {
        if (targetSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            gCheckRecycle = false;
        }
    }

    /**
     * Return a Message instance to the global pool.
     * <p>
     * You MUST NOT touch the Message after calling this function because it has
     * effectively been freed.  It is an error to recycle a message that is currently
     * enqueued or that is in the process of being delivered to a Handler.
     * </p>
     */
    public void recycle() {
        if (isInUse()) {
            if (gCheckRecycle) {
                throw new IllegalStateException("This message cannot be recycled because it "
                        + "is still in use.");
            }
            return;
        }
        recycleUnchecked();
    }

    /**
     * Recycles a Message that may be in-use.
     * Used internally by the MessageQueue and Looper when disposing of queued Messages.
     */
    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        objs = null;
        str = null;
        presenter = null;
        replyTo = null;
        sendingUid = -1;
        target = null;
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

    /**
     * Make this message like o.  Performs a shallow copy of the data field.
     * Does not copy the linked list fields, nor the timestamp or
     * target/callback of the original message.
     */
    public void copyFrom(Message o) {
        this.flags = o.flags & ~FLAGS_TO_CLEAR_ON_COPY_FROM;
        this.what = o.what;
        this.str = o.str;
        this.presenter = o.presenter;
        this.arg1 = o.arg1;
        this.arg2 = o.arg2;
        this.obj = o.obj;
        this.objs = o.objs;
        this.replyTo = o.replyTo;
        this.sendingUid = o.sendingUid;

        if (o.data != null) {
            this.data = (Bundle) o.data.clone();
        } else {
            this.data = null;
        }
    }


    public void setTarget(IView target) {
        this.target = target;
    }

    /**
     * Retrieve the a {@link Handler Handler} implementation that
     * will receive this message. The object must implement
     * {@link Handler#handleMessage(android.os.Message)
     * Handler.handleMessage()}. Each Handler has its own name-space for
     * message codes, so you do not need to
     * worry about yours conflicting with other handlers.
     */
    public IView getTarget() {
        return target;
    }


    /**
     * Obtains a Bundle of arbitrary data associated with this
     * event, lazily creating it if necessary. Set this value by calling
     * {@link #setData(Bundle)}.  Note that when transferring data across
     * processes via {@link Messenger}, you will need to set your ClassLoader
     * on the Bundle via {@link Bundle#setClassLoader(ClassLoader)
     * Bundle.setClassLoader()} so that it can instantiate your objects when
     * you retrieve them.
     *
     * @see #peekData()
     * @see #setData(Bundle)
     */
    public Bundle getData() {
        if (data == null) {
            data = new Bundle();
        }

        return data;
    }

    /**
     * Like getData(), but does not lazily create the Bundle.  A null
     * is returned if the Bundle does not already exist.  See
     * {@link #getData} for further information on this.
     *
     * @see #getData()
     * @see #setData(Bundle)
     */
    public Bundle peekData() {
        return data;
    }

    /**
     * Sets a Bundle of arbitrary data values. Use arg1 and arg2 members
     * as a lower cost way to send a few simple integer values, if you can.
     *
     * @see #getData()
     * @see #peekData()
     */
    public void setData(Bundle data) {
        this.data = data;
    }

    /**
     * 废弃,使用 {@link Message#handleMessageToTarget} 代替
     * 分发消息(这里需要自己切换线程),调用 {@link IView#handleMessage(Message)} 处理消息后
     * 将消息放入消息池,供下次 {@link #obtain()}
     */
    @Deprecated
    public void HandleMessageToTarget() {
        if (target == null) throw new IllegalArgumentException("target is null");
        target.handleMessage(this);
        this.recycleUnchecked();
    }

    /**
     * 分发消息(这里需要自己切换线程),调用 {@link IView#handleMessage(Message)} 处理消息后
     * 将消息放入消息池,供下次 {@link #obtain()}
     */
    public void handleMessageToTarget() {
        if (target == null) throw new IllegalArgumentException("target is null");
        target.handleMessage(this);
        this.recycleUnchecked();
    }

    /**
     * 废弃,使用 {@link Message#handleMessageToTargetUnrecycle} 代替
     * 分发消息(这里需要自己切换线程),调用 {@link IView#handleMessage(Message)} 处理消息
     */
    @Deprecated
    public void HandleMessageToTargetUnrecycle() {
        if (target == null) throw new IllegalArgumentException("target is null");
        target.handleMessage(this);
    }

    /**
     * 分发消息(这里需要自己切换线程),调用 {@link IView#handleMessage(Message)} 处理消息
     */
    public void handleMessageToTargetUnrecycle() {
        if (target == null) throw new IllegalArgumentException("target is null");
        target.handleMessage(this);
    }

    /**
     * Returns true if the message is asynchronous, meaning that it is not
     * subject to {@link Looper} synchronization barriers.
     *
     * @return True if the message is asynchronous.
     * @see #setAsynchronous(boolean)
     */
    public boolean isAsynchronous() {
        return (flags & FLAG_ASYNCHRONOUS) != 0;
    }

    /**
     * Sets whether the message is asynchronous, meaning that it is not
     * subject to {@link Looper} synchronization barriers.
     * <p>
     * Certain operations, such as view invalidation, may introduce synchronization
     * barriers into the {@link Looper}'s message queue to prevent subsequent messages
     * from being delivered until some condition is met.  In the case of view invalidation,
     * messages which are posted after a call to {@link android.view.View#invalidate}
     * are suspended by means of a synchronization barrier until the next frame is
     * ready to be drawn.  The synchronization barrier ensures that the invalidation
     * request is completely handled before resuming.
     * </p><p>
     * Asynchronous messages are exempt from synchronization barriers.  They typically
     * represent interrupts, input events, and other signals that must be handled independently
     * even while other work has been suspended.
     * </p><p>
     * Note that asynchronous messages may be delivered out of order with respect to
     * synchronous messages although they are always delivered in order among themselves.
     * If the relative order of these messages matters then they probably should not be
     * asynchronous in the first place.  Use with caution.
     * </p>
     *
     * @param async True if the message is asynchronous.
     * @see #isAsynchronous()
     */
    public void setAsynchronous(boolean async) {
        if (async) {
            flags |= FLAG_ASYNCHRONOUS;
        } else {
            flags &= ~FLAG_ASYNCHRONOUS;
        }
    }

    /*package*/ boolean isInUse() {
        return ((flags & FLAG_IN_USE) == FLAG_IN_USE);
    }

    /*package*/ void markInUse() {
        flags |= FLAG_IN_USE;
    }

    /**
     * Constructor (but the preferred way to get a Message is to call {@link #obtain() Message.obtain()}).
     */
    public Message() {
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{");

        if (target != null) {

            b.append(" what=");
            b.append(what);

            if (!TextUtils.isEmpty(presenter)) {
                b.append(" presenter=");
                b.append(presenter);
            }

            if (!TextUtils.isEmpty(str)) {
                b.append(" str=");
                b.append(str);
            }


            if (arg1 != 0) {
                b.append(" arg1=");
                b.append(arg1);
            }

            if (arg2 != 0) {
                b.append(" arg2=");
                b.append(arg2);
            }

            if (obj != null) {
                b.append(" obj=");
                b.append(obj);
            }

            b.append(" target=");
            b.append(target.getClass().getName());
        } else {
            b.append(" barrier=");
            b.append(arg1);
        }

        b.append(" }");
        return b.toString();
    }

    public static final Creator<Message> CREATOR
            = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            Message msg = Message.obtain();
            msg.readFromParcel(source);
            return msg;
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(what);
        dest.writeInt(arg1);
        dest.writeInt(arg2);
        dest.writeString(str);
        dest.writeString(presenter);
        if (obj != null) {
            try {
                Parcelable p = (Parcelable) obj;
                dest.writeInt(1);
                dest.writeParcelable(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        } else {
            dest.writeInt(0);
        }

        if (objs != null) {
            try {
                Parcelable[] p = (Parcelable[]) objs;
                dest.writeInt(1);
                dest.writeParcelableArray(p, flags);
            } catch (ClassCastException e) {
                throw new RuntimeException(
                        "Can't marshal non-Parcelable objects across processes.");
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeBundle(data);
        Messenger.writeMessengerOrNullToParcel(replyTo, dest);
        dest.writeInt(sendingUid);
    }


    private void readFromParcel(Parcel source) {
        what = source.readInt();
        arg1 = source.readInt();
        arg2 = source.readInt();
        str = source.readString();
        presenter = source.readString();
        if (source.readInt() != 0) {
            obj = source.readParcelable(getClass().getClassLoader());
        }
        if (source.readInt() != 0) {
            objs = source.readParcelableArray(getClass().getClassLoader());
        }
        data = source.readBundle();
        replyTo = Messenger.readMessengerOrNullFromParcel(source);
        sendingUid = source.readInt();
    }
}

