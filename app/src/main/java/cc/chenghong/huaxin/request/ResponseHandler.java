package cc.chenghong.huaxin.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.ByteArrayBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;

public abstract class ResponseHandler<T> implements ResponseHandlerInterface {

    private static final String TAG = ResponseHandler.class.getSimpleName();

    protected static final int SUCCESS_MESSAGE = 0;
    protected static final int FAILURE_MESSAGE = 1;
    protected static final int START_MESSAGE = 2;
    protected static final int FINISH_MESSAGE = 3;
    protected static final int PROGRESS_MESSAGE = 4;
    protected static final int RETRY_MESSAGE = 5;
    protected static final int CANCEL_MESSAGE = 6;

    protected static final int BUFFER_SIZE = 4096;

    public static final String DEFAULT_CHARSET = "UTF-8";
    private String responseCharset = DEFAULT_CHARSET;
    private Handler handler;
    private boolean useSynchronousMode;
    /**
     * 泛型类
     */
    private Class mClass;

    private URI requestURI = null;
    private Header[] requestHeaders = null;

    @Override
    public URI getRequestURI() {
        return this.requestURI;
    }

    @Override
    public Header[] getRequestHeaders() {
        return this.requestHeaders;
    }

    @Override
    public void setRequestURI(URI requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public void setRequestHeaders(Header[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    /**
     * Avoid leaks by using a non-anonymous handler class.
     */
    private static class ResponderHandler extends Handler {
        private final ResponseHandler mResponder;

        ResponderHandler(ResponseHandler mResponder) {
            this.mResponder = mResponder;
        }

        @Override
        public void handleMessage(Message msg) {
            mResponder.handleMessage(msg);
        }
    }

    @Override
    public boolean getUseSynchronousMode() {
        return useSynchronousMode;
    }

    @Override
    public void setUseSynchronousMode(boolean value) {
        // A looper must be prepared before setting asynchronous mode.
        if (!value && Looper.myLooper() == null) {
            value = true;
            Log.w(TAG, "Current thread has not called Looper.prepare(). Forcing synchronous mode.");
        }

        // If using asynchronous mode.
        if (!value && handler == null) {
            // Create a handler on current thread to submit tasks
            handler = new ResponderHandler(this);
        } else if (value && handler != null) {
            // TODO: Consider adding a flag to remove all queued messages.
            handler = null;
        }

        useSynchronousMode = value;
    }

    /**
     * Sets the charset for the response string. If not set, the default is UTF-8.
     *
     * @param charset to be used for the response string.
     * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/nio/charset/Charset.html">Charset</a>
     */
    public void setCharset(final String charset) {
        this.responseCharset = charset;
    }

    public String getCharset() {
        return this.responseCharset == null ? DEFAULT_CHARSET : this.responseCharset;
    }

    /**
     * Creates a new ResponseHandler
     */
    public ResponseHandler() {
        // Use asynchronous mode by default.
        setUseSynchronousMode(false);
        this.mClass = this.getSuperClassGenricType(getClass(), 0);
    }
    private Class clazz;
    public ResponseHandler(Context context) {
        // Use asynchronous mode by default.
        setUseSynchronousMode(false);
        clazz = context.getClass();
        this.mClass = this.getSuperClassGenricType(getClass(), 0);
    }

    /**
     * Fired when the request progress, override to handle in your own code
     *
     * @param bytesWritten offset from start of file
     * @param totalSize    total size of file
     */
    public void onProgress(int bytesWritten, int totalSize) {
        Log.v(TAG, String.format("Progress %d from %d (%2.0f%%)", bytesWritten, totalSize, (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1));
    }

    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart() {
    }

    /**
     * Fired in all cases when the request is finished, after both success and failure, override to
     * handle in your own code
     */
    public void onFinish() {
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode the status code of the response
     */
    public abstract void onSuccess(int statusCode, T obj, Header[] headers);

    /**
     * Fired when a request fails to complete, override to handle in your own code
     *
     * @param statusCode   return HTTP status code
     * @param headers      return headers, if any
     * @param responseBody the response body, if any
     * @param error        the underlying cause of the failure
     */
    public abstract void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error);

    /**
     * Fired when a retry occurs, override to handle in your own code
     *
     * @param retryNo number of retry
     */
    public void onRetry(int retryNo) {
        Log.d(TAG, String.format("Request retry no. %d", retryNo));
    }

    public void onCancel() {
        Log.d(TAG, "Request got cancelled");
    }

    final public void sendProgressMessage(int bytesWritten, int bytesTotal) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{bytesWritten, bytesTotal}));
    }

    final public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBytes) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{statusCode, headers, responseBytes}));
    }

    final public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode, headers, responseBody, throwable}));
    }

    final public void sendStartMessage() {
        sendMessage(obtainMessage(START_MESSAGE, null));
    }

    final public void sendFinishMessage() {
        sendMessage(obtainMessage(FINISH_MESSAGE, null));
    }

    final public void sendRetryMessage(int retryNo) {
        sendMessage(obtainMessage(RETRY_MESSAGE, new Object[]{retryNo}));
    }

    final public void sendCancelMessage() {
        sendMessage(obtainMessage(CANCEL_MESSAGE, null));
    }

    // Methods which emulate android's Handler and Message methods
    protected void handleMessage(Message message) {
        Object[] response;

        switch (message.what) {
            case SUCCESS_MESSAGE://网络连接成功，处理返回的数据
                response = (Object[]) message.obj;
                if (response != null && response.length >= 3) {
                    String str = new String((byte[]) response[2]);
                    Object obj = new Gson().fromJson(str, mClass);
                    if (clazz==null){
                        Log.i(TAG, str);
                    }else{
                        Log.i(clazz.getSimpleName(), str);
                    }
                    onSuccess((Integer) response[0], (T) obj, (Header[]) response[1]);
                } else {
                    Log.e(TAG, "SUCCESS_MESSAGE didn't got enough params");
                }
                break;
            case FAILURE_MESSAGE://网络连接失败，处理返回的错误
                response = (Object[]) message.obj;
                if (response != null && response.length >= 4) {
                    String responseBody = "";
                    if (response[2] != null) {
                        responseBody = new String((byte[]) response[2]);
                    }
                    onFailure((Integer) response[0], (Header[]) response[1], responseBody, (Throwable) response[3]);
                } else {
                    Log.e(TAG, "FAILURE_MESSAGE didn't got enough params");
                }
                break;
            case START_MESSAGE:
                onStart();
                break;
            case FINISH_MESSAGE:
                onFinish();
                break;
            case PROGRESS_MESSAGE://进度消息
                response = (Object[]) message.obj;
                if (response != null && response.length >= 2) {
                    try {
                        onProgress((Integer) response[0], (Integer) response[1]);
                    } catch (Throwable t) {
                        Log.e(TAG, "custom onProgress contains an error", t);
                    }
                } else {
                    Log.e(TAG, "PROGRESS_MESSAGE didn't got enough params");
                }
                break;
            case RETRY_MESSAGE://重试消息
                response = (Object[]) message.obj;
                if (response != null && response.length == 1)
                    onRetry((Integer) response[0]);
                else
                    Log.e(TAG, "RETRY_MESSAGE didn't get enough params");
                break;
            case CANCEL_MESSAGE://关闭消息
                onCancel();
                break;
        }
    }

    protected void sendMessage(Message msg) {
        if (getUseSynchronousMode() || handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) { // do not send messages if request has been cancelled
            handler.sendMessage(msg);
        }
    }

    /**
     * Helper method to send runnable into local handler loop
     *
     * @param runnable runnable instance, can be null
     */
    protected void postRunnable(Runnable runnable) {
        if (runnable != null) {
            if (getUseSynchronousMode() || handler == null) {
                // This response handler is synchronous, run on current thread
                runnable.run();
            } else {
                // Otherwise, run on provided handler
                handler.post(runnable);
            }
        }
    }

    /**
     * Helper method to create Message instance from handler
     *
     * @param responseMessageId   constant to identify Handler message
     * @param responseMessageData object to be passed to message receiver
     * @return Message instance, should not be null
     */
    protected Message obtainMessage(int responseMessageId, Object responseMessageData) {
        Message msg;
        if (handler == null) {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessageId;
                msg.obj = responseMessageData;
            }
        } else {
            msg = Message.obtain(handler, responseMessageId, responseMessageData);
        }
        return msg;
    }

    @Override
    public void sendResponseMessage(HttpResponse response) throws IOException {
        // do not process if request has been cancelled
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            byte[] responseBody;
            responseBody = getResponseData(response.getEntity());
            // additional cancellation check as getResponseData() can take non-zero time to process
            if (!Thread.currentThread().isInterrupted()) {
                if (status.getStatusCode() >= 300) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
                } else {
                    sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
                }
            }
        }
    }

    /**
     * Returns byte array of response HttpEntity contents
     *
     * @param entity can be null
     * @return response entity body or null
     * @throws java.io.IOException if reading entity or creating byte array failed
     */
    byte[] getResponseData(HttpEntity entity) throws IOException {
        byte[] responseBody = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                long contentLength = entity.getContentLength();
                if (contentLength > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                int buffersize = (contentLength <= 0) ? BUFFER_SIZE : (int) contentLength;
                try {
                    ByteArrayBuffer buffer = new ByteArrayBuffer(buffersize);
                    try {
                        byte[] tmp = new byte[BUFFER_SIZE];
                        int l, count = 0;
                        // do not send messages if request has been cancelled
                        while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
                            count += l;
                            buffer.append(tmp, 0, l);
                            sendProgressMessage(count, (int) (contentLength <= 0 ? 1 : contentLength));
                        }
                    } finally {
                        AsyncHttpClient.silentCloseInputStream(instream);
                        AsyncHttpClient.endEntityViaReflection(entity);
                    }
                    responseBody = buffer.toByteArray();
                } catch (OutOfMemoryError e) {
                    System.gc();
                    throw new IOException("File too large to fit into available memory");
                }
            }
        }
        return responseBody;
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }

}
