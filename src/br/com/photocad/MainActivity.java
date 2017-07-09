package br.com.photocad;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.photocad.domain.WrapData;
import br.com.photocad.util.HttpConnection;
import br.com.photocad.R;

public class MainActivity extends Activity {
	private WrapData wd;
	private ImageView imageView;
	private EditText etName;
	private EditText etTelefone;
	private EditText etDescricao;
	private Button btTakePhoto;
	private Button btSdcardPhoto;
	private Button btSend;
	private static final int IMG_CAM = 1;
	private static final int IMG_SDCARD = 2;
	private String answer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		wd = new WrapData();
		
		imageView = (ImageView) findViewById(R.id.imageView);
		
		etName = (EditText) findViewById(R.id.etName);
		etTelefone = (EditText) findViewById(R.id.etTelefone);
		etDescricao = (EditText) findViewById(R.id.etDescriao);
		
		btSend = (Button) findViewById(R.id.btSend);
		btTakePhoto = (Button) findViewById(R.id.btTakePhoto);
		btSdcardPhoto = (Button) findViewById(R.id.btSdcardPhoto);
	}
	
	// Metodo que chama camera nativa do aparelho
		public void callIntentImgCam(View view){
			File file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, IMG_CAM);
		}
		
	// Metodo que chama o sd do aparelho
		public void callIntentImgSDCard(View view){
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, IMG_SDCARD);
		}
		
	// Metodo de click do botão que chama conexao
		public void sendData(View view){
			enableViews(false);
			
			wd.setUrl("http://www.loja.srv.br:9000/upload_teste/index.php");
			wd.setMethod("save-form");
			wd.setName(etName.getText().toString());
			wd.setTelefone(etTelefone.getText().toString());
			wd.setDescricao(etDescricao.getText().toString());
			
			new Thread(){
				public void run(){
					answer = HttpConnection.getSetDataWeb(wd);
					
					runOnUiThread(new Runnable(){
						public void run(){
							enableViews(true);
							try{
								answer = Integer.parseInt(answer) == 1 ? "Form enviado com sucesso!" : "FAIL!";
								Toast.makeText(MainActivity.this, answer, Toast.LENGTH_SHORT).show();
							}
							catch(NumberFormatException e){ e.printStackTrace(); }
						}
					});
				}
			}.start();
		}
	// Metodo status	
	public void enableViews(boolean status){
		btTakePhoto.setEnabled(status);
		btSdcardPhoto.setEnabled(status);
		btSend.setEnabled(status);
		etName.setEnabled(status);
		etTelefone.setEnabled(status);
		etDescricao.setEnabled(status);
		
		btSend.setText(status ? "Enviar" : "Enviando...");
	}
		
    // Metodo que captura a imagem e retorna 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		File file = null;
		
		if(data != null && requestCode == IMG_SDCARD && resultCode == RESULT_OK){
			Uri img = data.getData();
			String[] cols = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(img, cols, null, null, null);
			cursor.moveToFirst();
			
			int indexCol = cursor.getColumnIndex(cols[0]);
			String imgString = cursor.getString(indexCol);
			cursor.close();
			
			file = new File(imgString);
			if(file != null){
				wd.getImage().setResizedBitmap(file, 800, 600);
				wd.getImage().setMimeFromImgPath(file.getPath());
			}
		}
		else if(requestCode == IMG_CAM && resultCode == RESULT_OK){
			file = new File(android.os.Environment.getExternalStorageDirectory(), "img.png");
			if(file != null){
				wd.getImage().setResizedBitmap(file, 800, 600);
				wd.getImage().setMimeFromImgPath(file.getPath());
			}
		}
		
		
		if(wd.getImage().getBitmap() != null){
			imageView.setImageBitmap(wd.getImage().getBitmap());
		}
	}
}
