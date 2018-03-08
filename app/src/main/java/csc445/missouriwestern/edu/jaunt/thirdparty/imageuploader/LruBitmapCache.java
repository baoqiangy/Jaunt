package csc445.missouriwestern.edu.jaunt.thirdparty.imageuploader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by byan on 3/6/2018.
 */

public class LruBitmapCache extends LruCache {

    Context mContext;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context context){
        super(4 * 1024 * 1024);
        this.mContext = context;
    }

    @Override
    protected int sizeOf(Object p_key, Object p_value) {
        String key = (String) p_key;
        Bitmap value = (Bitmap) p_value;

        return super.sizeOf(key, value);
    }
}
