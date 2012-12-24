package entities;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.flipviewproblematiclayout.R;

public class GalleryFlipItem extends LinearLayout
{
	private class ContentPhotosDownloader extends AsyncTask<Void, Void, Void>
	{
		private Context context;

		private String url;
		private Bitmap response;
		private ImageView imgView;

		private InputStream is;
		private BufferedInputStream bis;
		private URLConnection conn;

		public ContentPhotosDownloader(Context context, String url, ImageView imgView)
		{
			this.url = url;
			this.context = context;
			this.imgView = imgView;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			pd.setVisibility(View.VISIBLE);
			imgBackground.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			try
			{
				URL aURL = new URL(url);
				conn = aURL.openConnection();

				conn.connect();
				is = conn.getInputStream();
				bis = new BufferedInputStream(is);

				Bitmap bim = BitmapFactory.decodeStream(bis);

				response = bim;

				bis.close();
				is.close();

				bis = null;
				is = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);

			if (response != null)
			{
				if (imgView != null)
				{
					imgView.setImageBitmap(response);
					imgView.setVisibility(View.VISIBLE);
				}
				if (pd != null)
					pd.setVisibility(View.GONE);
			}
		}
	}

	public GalleryPage mGalleryPage;

	private ImageView imgBackground;
	private ProgressBar pd;
	private LinearLayout lnlPlace;

	private ImageView imgIcon;
	private TextView txtName;
	private TextView txtDistrict;
	private TextView txtCity;

	private Context mContext;

	public void refreshView(GalleryPage mGalleryPage)
	{
		this.mGalleryPage = mGalleryPage;

		new ContentPhotosDownloader(mContext, this.mGalleryPage.getTargetURL(), imgIcon).execute();

		txtName.setText(this.mGalleryPage.getPageTitle());

		new ContentPhotosDownloader(mContext, this.mGalleryPage.getImageURL(), imgBackground).execute();
	}

	@SuppressWarnings("deprecation")
	public GalleryFlipItem(Context context, GalleryPage mGalleryPage)
	{
		super(context);
		mContext = context;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.gallery_flip_item_layout, this);

		pd = (ProgressBar) findViewById(R.id.gallery_flip_item_background_progressbar);
		imgBackground = (ImageView) findViewById(R.id.gallery_flip_item_background_imageview);

		lnlPlace = (LinearLayout) findViewById(R.id.gallery_flip_item_place_linearlayout);

		imgIcon = (ImageView) findViewById(R.id.gallery_flip_item_place_icon_imageview);
		txtName = (TextView) findViewById(R.id.gallery_flip_item_place_name_textview);
		txtDistrict = (TextView) findViewById(R.id.gallery_flip_item_place_district_textview);
		txtCity = (TextView) findViewById(R.id.gallery_flip_item_place_city_textview);

		int Measuredwidth = 0;
		int Measuredheight = 0;
		Point size = new Point();
		WindowManager w = ((Activity) context).getWindowManager();

		Display d = w.getDefaultDisplay();
		Measuredwidth = d.getWidth();
		Measuredheight = d.getHeight();

		imgBackground.setLayoutParams(new LinearLayout.LayoutParams(Measuredwidth, Measuredheight));
		refreshView(mGalleryPage);
	}
}
