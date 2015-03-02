package cn.edu.sdu.online.fragment_second;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cn.edu.sdu.online.R;
import cn.edu.sdu.online.Listener.ActionBarListener;
import cn.edu.sdu.online.Listener.BackPressedListener;
import cn.edu.sdu.online.Listener.HomeButtonListener;
import cn.edu.sdu.online.app.Main;
import cn.edu.sdu.online.app.MainActivity;
import cn.edu.sdu.online.utils.DefineUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditInformation extends Fragment implements HomeButtonListener,
		BackPressedListener, OnMenuItemClickListener, ActionBarListener {

	EditText editname, editsex;
	EditText talktome, aim;
	Button save;
	Main app;
	MainActivity activity;
	ImageView imageface;
	View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
		app = Main.getApp();
		activity.getSupportActionBar().setIcon(R.drawable.icon_back);
		activity.setHomeButtonListener(this);
		activity.setBackKeyDownListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.frag_editinformation, null);
		initeView();
		setEditInfo();
		return view;
	}

	private void initeView() {
		imageface = (ImageView) view.findViewById(R.id.setface);
		imageface.setOnClickListener(new faceimageListener());
		editsex = (EditText) view.findViewById(R.id.editsex);
		editsex.setOnClickListener(new clickEditset());
		editname = (EditText) view.findViewById(R.id.editname);
		talktome = (EditText) view.findViewById(R.id.edittalktome);
		aim = (EditText) view.findViewById(R.id.editaim);

	}

	@SuppressLint("NewApi") 
	private void setEditInfo() {
		String editnamestore = app.getDataStore().getString("editname",
				app.getDataStore().getString("stuname", "未知"));
		String editsexstore = app.getDataStore().getString("Info_sex", "未知");
		String talktomestore = app.getDataStore().getString("Info_talktome",
				"总有些话是想对自己说的吧");
		String editaimstore = app.getDataStore().getString("Info_aim",
				"近期目标，砥砺自己前行");
		editsex.setText(editsexstore);
		talktome.setText(talktomestore);
		aim.setText(editaimstore);
		editname.setText(editnamestore);
		Resources res = getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.faceimage);
		Drawable drawable = new BitmapDrawable(bmp);
		if (app.getDataStore().getBoolean("FACEIMAGE", false)) {
			Bitmap bitmap = app.getLoacalBitmap(DefineUtil.SDPATH + "/"
					+ "faceimage.png");
			imageface.setImageBitmap(bitmap);
		} else {
			imageface.setImageDrawable(drawable);
		}
		activity.setActionBarListener(this);
		activity.invalidateOptionsMenu();

	}


	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	public void onHomeButtonClick() {
		back();
	}

	@SuppressLint("NewApi")
	private void back() {
		activity.setAnimIn(R.anim.right_push_in);
		activity.setAnimOut(R.anim.right_push_out);
		activity.switchFragment(activity.getMenuItems().get(Main.INT_INFO).fragment);
		activity.setAnimIn(0);
		activity.setAnimOut(0);
		activity.setHomeButtonListener(null);
		activity.setBackKeyDownListener(null);
		activity.setActionBarListener(null);
		activity.invalidateOptionsMenu();
		activity.getSupportActionBar().setIcon(R.drawable.icon_topbar);
	}

	class clickEditset implements OnClickListener {
		int selctsexIndex;
		final String[] arraysex = new String[] { "男", "女" };

		@Override
		public void onClick(View v) {
			Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle("请选择性别");
			alertDialog.setSingleChoiceItems(arraysex, 0,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							selctsexIndex = which;
						}
					});
			alertDialog.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							editsex.setText(arraysex[selctsexIndex]);
						}
					});
			alertDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});

			alertDialog.create();
			alertDialog.show();
		}

	}

	@SuppressLint("NewApi") @Override
	public void onPrepare(Menu menu) {
		MenuItem menuitem = menu.add("保存");
		menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menuitem.setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		storeData();
		back();
		uploadInformation();
		testUpload();
		return true;
	}

	private void storeData() {
		String editsexstr = editsex.getText().toString();
		String edittalk = talktome.getText().toString();
		String editaim = aim.getText().toString();
		String editnamestr = editname.getText().toString();
		if (photo != null) {
			app.writetofile_faceimage(photo);
			app.getDataStore().edit().putBoolean("FACEIMAGE", true).commit();
		}

		app.getDataStore().edit().putString("Info_sex", editsexstr)
				.putString("Info_name", editnamestr)
				.putString("Info_talktome", edittalk)
				.putString("Info_aim", editaim).commit();
	}

	class faceimageListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			showDialog();
		}

	}

	private void showDialog() {

		new AlertDialog.Builder(EditInformation.this.getActivity())
				.setTitle("设置头像")
				.setItems(new String[] { "上传图片", "拍照" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Intent intentFromGallery = new Intent();
									intentFromGallery.setType("image/*");
									intentFromGallery
											.setAction(Intent.ACTION_GET_CONTENT);
									startActivityForResult(intentFromGallery,
											DefineUtil.IMAGE_REQUEST_CODE);
									break;
								case 1:

									Intent intentFromCapture = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);

									if (hasSdcard()) {

										intentFromCapture.putExtra(
												MediaStore.EXTRA_OUTPUT,
												Uri.fromFile(new File(
														Environment
																.getExternalStorageDirectory(),
														DefineUtil.IMAGE_FILE_NAME)));
									}
									startActivityForResult(intentFromCapture,
											DefineUtil.CAMERA_REQUEST_CODE);
									break;
								}
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != DefineUtil.RESULT_CANCELED) {
			switch (requestCode) {
			case DefineUtil.IMAGE_REQUEST_CODE:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case DefineUtil.CAMERA_REQUEST_CODE:
				if (hasSdcard()) {

					File tempFile = new File(
							Environment.getExternalStorageDirectory(),
							DefineUtil.IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(EditInformation.this.getActivity(),
							"没有找到存储卡，不能存储照片", Toast.LENGTH_LONG).show();
				}

				break;
			case DefineUtil.RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", DefineUtil.IMAGEVIEW_WIDTH);
		intent.putExtra("outputY", DefineUtil.IMAGEVIEW_HEIGHT);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */

	private static final String SDPATH = Environment
			.getExternalStorageDirectory() + "";
	Bitmap photo;

	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			imageface.setImageDrawable(drawable);

		}

	}

	public void testUpload() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("Type", "uploadFile");
		params.addBodyParameter("file",
				new File(SDPATH + "/" + "faceimage.png"));
		params.addBodyParameter("id",
				app.getDataStore().getString("username", ""));
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {

						System.out.println("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							System.out.println("upload: " + current + "/"
									+ total);
						} else {
							System.out.println("reply: " + current + "/"
									+ total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("reply: " + responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(msg);
					}
				});
	}

	public void uploadInformation() {
		System.out.println("zhi xingl e uploadInformation");
		String editname = app.getDataStore().getString("Info_name",
				app.getDataStore().getString("stuname", ""));
		String editsex = app.getDataStore().getString("Info_sex", "未知");
		String edittalktome = app.getDataStore().getString("Info_talktome",
				"总有想对自己说的话吧");
		String editaim = app.getDataStore()
				.getString("Info_aim", "近期目标，砥砺自己前行");

		String id = app.getDataStore().getString("username", "");
		RequestParams params = new RequestParams();
		params.addBodyParameter("Type", "uploadInformation");
		params.addBodyParameter("name", editname);
		params.addBodyParameter("sex", editsex);
		params.addBodyParameter("talktome", edittalktome);
		params.addBodyParameter("aim", editaim);
		params.addBodyParameter("id", id);
		HttpUtils http = new HttpUtils();
		http.configTimeout(5000);
		http.send(HttpRequest.HttpMethod.POST, DefineUtil.PATH, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(app, "保存失败，网络或其他异常", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						Toast.makeText(app, "保存成功", Toast.LENGTH_SHORT).show();
					}
				});

	}
}
