package com.prototype.soundservey;

import java.io.File;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.prototype.soundservey.GlobalVariable.ProgressListener;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RecordingSurveyFragment extends MainAbstractClass implements
		OnClickListener {

	View rootView;

	private MediaRecorder mediaRecorder;
	// File path of recorded audio
	String mFileName;
	private ImageView imageview_recorder;
	private boolean working;
	private Button button_sendData;
	private EditText edittext_name;
	private EditText edittext_address;
	private ProgressDialog progress;

	private int which_fragment;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.activity_main, container, false);

		which_fragment = getArguments().getInt(GlobalVariable.pos);
		imageview_recorder = (ImageView) rootView
				.findViewById(R.id.imageview_recorder);
		button_sendData = (Button) rootView.findViewById(R.id.button_sendData);
		edittext_name = (EditText) rootView.findViewById(R.id.edittext_name);
		edittext_address = (EditText) rootView
				.findViewById(R.id.edittext_address);
		imageview_recorder.setImageResource(R.drawable.start);
		imageview_recorder.setOnClickListener(this);
		button_sendData.setOnClickListener(this);
		working = false;
		return rootView;
	}

	public void startRecording() {
		// Verify that the device has a mic first
		PackageManager pmanager = getActivity().getPackageManager();
		if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
			// Set the file location for the audio
			File directory = new File(
					Environment.getExternalStorageDirectory(), "soundSurvey");
			if (!directory.exists()) {
				directory.mkdirs();
			}
			mFileName = directory.getAbsolutePath();
			mFileName += "/" + System.currentTimeMillis() + ".3gp";
			// Create the recorder
			mediaRecorder = new MediaRecorder();
			// Set the audio format and encoder
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// Setup the output location
			mediaRecorder.setOutputFile(mFileName);
			// Start the recording
			try {
				mediaRecorder.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaRecorder.start();
			working = true;
			initProgress();
			imageview_recorder.setImageResource(R.drawable.stop);

		} else { // no mic on device
			GlobalVariable.showToast(getActivity(),
					"This device doesn't have a mic!");
		}
		
	}

	void initProgress() {
		final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle(R.string.string_recording);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setButton(ProgressDialog.BUTTON_POSITIVE,
				"Save recording", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mProgressDialog.dismiss();
						StopRecording();
						insertToMediaDB(new File(mFileName));
					}
				});

		mProgressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE,
				"Stop recording", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						mProgressDialog.dismiss();
						StopRecording();

					}
				});

		mProgressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface p1) {
						StopRecording();
						
					}
				});

		mProgressDialog.show();
		GlobalVariable.showToast(getActivity(),
				getResources().getString(R.string.string_recording_started));

	}

	public void StopRecording() {
		// Stop the recording of the audio
		imageview_recorder.setImageResource(R.drawable.start);

		GlobalVariable.showToast(getActivity(),
				getResources().getString(R.string.string_recording_stopped));
		mediaRecorder.stop();
		mediaRecorder.reset();
		mediaRecorder.release();
		working = false;

	}

	public void PlaybeforeSending() {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(mFileName);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // must call prepare first
		mediaPlayer.start(); // then start
	}

	public void insertToMediaDB(File audiofile) {
		// Setup values for the media file
		ContentValues values = new ContentValues(4);
		long current = System.currentTimeMillis();
		values.put(MediaStore.Audio.Media.TITLE, audiofile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
		values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
		ContentResolver contentResolver = getActivity().getContentResolver();
		// Construct uris
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);

		// Trigger broadcast to add
		try {
			getActivity().sendBroadcast(
					new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

			GlobalVariable.showToast(getActivity(),
					"Saved to " + audiofile.getAbsolutePath());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imageview_recorder: {
			if (working) {
				// stop recording
				StopRecording();
				imageview_recorder.setImageResource(R.drawable.start);
			} else {
				// start recording
				startRecording();
				imageview_recorder.setImageResource(R.drawable.stop);

			}
		}
			break;
		case R.id.button_sendData: {

			// Remove the fragment

			getActivity().sendBroadcast(
					new Intent().setAction(MainActivity.class.getName())
							.putExtra(GlobalVariable.pos, which_fragment));

			// send data to backend

			String edittext_address_string = edittext_address.getText()
					.toString();
			String edittext_name_string = edittext_name.getText().toString();
			if (mFileName != null) {
				File recordedfile = new File(mFileName);
				if (recordedfile.exists() && edittext_address_string != null
						&& edittext_name_string != null) {
					progress = new ProgressDialog(getActivity());
					progress.setMessage("Uploading....");
					progress.setMax(100);
					progress.setProgress(0);
					progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progress.setIndeterminate(true);
					progress.show();

					CountingTypedFile recording = new CountingTypedFile(
							"audio/3gp", recordedfile, new ProgressListener() {

								@Override
								public void transferred(long num) {
									// TODO Auto-generated method stub
									progress.setProgress((int) num);
								}
							});

					getService().updateUser(recording, edittext_address_string,
							edittext_name_string,
							new Callback<MODEL_RESPONSE>() {

								@Override
								public void success(MODEL_RESPONSE data,
										Response arg1) {
									// TODO Auto-generated method stub
									switch (data.getStatus_code()) {
									case 100:

										GlobalVariable.showToast(getActivity(),
												data.getMessage());

										progress.dismiss();
										break;

									case 200:
										GlobalVariable.showToast(getActivity(),
												data.getMessage());

										progress.dismiss();

										break;
									}
								}

								@Override
								public void failure(RetrofitError arg0) {
									// TODO Auto-generated method stub
									progress.dismiss();

									try {
										// cause can be null

										GlobalVariable.showToast(getActivity(),
												arg0.getCause().getMessage());

									} catch (Exception e) {
										// TODO: handle exception
									}

								}
							});
				}

			}

		}
			break;
		}

	}

}
