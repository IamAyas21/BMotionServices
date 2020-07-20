package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseUser;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.UploadService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.imgCamera)
    ImageView mCamera;

    @BindView(R.id.btnSignUp)
    Button bSignUp;

    @BindView(R.id.edtNIK)
    EditText eNIK;

    @BindView(R.id.edtFullName)
    EditText eFullName;

    @BindView(R.id.edtEmail)
    EditText eEmail;

    @BindView(R.id.edtPhone)
    EditText ePhone;

    @BindView(R.id.edtCity)
    EditText eCity;

    ApiInterface apiService;
    static final int REQUEST_TAKE_PHOTO = 100;
    private Uri uri;
    private Bitmap imageBitmap;
    private String currentPhotoPath;
    private File image;
    private UploadService uploadService;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(image);
            }
        });
    }


    private void signUp(File file)
    {
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("imagektp",
                file.getName(), photoBody);

        User user = new User();
        user.setNIP(eNIK.getText().toString());
        user.setEmail(eEmail.getText().toString());
        user.setName(eFullName.getText().toString());
        user.setPhone(ePhone.getText().toString());
        user.setKTP(file.getName());

        uploadService = new UploadService();
        uploadService.SignUpMultipart(user, photoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ResponseUser baseResponse = (ResponseUser) response.body();

                if(baseResponse != null) {
                    if(baseResponse.getStatus().equals("success"))
                    {
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(this,
                        "com.bmotion.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        /*Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, REQUEST_TAKE_PHOTO);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            mCamera.setImageBitmap(imageBitmap);*/

            mCamera.setImageURI(uri);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ktp_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
