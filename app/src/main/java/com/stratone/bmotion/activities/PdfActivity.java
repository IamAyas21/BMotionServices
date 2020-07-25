package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;
import com.stratone.bmotion.R;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    private int pageNumber = 0;

    private String pdfFileName;
    private PDFView pdfView;
    public ProgressDialog pDialog;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private String filePath, nik, fullName, email, password, phone, city, expDate;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        pdfView = (PDFView) findViewById(R.id.pdfView);

        GetPutExtra();
    }

    private void GetPutExtra()
    {
        if(getIntent().getExtras()!=null){
            filePath = String.valueOf(getIntent().getStringExtra("path_file")) ;
            nik = String.valueOf(getIntent().getStringExtra("nik"));
            fullName = String.valueOf(getIntent().getStringExtra("full_name"));
            email = String.valueOf(getIntent().getStringExtra("email_address"));
            password = String.valueOf(getIntent().getStringExtra("password"));
            phone = String.valueOf(getIntent().getStringExtra("phone_number"));
            city = String.valueOf(getIntent().getStringExtra("city"));
            expDate = String.valueOf(getIntent().getStringExtra("exp_date"));

            if(getIntent().getStringExtra("image_ktp") != null)
            {
                uri = Uri.parse(getIntent().getStringExtra("image_ktp"));
            }
        }
    }

    private void SetPutExtra(Intent intent)
    {
        if(uri != null)
        {
            intent.putExtra("image_ktp",uri.toString());
        }

        intent.putExtra("nik",nik);
        intent.putExtra("full_name",fullName);
        intent.putExtra("email_address",email);
        intent.putExtra("password",password);
        intent.putExtra("phone_number",phone);
        intent.putExtra("city",city);
        intent.putExtra("exp_date",expDate);
        intent.putExtra("path_file",filePath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pickFile:
                launchPicker();
                return true;
            case R.id.OkPdf:
                OkPDF();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }

    private void OkPDF()
    {
       Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
       SetPutExtra(intent);
       this.startActivity(intent);
       finish();
    }
    private void launchPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.pdf$"))
                .withTitle("Select PDF file")
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            File file = new File(path);
            displayFromFile(file);
            if (path != null) {
                Log.d("Path: ", path);
                filePath = path;
                Toast.makeText(this, "Picked file: " + path, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void displayFromFile(File file) {

        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        pdfFileName = getFileName(uri);

        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }



    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            //Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PdfActivity.this, RegisterActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        SetPutExtra(i);
        startActivity(i);
        finish();
    }
}
