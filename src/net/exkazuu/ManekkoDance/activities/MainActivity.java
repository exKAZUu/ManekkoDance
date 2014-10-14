// Todoリスト
/* キーボードを出した時の画像潰れ解消法　http://d.hatena.ne.jp/Superdry/20110715/1310754502 */
/* フラグメントによるTabの実装 */
/* 絵文字の実装 */

package net.exkazuu.ManekkoDance.activities;

import java.util.ArrayList;
import java.util.List;

import jp.eclipcebook.R;
import net.exkazuu.ManekkoDance.DetectableSoftKeyLayout;
import net.exkazuu.ManekkoDance.IconContainer;
import net.exkazuu.ManekkoDance.ImageContainer;
import net.exkazuu.ManekkoDance.ImageInEdit;
import net.exkazuu.ManekkoDance.Lessons;
import net.exkazuu.ManekkoDance.command.StringCommandExecutor;
import net.exkazuu.ManekkoDance.command.StringCommandParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final int TEXT_VIEW = 1;
	private static final int IMAGE_VIEW = 0;
	private String lesson;
	private String message;
	private String text_data;

	private TextView textView;
	private DetectableSoftKeyLayout DSKLayout;
	private HorizontalScrollView iconList;

	private ImageContainer leftImages;
	private ImageContainer rightImages;

	private Thread thread;
	private CommandExecutor commandExecutor;

	@Override
	protected void onPause() {
		super.onPause();
		if (commandExecutor != null) {
			commandExecutor.died = true;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("編集画面");
		setContentView(R.layout.main);

		iconList = (HorizontalScrollView) findViewById(R.id.iconList);

		leftImages = ImageContainer.createPiyoLeft(this);
		rightImages = ImageContainer.createPiyoRight(this);

		/********** Lesson data　の 取得 **************/
		Intent intent = getIntent();
		lesson = intent.getStringExtra("lesson");
		message = intent.getStringExtra("message");
		text_data = intent.getStringExtra("text_data");
		Log.v("my_debug", "" + text_data);
		if (text_data != null) {

		}
		if (message.equals("3")) {

		}
		if (message.equals("4")) {

		}
		if (message.equals("5")) {

		}
		if (message.equals("6")) {

		}

		/******************* tabの実装と切り替え *****************/

		host = (TabHost) findViewById(R.id.tabhost);
		host.setup();

		TabSpec tab1 = host.newTabSpec("tab1");
		tab1.setIndicator("絵文字");
		tab1.setContent(R.id.tab1);
		host.addTab(tab1);

		/*
		 * TabSpec tab2 = host.newTabSpec("tab2"); tab2.setIndicator("絵文字");
		 * tab2.setContent(R.id.tab2); host.addTab(tab2);
		 */
		if (iconContainer == null) {
			iconContainer = new IconContainer(getApplication());
		}

		final MainActivity mainActivity = this;

		host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				Log.v("my_debug", "onTabChanged" + tabId);
				if (tabId == "tab2") {
					new Thread(new Runnable() {
						@Override
						public void run() {

							mainActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {

								}
							});
						}
					}).start();
				} else if (tabId == "tab1") { //

				}
			}
		});

		host.setCurrentTab(IMAGE_VIEW);

		/********** 音楽 **************/
//		View.OnClickListener piyoOnClickListener = new View.OnClickListener() {
//
//			public void onClick(View v) {
//				host.setCurrentTab(IMAGE_VIEW);
//				final Handler handler = new Handler();
//				if (thread == null || !thread.isAlive()) {
//					commandExecutor = new CommandExecutor(handler);
//					thread = new Thread(commandExecutor);
//					thread.start();
//				}
//			}
//		};
//		this.findViewById(R.id.frameLayoutPiyo).setOnClickListener(
//				piyoOnClickListener);
//		this.findViewById(R.id.frameLayoutPiyo2).setOnClickListener(
//				piyoOnClickListener);

		final Activity activity = this;
		final int limitation = Lessons.getLimitation(Integer.parseInt(message));
		final Button btnJudge = (Button) findViewById(R.id.btnJudge);

	}

	/******************** 構文解析＆実行 *************************/
	public final class CommandExecutor implements Runnable {
		private final Handler handler;
		private boolean died;

		private CommandExecutor(Handler handler) {
			this.handler = handler;
			this.died = false;
		}

		public void run() {

			textView = (TextView) findViewById(R.id.editText1);
			String commandsText = textView.getText().toString();
			// (imgTextView.getTextFromImage(iconContainer)); // 1行ずつ配列に収納

			List<String> leftCommands = new ArrayList<String>();
			List<Integer> leftNumbers = new ArrayList<Integer>();
			StringCommandParser.parse(commandsText, leftCommands, leftNumbers,
					true);

			List<String> rightCommands = new ArrayList<String>();
			List<Integer> rightNumbers = new ArrayList<Integer>();
			StringCommandParser.parse(commandsText, rightCommands,
					rightNumbers, false);

			executeCommands(leftImages, rightImages, leftCommands,
					rightCommands, leftNumbers, rightNumbers, textView);
		}

		private void executeCommands(ImageContainer leftImages,
				ImageContainer rightImages, List<String> leftCommands,
				List<String> rightCommands, List<Integer> leftNumbers,
				List<Integer> rightNumbers, TextView textView) {
			StringCommandExecutor leftRunnable = new StringCommandExecutor(
					leftImages, leftCommands, textView, leftNumbers,
					getApplicationContext(), true);
			StringCommandExecutor rightRunnable = new StringCommandExecutor(
					rightImages, rightCommands, textView, rightNumbers,
					getApplicationContext(), false);
			for (int i = 0; !died && i < leftCommands.size(); i++) { /* 解析&実行 */
				handler.post(leftRunnable); /* 光らせる */
				handler.post(rightRunnable); /* 光らせる */

				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				handler.post(leftRunnable);
				handler.post(rightRunnable);

				try { /* 1秒待機 */
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// for文から抜けて画像を初期化
				if (leftRunnable.existsError() || rightRunnable.existsError()) {
					handler.post(new Runnable() {
						public void run() {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									MainActivity.this);
							builder.setTitle("間違った文を書いているよ");
							builder.setPositiveButton("もう一度Challenge",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											initializeImage();
										}
									});
							builder.show();
						}
					});
					return;
				}
			}

			handler.post(new Runnable() {
				public void run() {
					initializeImage();
				}
			});
		}

	}

	private TabHost host;
	private static IconContainer iconContainer;


	public void initializeImage() {
		leftImages = ImageContainer.createPiyoLeft(this);
		rightImages = ImageContainer.createPiyoRight(this);
	}

	/******************** メニュー作成 *************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		MenuItem item1 = menu.add("ヘルプ");
		MenuItem item2 = menu.add("クリア");
		MenuItem item3 = menu.add("タイトル画面へ戻る");

		item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				changeHelpScreen();
				return false;
			}
		});
		item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				textView.getEditableText().clear();
				return false;
			}
		});
		item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				changeTitleScreen();
				return false;
			}
		});

		return true;
	}

	/************************* インテント（画面遷移） *****************************/
	public void changeActionScreen(View view) {
		host.setCurrentTab(TEXT_VIEW);
		Intent intent = new Intent(this,
				net.exkazuu.ManekkoDance.activities.ActionActivity.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	public void changePartnerScreen(View view) { // お手本画面へ遷移
		host.setCurrentTab(TEXT_VIEW);
		Intent intent = new Intent(this,
				net.exkazuu.ManekkoDance.activities.PartnerActivity.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		intent.putExtra("text_data", textView.getText().toString());
		this.startActivity(intent);
	}

	public void changeHelpScreen() { // ヘルプ画面へ遷移
		host.setCurrentTab(TEXT_VIEW);
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.Help.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	public void changeHelpScreen(View view) { // ヘルプ画面へ遷移
		host.setCurrentTab(TEXT_VIEW);
		Intent intent = new Intent(this, net.exkazuu.ManekkoDance.Help.class);
		intent.putExtra("lesson", lesson);
		intent.putExtra("message", message);
		this.startActivity(intent);
	}

	private void changeTitleScreen() {
		Intent intent = new Intent(this,
				net.exkazuu.ManekkoDance.activities.TitleActivity.class);
		this.startActivity(intent);
	}

	protected void onDestroy() {
		super.onDestroy();
		cleanupView(findViewById(R.id.root));
		System.gc();
	}

	public static final void cleanupView(View view) {
		if (view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			ib.setImageDrawable(null);
		} else if (view instanceof ImageView) {
			ImageView iv = (ImageView) view;
			iv.setImageDrawable(null);
			// } else if(view instanceof(XXX)) {
			// 他にもDrawableを使用する対象があればここで中身をnullに
		}
		view.setBackgroundDrawable(null);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			int size = vg.getChildCount();
			for (int i = 0; i < size; i++) {
				cleanupView(vg.getChildAt(i));
			}
		}
	}

}
