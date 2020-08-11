package rest;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostFile {

	public static void main(String[] args) throws Exception {
		new PostFile().run();

	}

	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

	private final OkHttpClient client = new OkHttpClient();

	public void run() throws Exception {
		File file = new File("d:/aaa.xlsx");

		RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "aaa.xlsx",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:8000/demo/upfile.do")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

       
	}
}
