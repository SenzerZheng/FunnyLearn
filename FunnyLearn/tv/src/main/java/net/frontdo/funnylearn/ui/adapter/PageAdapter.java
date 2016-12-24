package net.frontdo.funnylearn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.frontdo.funnylearn.R;

import java.util.List;

/**
 * ProjectName: PageAdapter
 * Description:
 * <p>
 * author: JeyZheng
 * version: 4.0
 * created at: 11/14/2016 23:15
 */
public class PageAdapter extends PagerAdapter {
    private List picUrls;
    private Context context;

    public PageAdapter(Context context, List picUrls) {
        this.context = context;
        this.picUrls = picUrls;
    }

    @Override
    public int getCount() {
        return picUrls.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_app_img, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_app_img);

//        String picUrl = (String) picUrls.get(position);
//        PhotoLoader.display(container.getContext(), imageView, picUrl, R.mipmap.ic_launcher);
        container.addView(view);
        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

}
