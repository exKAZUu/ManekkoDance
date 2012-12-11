package jp.eclipcebook;

import java.io.IOException;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * EditText内に画像を配置できるカスタムEditText
 */
public class ImageInEdit extends EditText {

	/** テキストサイズ */
	private int mTextSize;

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 *            Context
	 */
	public ImageInEdit(Context context) {
		super(context);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            属性
	 */
	public ImageInEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            属性
	 * @param defStyle
	 *            スタイル
	 */
	public ImageInEdit(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		mTextSize = 3 * (int) getTextSize();

	}

	/**
	 * リソースIDから画像を挿入
	 * 
	 * @param context
	 *            Context
	 * @param id
	 *            リソースID
	 */
	public void insertResourceImage(Context context, int id) {
		Drawable drawable = context.getResources().getDrawable(id);
		insertImage(drawable);
	}

	/**
	 * assets内の画像を挿入
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            assets内パス
	 */
	public void insertAssetsImage(Context context, String path) {
		try {
			Drawable drawable = Drawable.createFromStream(context.getAssets().open(path), null);
			insertImage(drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drawabaleから画像を挿入
	 * 
	 * @param drawable
	 *            Drawable
	 */
	public void insertImage(final Drawable drawable) {

		ImageGetter imageGetter = new ImageGetter() {
			@Override
			public Drawable getDrawable(String source) {
				drawable.setBounds(0, 0, mTextSize, mTextSize);
				return drawable;
			}
		};

		String img = "<img src=\"" + drawable.toString() + "\" />";

		Spanned spanned = Html.fromHtml(img, imageGetter, null);

		int start = this.getSelectionStart();
		int end = this.getSelectionEnd();

		this.getText().replace(Math.min(start, end), Math.max(start, end), spanned, 0,
				spanned.length());

	}
	
	public void replaceImage(Context context, int id) {
		Drawable drawable = context.getResources().getDrawable(id);
		replaceTextToImage(drawable);
	}
	
	public void replaceTextToImage(final Drawable drawable) {
		
		//this.setText(this.getText()); //文字テキストを絵文字テキストにコピー

		String[] commands = new String[] { "左腕を上げる", "左腕を下げる", "右腕を上げる", "右腕を下げる", "左足を上げる",
				"左足を下げる", "右足を上げる", "右足を下げる", "loop", "ここまで" };

		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		for (String command : commands) {
			// commandの位置を探す
			int start = 0;
			int end = 5;
			
			int i = -1;
//			while ((i = line.indexOf(command, i + 1)) >= 0) {
//				map.put(i, command);
//			}

			ImageGetter imageGetter = new ImageGetter() {
				@Override
				public Drawable getDrawable(String source) {
					drawable.setBounds(0, 0, mTextSize, mTextSize);
					return drawable;
				}
			};
			String img = "<img src=\"" + drawable.toString() + "\" />";
			Spanned spanned = Html.fromHtml(img, imageGetter, null);

			this.getText().replace(start, end, spanned, 0, spanned.length());
		}

	}
}
