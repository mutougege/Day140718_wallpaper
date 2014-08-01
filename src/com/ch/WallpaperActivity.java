package com.ch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher.ViewFactory;

public class WallpaperActivity extends Activity implements ViewFactory {
	
	public ImageSwitcher iSwitcher;
	public Gallery gallery;
	
	private boolean isGallery = true;
	private int imagePosition = 0;
	
	private ArrayList<String> imageSources;
	private static final int IMAGE_COUNT = 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
		initData();
		setupView();
		setupListener();
		
	}

	public void setupView() {
		iSwitcher = (ImageSwitcher) findViewById(R.id.image_switcher);
		iSwitcher.setFactory(this);
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ImageAdapter(getApplicationContext(),imageSources));
	}

	public void setupListener() {
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				int drawableId = 0;
				try {
					drawableId = R.drawable.class.getDeclaredField(
							"wallpaper" + position).getInt(this);
					imagePosition = position;
				} catch (Exception e) {
					e.printStackTrace();
				} 
				iSwitcher.setImageResource(drawableId);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		iSwitcher.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isGallery){
					
					gallery.setVisibility(ViewGroup.GONE);
					isGallery = false;
				}else{
					gallery.setVisibility(ViewGroup.VISIBLE);
					isGallery = true;
				}
				return false;
			}
		});
	}

	private void initData() {
		imageSources = new ArrayList<String>();
		for(int i=0;i<IMAGE_COUNT;i++){
			try {
				imageSources.add("wallpaper" + i);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.CENTER_CROP);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	/**
	 * 定义内部类
	 * 
	 * @author chenhua
	 * 
	 */
	private class ImageAdapter extends BaseAdapter {
		
		private Context mContext;
		private ArrayList<String> list;

		public ImageAdapter(Context c,ArrayList<String> list) {
			mContext = c;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();// 11张图片
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			int drawableId = 0;
			try {
				drawableId = R.drawable.class.getDeclaredField(list.get(position)).getInt(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			imageView.setLayoutParams(new Gallery.LayoutParams(120, 120));
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(drawableId);
			return imageView;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.setwallpaper);
		menu.add(0, 2, 2, R.string.abourt);
		return super.onCreateOptionsMenu(menu);
	}

	public void setWallpaper(InputStream paramInputStream) throws IOException {
		super.setWallpaper(paramInputStream);
		Toast.makeText(this, "设置成功", 1).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			
			Resources localResources = getBaseContext().getResources();
			InputStream localInputStream2 = localResources
					.openRawResource(getResources().getIdentifier(
							"wallpaper" + imagePosition, "drawable", "com.ch"));
			try {
				setWallpaper(localInputStream2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (item.getItemId() == 2) {
			Toast.makeText(getApplicationContext(), R.string.author,
					Toast.LENGTH_SHORT).show();
		}
		return super.onOptionsItemSelected(item);
	}


}